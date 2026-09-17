package com.diskora.view;

import com.diskora.model.DiskInfo;
import com.diskora.util.AnimationUtils;
import com.diskora.util.FormatUtils;
import com.diskora.util.ViewUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public final class StorageView extends VBox {

    private final VBox diskList = new VBox(14);
    private final Label status = new Label("Aguardando leitura...");
    private final Button refreshButton = new Button("↻  Atualizar");

    public StorageView(Runnable onRefresh) {
        getStyleClass().add("page");
        setPadding(new Insets(36, 42, 36, 42));
        setSpacing(28);

        Label eyebrow = ViewUtils.eyebrow("ARMAZENAMENTO");
        Label title = new Label("Suas unidades.");
        title.getStyleClass().add("page-title");
        Label description = new Label("Veja capacidade, uso e detalhes técnicos dos volumes locais.");
        description.getStyleClass().add("page-description");

        refreshButton.getStyleClass().add("secondary-button");
        AnimationUtils.installNavHover(refreshButton);
        refreshButton.setOnAction(event -> {
            AnimationUtils.playSpin(refreshButton);
            onRefresh.run();
        });
        HBox headerLine = new HBox(18, new VBox(8, eyebrow, title, description), refreshButton);
        headerLine.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(headerLine.getChildren().get(0), Priority.ALWAYS);

        status.getStyleClass().add("muted");
        diskList.setFillWidth(true);
        getChildren().addAll(headerLine, status, diskList);
        VBox.setVgrow(diskList, Priority.ALWAYS);
    }

    public void update(List<DiskInfo> disks) {
        diskList.getChildren().clear();
        status.setText(disks.size() + (disks.size() == 1 ? " unidade detectada" : " unidades detectadas"));
        status.getStyleClass().remove("error-text");
        if (disks.isEmpty()) {
            Label empty = new Label("Nenhuma unidade local foi encontrada.");
            empty.getStyleClass().add("muted");
            diskList.getChildren().add(empty);
            return;
        }
        disks.forEach(disk -> diskList.getChildren().add(diskCard(disk)));
        AnimationUtils.playStaggered(diskList.getChildren());
    }

    public void showLoading() {
        status.getStyleClass().remove("error-text");
        status.setText("Atualizando unidades...");
        diskList.getChildren().clear();
        Label loading = new Label("Lendo informações locais...");
        loading.getStyleClass().add("muted");
        diskList.getChildren().add(loading);
    }

    public void showError(String message) {
        diskList.getChildren().clear();
        status.setText(message);
        status.getStyleClass().add("error-text");
    }

    private VBox diskCard(DiskInfo disk) {
        VBox card = new VBox(18);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(22));
        AnimationUtils.installHoverLift(card);

        Label driveBadge = new Label(disk.mountPoint());
        driveBadge.getStyleClass().add("drive-badge-large");
        Label title = new Label(disk.displayName());
        title.getStyleClass().add("section-title");
        Label type = new Label(disk.type().label());
        type.getStyleClass().add("type-pill");
        HBox heading = new HBox(12, driveBadge, title, type);
        heading.setAlignment(Pos.CENTER_LEFT);

        ProgressBar usageBar = new ProgressBar(disk.usagePercent() / 100.0);
        usageBar.setMaxWidth(Double.MAX_VALUE);
        usageBar.getStyleClass().add("usage-bar");
        AnimationUtils.animateProgress(usageBar, disk.usagePercent() / 100.0);

        Label usage = new Label(FormatUtils.bytes(disk.usedBytes()) + " usados de "
                + FormatUtils.bytes(disk.totalBytes()));
        usage.getStyleClass().add("row-title");
        Label percentage = new Label(FormatUtils.percentage(disk.usagePercent()));
        percentage.getStyleClass().add("row-value");
        HBox usageLine = new HBox(12, usage, usageBar, percentage);
        usageLine.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(usageBar, Priority.ALWAYS);

        HBox metadata = new HBox(36,
                metadataField("LIVRE", FormatUtils.bytes(disk.freeBytes())),
                metadataField("SISTEMA DE ARQUIVOS", disk.fileSystem()),
                metadataField("MODELO", disk.model()));
        metadata.setPadding(new Insets(2, 0, 0, 0));
        card.getChildren().addAll(heading, usageLine, metadata);
        return card;
    }

    private VBox metadataField(String label, String value) {
        Label labelView = new Label(label);
        labelView.getStyleClass().add("card-label");
        Label valueView = new Label(value);
        valueView.getStyleClass().add("field-value");
        return new VBox(5, labelView, valueView);
    }
}
