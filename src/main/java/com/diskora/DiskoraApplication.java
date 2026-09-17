package com.diskora;

import com.diskora.controller.MainController;
import com.diskora.service.DiskService;
import com.diskora.service.SystemInfoService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class DiskoraApplication extends Application {

    @Override
    public void start(Stage stage) {
        MainController mainController = new MainController(
                new DiskService(),
                new SystemInfoService());

        Scene scene = new Scene(mainController.getView(), 1_240, 780);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("Diskora");
        stage.setMinWidth(1_020);
        stage.setMinHeight(680);
        stage.setScene(scene);
        stage.show();
    }
}
