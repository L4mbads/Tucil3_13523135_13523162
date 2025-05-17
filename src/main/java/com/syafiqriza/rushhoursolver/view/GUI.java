package com.syafiqriza.rushhoursolver.view;
import com.syafiqriza.rushhoursolver.model.Board;
import com.syafiqriza.rushhoursolver.model.Utils;
import com.syafiqriza.rushhoursolver.model.algorithm.Algorithm;
import com.syafiqriza.rushhoursolver.model.algorithm.GreedyBestFirstSearch;
import com.syafiqriza.rushhoursolver.model.algorithm.InformedSearch;
import com.syafiqriza.rushhoursolver.model.algorithm.UniformCostSearch;
import com.syafiqriza.rushhoursolver.model.heuristic.BlockingHeuristic;
import com.syafiqriza.rushhoursolver.model.heuristic.DistanceHeuristic;
import com.syafiqriza.rushhoursolver.model.heuristic.Heuristic;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
    private Font customFont;
    private Text resultText;
    private Text errorMessageText;
    private VBox boardViewContainer;

    @Override
    public void start(Stage stage) {
        customFont = Font.loadFont(getClass().getResourceAsStream("/RacingEngine-Regular.otf"), 16);

        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0f0f0f")), new Stop(1, Color.web("#1c1c1c"))
                ), CornerRadii.EMPTY, Insets.EMPTY)));

        VBox titleBox = createTitleBox();
        root.setTop(titleBox);

        VBox menuBox = createMenuBox(stage);
        root.setCenter(menuBox);

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Rush Hour Solver - Menu Utama");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createTitleBox() {
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
        return titleBox;
    }

    private VBox createMenuBox(Stage stage) {
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setPadding(new Insets(40));
        menuBox.setMaxWidth(350);
        menuBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.05); -fx-background-radius: 15;");

        Text subtitle = styledLabel("Pilih algoritma dan puzzle:");
        subtitle.setFill(Color.LIGHTYELLOW);

        errorMessageText = styledLabel("");
        errorMessageText.setFill(Color.ORANGERED);

        ComboBox<String> algoSelector = new ComboBox<>();
        algoSelector.getItems().addAll("Uniform Cost Search", "Greedy Best First Search", "A*");
        algoSelector.getSelectionModel().selectFirst();
        algoSelector.setPrefWidth(250);
        algoSelector.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-background-color: #2c2c2c;" +
                        "-fx-text-fill: #ffff00;" +
                        "-fx-prompt-text-fill: #ffff00;" +
                        "-fx-mark-color: yellow;" +
                        "-fx-border-color: yellow;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );
        algoSelector.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setTextFill(Color.web("#ffff00"));
                setFont(customFont);
            }
        });

        Button loadButton = createStyledButton("\uD83D\uDCC2 Load Puzzle");
        Button solveButton = createStyledButton("\uD83D\uDE80 Cari Solusi");

        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Puzzle File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    board = Utils.readRushHourPuzzleFromFile(file.getAbsolutePath());
                    errorMessageText.setFill(Color.LIMEGREEN);
                    errorMessageText.setText("Puzzle berhasil dimuat");
                } catch (IOException | IllegalArgumentException ex) {
                    errorMessageText.setFill(Color.ORANGERED);
                    errorMessageText.setText("Error membaca puzzle");
                }
            }
        });

        solveButton.setOnAction(e -> {
            errorMessageText.setFill(Color.ORANGERED);
            if (board == null) {
                errorMessageText.setText("Silakan load puzzle terlebih dahulu");
                return;
            }
            int choice = algoSelector.getSelectionModel().getSelectedIndex();
            switch (choice) {
                case 0 -> algorithm = new UniformCostSearch();
                case 1 -> {
                    algorithm = new GreedyBestFirstSearch();
                    if (algorithm instanceof InformedSearch alg) {
                        alg.setHeuristicModel(new BlockingHeuristic());
                    }
                }
                case 2 -> {
                    algorithm = new GreedyBestFirstSearch();
                    if (algorithm instanceof InformedSearch alg) {
                        alg.setHeuristicModel(new DistanceHeuristic());
                    }
                }
                default -> algorithm = null;
            }

            if (algorithm == null) {
                errorMessageText.setText("Algoritma tidak dikenali");
                return;
            }

            showProcessWindow(stage);

            Task<Void> solveTask = new Task<>() {
                private long duration;
                private boolean found;

                @Override
                protected Void call() {
                    long start = System.nanoTime();
                    com.syafiqriza.rushhoursolver.model.State initial = new com.syafiqriza.rushhoursolver.model.State(board, 0, 0);
                    algorithm.solve(initial);
                    found = algorithm.getSolution() != null && algorithm.getSolution().states.length != 0;
                    duration = (System.nanoTime() - start) / 1_000_000;
                    return null;
                }

                @Override
                protected void succeeded() {
                    String message = found ?
                            "Solusi ditemukan\nWaktu pencarian: " + duration + " ms" :
                            "Solusi tidak ditemukan.\nWaktu pencarian: " + duration + " ms";
                    showSolutionWindow(stage, message, found);
                }
            };
            new Thread(solveTask).start();
        });

        menuBox.getChildren().addAll(subtitle, errorMessageText, algoSelector, loadButton, solveButton);
        return menuBox;
    }

    private void showProcessWindow(Stage stage) {
        BorderPane processLayout = new BorderPane();
        processLayout.setBackground(new Background(new BackgroundFill(Color.web("#1c1c1c"), CornerRadii.EMPTY, Insets.EMPTY)));

        resultText = styledLabel("Mencari solusi...");
        boardViewContainer = new VBox();
        boardViewContainer.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(20, resultText, boardViewContainer);
        centerBox.setAlignment(Pos.CENTER);

        processLayout.setCenter(centerBox);
        Scene processScene = new Scene(processLayout, 800, 600);
        stage.setScene(processScene);
    }

    private void showSolutionWindow(Stage stage, String resultMessage, boolean found) {
        BorderPane layout = new BorderPane();
        layout.setBackground(new Background(new BackgroundFill(Color.web("#0f0f0f"), CornerRadii.EMPTY, Insets.EMPTY)));

        Text messageText = styledLabel(resultMessage);
        Button backButton = createStyledButton("⬅ Kembali");
        backButton.setOnAction(e -> start(stage));

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        BoardView boardView = new BoardView();
        centerBox.getChildren().addAll(messageText, boardView, backButton);

        layout.setCenter(centerBox);
        Scene solutionScene = new Scene(layout, 800, 600);
        stage.setScene(solutionScene);

        if (found && algorithm.getSolution().states.length != 0) {
            Task<Void> animationTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    var steps = algorithm.getSolution().states;
                    for (var state : steps) {
                        Platform.runLater(() -> boardView.draw(state));
                        Thread.sleep(500); // delay antar langkah
                    }
                    return null;
                }
            };
            new Thread(animationTask).start();
        }
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
