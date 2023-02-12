package com.mdg.whatsmylunch;

import static javafx.geometry.Pos.*;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.mdg.whatsmylunch.domain.Restaurant;
import com.mdg.whatsmylunch.util.SQLiteManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApplication extends Application {
    private static List<Restaurant> restaurants = List.of();

    @Override
    public void start(Stage stage) {
        TableView<Restaurant> tableView = createRestaurantTableView();

        Label label = new Label("식당 이름: ");
        TextField textField = new TextField();
        Button addButton = new Button("추가");
        Button deleteButton = new Button("삭제");

        HBox hBox = new HBox(label, textField, addButton, deleteButton);
        hBox.setSpacing(10.0d);
        hBox.setAlignment(CENTER);

        Button finalButton = new Button("추첨");

        VBox vBox = new VBox(hBox, tableView, finalButton);
        vBox.setAlignment(CENTER);

        Scene scene = new Scene(vBox, 700, 500);
        stage.setTitle("What's my lunch!");
        stage.setScene(scene);
        stage.show();

        addButton.setOnMouseClicked(event -> {
            SQLiteManager.insertRestaurant(textField.getText());
            refreshRestaurantList();
            tableView.getItems().setAll(restaurants);
            tableView.refresh();
        });
        deleteButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "삭제하시겠습니까?");
            alert.showAndWait().ifPresent(type -> {
                if (type == ButtonType.OK) {
                    if (tableView.getFocusModel().getFocusedItem() == null) {
                        return;
                    }

                    SQLiteManager.deleteRestaurant(tableView.getFocusModel().getFocusedItem().getId());
                    refreshRestaurantList();
                    tableView.getItems().setAll(restaurants);
                    tableView.refresh();
                }
            });
        });
        finalButton.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.NONE, pickRandomRestaurant().getName(), ButtonType.OK);
            alert.showAndWait();
        });
    }

    private static TableView<Restaurant> createRestaurantTableView() {
        refreshRestaurantList();
        TableView<Restaurant> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        TableColumn<Restaurant, String> columnId = new TableColumn<>("Id");
        TableColumn<Restaurant, String> columnName = new TableColumn<>("Name");

        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableView.getColumns().addAll(
            columnName
        );

        tableView.getItems().addAll(
            restaurants
        );

        return tableView;
    }

    private static void refreshRestaurantList() {
        restaurants = SQLiteManager.selectRestaurants();
    }

    private static Restaurant pickRandomRestaurant() {
        Random random = new Random();
        return restaurants.stream()
            .skip(random.nextInt(restaurants.size()))
            .limit(1)
            .collect(Collectors.toList()).get(0);
    }

    public static void main(String[] args) {
        SQLiteManager.createTableIfNotExists();
        launch();
    }
}