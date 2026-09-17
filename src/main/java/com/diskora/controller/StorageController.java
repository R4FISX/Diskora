package com.diskora.controller;

import com.diskora.model.DiskInfo;
import com.diskora.service.DiskService;
import com.diskora.view.StorageView;
import javafx.concurrent.Task;

import java.util.List;

public final class StorageController {

    private final DiskService diskService;
    private final StorageView view;

    public StorageController(DiskService diskService) {
        this.diskService = diskService;
        this.view = new StorageView(this::refresh);
    }

    public StorageView getView() {
        return view;
    }

    public void refresh() {
        view.showLoading();
        Task<List<DiskInfo>> task = new Task<>() {
            @Override
            protected List<DiskInfo> call() {
                return diskService.detectDisks();
            }
        };
        task.setOnSucceeded(event -> view.update(task.getValue()));
        task.setOnFailed(event -> view.showError("Leitura indisponível"));

        Thread thread = new Thread(task, "diskora-storage-loader");
        thread.setDaemon(true);
        thread.start();
    }
}
