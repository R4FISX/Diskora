package com.diskora.model;

public record DiskInfo(
        String mountPoint,
        String volumeName,
        String fileSystem,
        long totalBytes,
        long usedBytes,
        long freeBytes,
        double usagePercent,
        String model,
        DiskType type) {

    public String displayName() {
        if (volumeName == null || volumeName.isBlank()) {
            return mountPoint;
        }
        return volumeName + " (" + mountPoint + ")";
    }
}
