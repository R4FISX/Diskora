package com.diskora.view;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public final class MainView extends BorderPane {

    public MainView(SidebarView sidebar) {
        getStyleClass().add("app-shell");
        setLeft(sidebar);
    }

    public void showContent(Node content) {
        setCenter(content);
    }
}
