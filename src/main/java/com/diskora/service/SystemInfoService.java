package com.diskora.service;

import com.diskora.model.SystemInfoSnapshot;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

/** Reads basic operating-system and hardware information through OSHI. */
public final class SystemInfoService {

    public SystemInfoSnapshot readSnapshot() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        ComputerSystem computerSystem = systemInfo.getHardware().getComputerSystem();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        GlobalMemory memory = systemInfo.getHardware().getMemory();

        return new SystemInfoSnapshot(
                operatingSystem.getFamily(),
                operatingSystem.getVersionInfo().toString(),
                operatingSystem.getNetworkParams().getHostName(),
                safeValue(computerSystem.getManufacturer()),
                safeValue(computerSystem.getModel()),
                safeValue(processor.getProcessorIdentifier().getName()),
                Math.max(0, memory.getTotal()),
                Math.max(0, memory.getAvailable()));
    }

    private String safeValue(String value) {
        return value == null || value.isBlank() ? "Desconhecido" : value.trim();
    }
}
