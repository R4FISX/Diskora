package com.diskora.view;

import com.diskora.model.DiskInfo;
import com.diskora.model.SystemInfoSnapshot;
import com.diskora.util.FormatUtils;
import com.diskora.util.ViewUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public final class DashboardView extends VBox {

    private final Label diskCountValue = new Label("—");
    private final Label totalStorageValue = new Label("—");
    private final Label usedStorageValue = new Label("—");
    private final Label systemValue = new Label("Carregando...");
    private final Label computerValue = new Label("Carregando...");
    private final Label processorValue = new Label("Carregando...");
    private final Label memoryValue = new Label("Carregando...");
    private final VBox diskSummary = new VBox(12);

    public DashboardView() {
        getStyleClass().add("page");
        setPadding(new Insets(36, 42, 36, 42));
        setSpacing(28);

        Label eyebrow = ViewUtils.eyebrow("VISÃO GERAL");
        Label title = new Label("Tudo sob controle.");
        title.getStyleClass().add("page-title");
        Label description = new Label("Acompanhe o estado do seu sistema e dos seus discos em um só lugar.");
        description.getStyleClass().add("page-description");
        VBox header = new VBox(8, eyebrow, title, description);

        HBox statCards = new HBox(14,
                statCard("UNIDADES DETECTADAS", diskCountValue, "volumes locais"),
                statCard("ESPAÇO TOTAL", totalStorageValue, "em todas as unidades"),
                statCard("ESPAÇO EM USO", usedStorageValue, "soma das unidades"));
        HBox.setHgrow(statCards.getChildren().get(0), Priority.ALWAYS);
        HBox.setHgrow(statCards.getChildren().get(1), Priority.ALWAYS);
        HBox.setHgrow(statCards.getChildren().get(2), Priority.ALWAYS);

        VBox storageCard = card();
        Label storageTitle = new Label("Resumo do armazenamento");
        storageTitle.getStyleClass().add("section-title");
        Label storageHint = new Label("Uso atual por unidade lógica");
        storageHint.getStyleClass().add("muted");
        diskSummary.setPadding(new Insets(16, 0, 0, 0));
        storageCard.getChildren().addAll(storageTitle, storageHint, diskSummary);

        VBox systemCard = card();
        Label systemTitle = new Label("Informações do sistema");
        systemTitle.getStyleClass().add("section-title");
        GridPane systemGrid = new GridPane();
        systemGrid.setHgap(28);
        systemGrid.setVgap(14);
        addSystemField(systemGrid, "SISTEMA OPERACIONAL", systemValue, 0, 0);
        addSystemField(systemGrid, "COMPUTADOR", computerValue, 1, 0);
        addSystemField(systemGrid, "PROCESSADOR", processorValue, 0, 1);
        addSystemField(systemGrid, "MEMÓRIA", memoryValue, 1, 1);
        systemCard.getChildren().addAll(systemTitle, systemGrid);

        HBox lower = new HBox(14, storageCard, systemCard);
        HBox.setHgrow(storageCard, Priority.ALWAYS);
        HBox.setHgrow(systemCard, Priority.ALWAYS);
        storageCard.setPrefWidth(500);
        systemCard.setPrefWidth(500);

        getChildren().addAll(header, statCards, lower);
        VBox.setVgrow(lower, Priority.ALWAYS);
    }

    public void update(List<DiskInfo> disks, SystemInfoSnapshot system) {
        diskCountValue.setText(String.valueOf(disks.size()));
        totalStorageValue.setText(FormatUtils.bytes(disks.stream().mapToLong(DiskInfo::totalBytes).sum()));
        usedStorageValue.setText(FormatUtils.bytes(disks.stream().mapToLong(DiskInfo::usedBytes).sum()));

        systemValue.setText(system.operatingSystem() + " " + system.osVersion());
        computerValue.setText(system.manufacturer() + " " + system.computerModel());
        processorValue.setText(system.processor());
        memoryValue.setText(FormatUtils.bytes(system.availableMemoryBytes()) + " livres de "
                + FormatUtils.bytes(system.totalMemoryBytes()));

        diskSummary.getChildren().clear();
        if (disks.isEmpty()) {
            Label empty = new Label("Nenhuma unidade local foi encontrada.");
            empty.getStyleClass().add("muted");
            diskSummary.getChildren().add(empty);
            return;
        }
        disks.forEach(disk -> diskSummary.getChildren().add(diskRow(disk)));
    }

    public void showError(String message) {
        systemValue.setText(message);
        computerValue.setText("—");
        processorValue.setText("—");
        memoryValue.setText("—");
        diskSummary.getChildren().clear();
        Label error = new Label("Não foi possível carregar os dados agora.");
        error.getStyleClass().add("error-text");
        diskSummary.getChildren().add(error);
    }

    private VBox statCard(String label, Label value, String caption) {
        VBox card = card();
        Label labelView = new Label(label);
        labelView.getStyleClass().add("card-label");
        value.getStyleClass().add("stat-value");
        Label captionView = new Label(caption);
        captionView.getStyleClass().add("muted");
        card.getChildren().addAll(labelView, value, captionView);
        return card;
    }

    private VBox card() {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(20));
        return card;
    }

    private void addSystemField(GridPane grid, String label, Label value, int column, int row) {
        Label fieldLabel = new Label(label);
        fieldLabel.getStyleClass().add("card-label");
        value.getStyleClass().add("field-value");
        VBox field = new VBox(4, fieldLabel, value);
        field.setMinWidth(220);
        grid.add(field, column, row);
    }

    private HBox diskRow(DiskInfo disk) {
        Label mount = new Label(disk.mountPoint());
        mount.getStyleClass().add("drive-badge");
        Label name = new Label(disk.displayName());
        name.getStyleClass().add("row-title");
        Label usage = new Label(FormatUtils.percentage(disk.usagePercent()));
        usage.getStyleClass().add("row-value");

        ProgressBar bar = new ProgressBar(disk.usagePercent() / 100.0);
        bar.setMaxWidth(Double.MAX_VALUE);
        bar.getStyleClass().add("usage-bar");
        HBox.setHgrow(bar, Priority.ALWAYS);
        HBox top = new HBox(10, mount, name, bar, usage);
        top.setAlignment(Pos.CENTER_LEFT);

        Label details = new Label(FormatUtils.bytes(disk.usedBytes()) + " usados  ·  "
                + FormatUtils.bytes(disk.freeBytes()) + " livres  ·  " + disk.fileSystem());
        details.getStyleClass().add("muted");
        VBox row = new VBox(6, top, details);
        return new HBox(row);
    }
}
