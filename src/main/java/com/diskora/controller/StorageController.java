package com.diskora.controller;

import com.diskora.service.DiskService;
import com.diskora.view.StorageView;

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
        try {
            view.update(diskService.detectDisks());
        } catch (RuntimeException exception) {
            view.showError("Leitura indisponível");
        }
    }
}
