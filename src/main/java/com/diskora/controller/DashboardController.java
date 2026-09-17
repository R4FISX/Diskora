package com.diskora.controller;

import com.diskora.model.DiskInfo;
import com.diskora.model.SystemInfoSnapshot;
import com.diskora.service.DiskService;
import com.diskora.service.SystemInfoService;
import com.diskora.view.DashboardView;
import javafx.concurrent.Task;

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
        view.showLoading();
        Task<DashboardData> task = new Task<>() {
            @Override
            protected DashboardData call() {
                List<DiskInfo> disks = diskService.detectDisks();
                SystemInfoSnapshot system = systemInfoService.readSnapshot();
                return new DashboardData(disks, system);
            }
        };
        task.setOnSucceeded(event -> view.update(task.getValue().disks(), task.getValue().system()));
        task.setOnFailed(event -> view.showError("Leitura indisponível"));
        startTask(task, "diskora-dashboard-loader");
    }

    private void startTask(Task<?> task, String threadName) {
        Thread thread = new Thread(task, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    private record DashboardData(List<DiskInfo> disks, SystemInfoSnapshot system) {
    }
}
