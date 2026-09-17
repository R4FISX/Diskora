package com.diskora;

import javafx.application.Application;

/** Entry point kept separate so the JavaFX launcher works reliably on the classpath. */
public final class Launcher {

    private Launcher() {
    }

    public static void main(String[] args) {
        Application.launch(DiskoraApplication.class, args);
    }
}
