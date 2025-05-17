package com.syafiqriza.rushhoursolver.view;

import com.syafiqriza.rushhoursolver.model.Board;
import com.syafiqriza.rushhoursolver.model.State;
import com.syafiqriza.rushhoursolver.model.Utils;
import com.syafiqriza.rushhoursolver.model.algorithm.Algorithm;
import com.syafiqriza.rushhoursolver.model.algorithm.UniformCostSearch;
import com.syafiqriza.rushhoursolver.model.heuristic.BlockingHeuristic;
import com.syafiqriza.rushhoursolver.model.heuristic.Heuristic;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class GUI extends Application {
    private Board board;
    private Algorithm algorithm;
    private Heuristic heuristic = new BlockingHeuristic();
    private Font customFont;

    @Override
    public void start(Stage stage) {
        customFont = Font.loadFont(getClass().getResourceAsStream("/RacingEngine-Regular.otf"), 16);

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0f0f0f")), new Stop(1, Color.web("#1c1c1c"))
                ), CornerRadii.EMPTY, Insets.EMPTY)));

        // === Title Box with Checkered Borders ===
        VBox titleBox = new VBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#ffff00")), new Stop(1, Color.web("#ffff00"))
                ), CornerRadii.EMPTY, Insets.EMPTY)));

        BackgroundImage checkeredBg = new BackgroundImage(
                new Image(getClass().getResourceAsStream("/checkered.jpg")),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(BackgroundSize.AUTO, 20, false, false, false, false)
        );

        Region topBar = new Region();
        topBar.setPrefHeight(20);
        topBar.setBackground(new Background(checkeredBg));

        Region bottomBar = new Region();
        bottomBar.setPrefHeight(20);
        bottomBar.setBackground(new Background(checkeredBg));

        Text title = new Text("RUSH HOUR SOLVER");
        title.setFont(Font.loadFont(getClass().getResourceAsStream("/RacingEngine-Sharps.otf"), 36));
        title.setFill(Color.BLACK);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), title);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        VBox titleTextBox = new VBox(title);
        titleTextBox.setAlignment(Pos.CENTER);
        titleTextBox.setPadding(new Insets(10));

        titleBox.getChildren().addAll(topBar, titleTextBox, bottomBar);
        root.setTop(titleBox);

        // === Menu Box ===
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(40));
        menuBox.setMaxWidth(350);
        menuBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 15;");

        Text subtitle = styledLabel("Pilih algoritma dan puzzle:");
        subtitle.setFill(Color.LIGHTYELLOW);

        ComboBox<String> algoSelector = new ComboBox<>();
        algoSelector.getItems().addAll("Uniform Cost Search", "Greedy Best First Search", "A*");
        algoSelector.getSelectionModel().selectFirst();
        algoSelector.setPrefWidth(250);
        algoSelector.setStyle("-fx-font-size: 14px; -fx-background-color: #2c2c2c; -fx-text-fill: white;" +
                " -fx-border-color: yellow; -fx-border-radius: 5; -fx-background-radius: 5;");

        Button loadButton = createStyledButton("\uD83D\uDCC2 Load Puzzle");
        Button solveButton = createStyledButton("\uD83D\uDE80 Cari Solusi");

        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Puzzle File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    board = Utils.readRushHourPuzzleFromFile(file.getAbsolutePath());
                    showAlert("Sukses", "Puzzle berhasil dimuat!");
                } catch (IOException | IllegalArgumentException ex) {
                    showAlert("Gagal", "Error membaca puzzle: " + ex.getMessage());
                }
            }
        });

        solveButton.setOnAction(e -> {
            if (board == null) {
                showAlert("Error", "Silakan load puzzle terlebih dahulu.");
                return;
            }
            int choice = algoSelector.getSelectionModel().getSelectedIndex();
            switch (choice) {
                case 0 -> algorithm = new UniformCostSearch();
                case 1 -> algorithm = new UniformCostSearch();
                case 2 -> algorithm = new UniformCostSearch();
                default -> showAlert("Error", "Algoritma tidak dikenali.");
            }
            algorithm.solve(new State(board, 0, 0));
            showAlert("Selesai", "Solusi ditemukan! (visualisasi menyusul)");
        });

        menuBox.getChildren().addAll(subtitle, algoSelector, loadButton, solveButton);
        root.setCenter(menuBox);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Rush Hour Solver - Menu Utama");
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(customFont);
        button.setStyle("-fx-background-color: linear-gradient(to bottom, #fff200, #ffd000); -fx-text-fill: black; " +
                "-fx-font-size: 16px; -fx-border-color: black; -fx-border-width: 2px; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8px 16px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: black; -fx-text-fill: white; " +
                "-fx-font-size: 16px; -fx-border-color: white; -fx-border-width: 2px; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8px 16px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: linear-gradient(to bottom, #fff200, #ffd000); -fx-text-fill: black; " +
                "-fx-font-size: 16px; -fx-border-color: black; -fx-border-width: 2px; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8px 16px;"));
        return button;
    }


    private Text styledLabel(String content) {
        Text label = new Text(content);
        label.setFont(customFont);
        label.setFill(Color.WHITE);
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
