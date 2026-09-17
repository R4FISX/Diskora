package com.diskora.model;

public enum DiskType {
    HDD("HDD"),
    SSD("SSD"),
    NVME("NVMe"),
    UNKNOWN("Desconhecido");

    private final String label;

    DiskType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
