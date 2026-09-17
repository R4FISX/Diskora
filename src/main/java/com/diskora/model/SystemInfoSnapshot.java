package com.diskora.model;

public record SystemInfoSnapshot(
        String operatingSystem,
        String osVersion,
        String computerName,
        String manufacturer,
        String computerModel,
        String processor,
        long totalMemoryBytes,
        long availableMemoryBytes) {
}
