package com.diskora.controller;

import com.diskora.model.DiskInfo;
import com.diskora.model.SystemInfoSnapshot;
import com.diskora.service.DiskService;
import com.diskora.service.SystemInfoService;
import com.diskora.view.DashboardView;

import java.util.List;

public final class DashboardController {

    private final DiskService diskService;
    private final SystemInfoService systemInfoService;
    private final DashboardView view = new DashboardView();

    public DashboardController(DiskService diskService, SystemInfoService systemInfoService) {
        this.diskService = diskService;
        this.systemInfoService = systemInfoService;
    }

    public DashboardView getView() {
        return view;
    }

    public void refresh() {
        try {
            List<DiskInfo> disks = diskService.detectDisks();
            SystemInfoSnapshot system = systemInfoService.readSnapshot();
            view.update(disks, system);
        } catch (RuntimeException exception) {
            view.showError("Leitura indisponível");
        }
    }
}
