package com.diskora.controller;

import com.diskora.service.DiskService;
import com.diskora.service.SystemInfoService;
import com.diskora.view.MainView;
import com.diskora.view.PlaceholderView;
import com.diskora.view.SidebarView;

public final class MainController {

    private final MainView view;
    private final SidebarView sidebar;
    private final DashboardController dashboardController;
    private final StorageController storageController;

    public MainController(DiskService diskService, SystemInfoService systemInfoService) {
        sidebar = new SidebarView(this::navigate);
        view = new MainView(sidebar);
        dashboardController = new DashboardController(diskService, systemInfoService);
        storageController = new StorageController(diskService);
        navigate("Dashboard");
    }

    public MainView getView() {
        return view;
    }

    private void navigate(String page) {
        sidebar.select(page);
        switch (page) {
            case "Dashboard" -> {
                view.showContent(dashboardController.getView());
                dashboardController.refresh();
            }
            case "Armazenamento" -> {
                view.showContent(storageController.getView());
                storageController.refresh();
            }
            default -> view.showContent(new PlaceholderView(page));
        }
    }
}
