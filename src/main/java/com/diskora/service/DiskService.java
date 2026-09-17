package com.diskora.service;

import com.diskora.model.DiskInfo;
import com.diskora.model.DiskType;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import oshi.software.os.OSFileStore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Reads logical volumes and their best-effort physical disk metadata through OSHI. */
public final class DiskService {

    public List<DiskInfo> detectDisks() {
        SystemInfo systemInfo = new SystemInfo();
        Map<String, HWDiskStore> disksByMount = mapPhysicalDisks(systemInfo);
        List<DiskInfo> result = new ArrayList<>();

        for (OSFileStore fileStore : systemInfo.getOperatingSystem().getFileSystem().getFileStores()) {
            String mountPoint = normalizeMountPoint(fileStore.getMount());
            if (!isWindowsDrive(mountPoint)) {
                continue;
            }

            long totalBytes = nonNegative(fileStore.getTotalSpace());
            long freeBytes = nonNegative(fileStore.getUsableSpace());
            long usedBytes = Math.max(0, totalBytes - freeBytes);
            double usagePercent = totalBytes == 0 ? 0 : (usedBytes * 100.0) / totalBytes;

            HWDiskStore physicalDisk = disksByMount.get(mountPoint.toUpperCase(Locale.ROOT));
            String model = physicalDisk == null ? "Desconhecido" : cleanModel(physicalDisk.getModel());
            DiskType type = inferDiskType(physicalDisk, model);
            String volumeName = firstNonBlank(fileStore.getLabel(), fileStore.getName(), mountPoint);
            String fileSystem = firstNonBlank(fileStore.getType(), "Desconhecido");

            result.add(new DiskInfo(
                    mountPoint,
                    volumeName,
                    fileSystem,
                    totalBytes,
                    usedBytes,
                    freeBytes,
                    Math.min(100, Math.max(0, usagePercent)),
                    model,
                    type));
        }

        result.sort(Comparator.comparing(DiskInfo::mountPoint));
        return List.copyOf(result);
    }

    private Map<String, HWDiskStore> mapPhysicalDisks(SystemInfo systemInfo) {
        Map<String, HWDiskStore> result = new HashMap<>();
        for (HWDiskStore disk : systemInfo.getHardware().getDiskStores()) {
            for (HWPartition partition : disk.getPartitions()) {
                String mountPoint = normalizeMountPoint(partition.getMountPoint());
                if (isWindowsDrive(mountPoint)) {
                    result.putIfAbsent(mountPoint.toUpperCase(Locale.ROOT), disk);
                }
            }
        }
        return result;
    }

    private DiskType inferDiskType(HWDiskStore physicalDisk, String model) {
        String value = (model + " " + (physicalDisk == null ? "" : physicalDisk.getName()))
                .toLowerCase(Locale.ROOT);
        if (value.contains("nvme")) {
            return DiskType.NVME;
        }
        if (value.contains("ssd") || value.contains("solid state")) {
            return DiskType.SSD;
        }
        if (value.contains("hdd") || value.contains("hard disk") || value.contains("harddrive")) {
            return DiskType.HDD;
        }
        return DiskType.UNKNOWN;
    }

    private String normalizeMountPoint(String mountPoint) {
        if (mountPoint == null) {
            return "";
        }
        String normalized = mountPoint.trim().replace('/', '\\');
        if (normalized.length() >= 2 && normalized.charAt(1) == ':') {
            return normalized.substring(0, 2).toUpperCase(Locale.ROOT);
        }
        return normalized;
    }

    private boolean isWindowsDrive(String mountPoint) {
        return mountPoint.length() == 2
                && Character.isLetter(mountPoint.charAt(0))
                && mountPoint.charAt(1) == ':';
    }

    private long nonNegative(long value) {
        return Math.max(0, value);
    }

    private String cleanModel(String model) {
        return firstNonBlank(model, "Desconhecido").trim();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return "Desconhecido";
    }
}
