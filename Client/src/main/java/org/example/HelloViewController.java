//package org.example;
//
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import org.example.models.Movie;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.ResourceBundle;
//
//public class HelloViewController {
//    private Movie movieToSend;
//    private final Client client;
//    private String username;
//    private String scriptFilePath; // Переменная для хранения пути к выбранному файлу
//    private ResourceBundle resourceBundle;
//    @FXML
//    private Label helloLabel;
//    @FXML
//    private Button updateButton;
//    @FXML
//    private Button addButton;
//    @FXML
//    private Button infoButton;
//    @FXML
//    private Button clearButton;
//    @FXML
//    private Button removeByIdButton;
//    @FXML
//    private Button removeFirstButton;
//    @FXML
//    private Button sumOfBudgetButton;
//    @FXML
//    private Button minByNameButton;
//    @FXML
//    private Button addIfMaxButton;
//    @FXML
//    private Button removeGreaterButton;
//    @FXML
//    private Button TableButton;
//    @FXML
//    private Button TableButton1;
//    @FXML
//    private Button logoutButton;
//    @FXML
//    private Button executeScriptButton; // Новая кнопка
//    @FXML
//    private TextField updateIdField;
//    @FXML
//    private TextField removeByIdField;
//
//    public HelloViewController(Client client) {
//        this.client = client;
//        resourceBundle = client.getResourceBundle();
//    }
//
//    @FXML
//    public void initialize() {
//        updateButton.setOnAction(event -> switchToNewMovieScene("update", updateIdField.getText()));
//        addButton.setOnAction(event -> switchToNewMovieScene("add", ""));
//        addIfMaxButton.setOnAction(event -> switchToNewMovieScene("add_if_max", ""));
//        removeGreaterButton.setOnAction(event -> switchToNewMovieScene("remove_greater", ""));
//
//        infoButton.setOnAction(event -> showAlert("Info", client.sendCommand("info", "").getMessage()));
//        clearButton.setOnAction(event -> showAlert("Clear", client.sendCommand("clear", "").getMessage()));
//        removeByIdButton.setOnAction(event -> showAlert("Remove by ID", client.sendCommand("remove_by_id " + removeByIdField.getText(), "").getMessage()));
//        removeFirstButton.setOnAction(event -> showAlert("Remove First", client.sendCommand("remove_first", "").getMessage()));
//        sumOfBudgetButton.setOnAction(event -> showAlert("Sum of Budget", client.sendCommand("sum_of_budget", "").getMessage()));
//        minByNameButton.setOnAction(event -> showAlert("Min by Name", client.sendCommand("min_by_name", "").getMessage()));
//        executeScriptButton.setOnAction(this::handleExecuteScript); // Обработчик для новой кнопки
//
//        // Добавляем обработчик события закрытия окна
//        Platform.runLater(() -> {
//            Stage stage = (Stage) helloLabel.getScene().getWindow();
//            stage.setOnCloseRequest(event -> {
//                client.sendCommand("save", "");
//            });
//        });
//    }
//
//    @FXML
//    private void handleLogout(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"), client.getResourceBundle());
//            loader.setControllerFactory(param -> new AuthorizationController(client));
//            Scene scene = new Scene(loader.load());
//            Stage stage = (Stage) logoutButton.getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void switchToNewMovieScene(String commandName, String args) {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new-movie.fxml"), client.getResourceBundle());
//            fxmlLoader.setControllerFactory(param -> new NewMovieController(client, commandName, args));
//            Scene scene = new Scene(fxmlLoader.load());
//            NewMovieController controller = fxmlLoader.getController();
//            controller.setHelloViewController(this);
//
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.setTitle("New Movie");
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void switchToTable(ActionEvent event) throws IOException {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("table.fxml"), client.getResourceBundle());
//            fxmlLoader.setControllerFactory(param -> new TableController(client));
//            Scene scene = new Scene(fxmlLoader.load());
//            Stage stage = (Stage) TableButton.getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void switchToVisualize(ActionEvent event) throws IOException {
//        try {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("visual.fxml"), client.getResourceBundle());
//            fxmlLoader.setControllerFactory(param -> new VisualizeController(client));
//            Scene scene = new Scene(fxmlLoader.load());
//            Stage stage = (Stage) TableButton1.getScene().getWindow();
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleExecuteScript(ActionEvent event) {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Open Script File");
//        File file = fileChooser.showOpenDialog((Stage) executeScriptButton.getScene().getWindow());
//        if (file != null) {
//            script(file.getAbsolutePath());
//        }
//    }
//    private void script(String path){
//        HashSet<String> scriptHistory = new HashSet<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                if (scriptHistory.contains(line)){
//                    showAlert("result", "Рекурсия!");
//                    break;
//                }
//                scriptHistory.add(line);
//                showAlert("result", client.sendCommand(line, "").getMessage());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//        helloLabel.setText((resourceBundle.getString("hello_view")) + " "+ username + "!");
//    }
//}


package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.models.Movie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class HelloViewController {
    private Movie movieToSend;
    private final Client client;
    private String username;
    private String scriptFilePath; // Переменная для хранения пути к выбранному файлу
    private ResourceBundle resourceBundle;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label helloLabel;
    @FXML
    private Label infoDescriptionLabel;
    @FXML
    private Label addElementDescriptionLabel;
    @FXML
    private Label updateIdElementDescriptionLabel;
    @FXML
    private Label removeByIdDescriptionLabel;
    @FXML
    private Label clearDescriptionLabel;
    @FXML
    private Label removeFirstDescriptionLabel;
    @FXML
    private Label addIfMaxElementDescriptionLabel;
    @FXML
    private Label removeGreaterElementDescriptionLabel;
    @FXML
    private Label sumOfBudgetDescriptionLabel;
    @FXML
    private Label minByNameDescriptionLabel;
    @FXML
    private Label standardCommandsLabel;
    @FXML
    private Label removalCommandsLabel;
    @FXML
    private Label addFilterCommandsLabel;
    @FXML
    private Label visualizeDescriptionLabel;
    @FXML
    private Label filteredTableDescriptionLabel;
    @FXML
    private Label executeScriptDescriptionLabel;

    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;
    @FXML
    private Button infoButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button removeByIdButton;
    @FXML
    private Button removeFirstButton;
    @FXML
    private Button sumOfBudgetButton;
    @FXML
    private Button minByNameButton;
    @FXML
    private Button addIfMaxButton;
    @FXML
    private Button removeGreaterButton;
    @FXML
    private Button TableButton;
    @FXML
    private Button TableButton1;
    @FXML
    private Button logoutButton;
    @FXML
    private Button executeScriptButton; // Новая кнопка
    @FXML
    private TextField updateIdField;
    @FXML
    private TextField removeByIdField;

    public HelloViewController(Client client) {
        this.client = client;
        resourceBundle = client.getResourceBundle();
    }

    @FXML
    public void initialize() {
        languageComboBox.getItems().addAll("Русский", "Türkçe", "Lietuvių", "Español (México)");
        languageComboBox.setValue("Русский");

        languageComboBox.setOnAction(this::handleLanguageChange);

        updateButton.setOnAction(event -> switchToNewMovieScene("update", updateIdField.getText()));
        addButton.setOnAction(event -> switchToNewMovieScene("add", ""));
        addIfMaxButton.setOnAction(event -> switchToNewMovieScene("add_if_max", ""));
        removeGreaterButton.setOnAction(event -> switchToNewMovieScene("remove_greater", ""));

        infoButton.setOnAction(event -> showAlert("Info", client.sendCommand("info", "").getMessage()));
        clearButton.setOnAction(event -> showAlert("Clear", client.sendCommand("clear", "").getMessage()));
        removeByIdButton.setOnAction(event -> showAlert("Remove by ID", client.sendCommand("remove_by_id " + removeByIdField.getText(), "").getMessage()));
        removeFirstButton.setOnAction(event -> showAlert("Remove First", client.sendCommand("remove_first", "").getMessage()));
        sumOfBudgetButton.setOnAction(event -> showAlert("Sum of Budget", client.sendCommand("sum_of_budget", "").getMessage()));
        minByNameButton.setOnAction(event -> showAlert("Min by Name", client.sendCommand("min_by_name", "").getMessage()));
        executeScriptButton.setOnAction(this::handleExecuteScript); // Обработчик для новой кнопки

        // Добавляем обработчик события закрытия окна
        Platform.runLater(() -> {
            Stage stage = (Stage) helloLabel.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                client.sendCommand("save", "");
            });
        });
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"), client.getResourceBundle());
            loader.setControllerFactory(param -> new AuthorizationController(client));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToNewMovieScene(String commandName, String args) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new-movie.fxml"), client.getResourceBundle());
            fxmlLoader.setControllerFactory(param -> new NewMovieController(client, commandName, args));
            Scene scene = new Scene(fxmlLoader.load());
            NewMovieController controller = fxmlLoader.getController();
            controller.setHelloViewController(this);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("New Movie");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToTable(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("table.fxml"), client.getResourceBundle());
            fxmlLoader.setControllerFactory(param -> new TableController(client));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) TableButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToVisualize(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("visual.fxml"), client.getResourceBundle());
            fxmlLoader.setControllerFactory(param -> new VisualizeController(client));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) TableButton1.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleExecuteScript(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Script File");
        File file = fileChooser.showOpenDialog((Stage) executeScriptButton.getScene().getWindow());
        if (file != null) {
            script(file.getAbsolutePath());
        }
    }
    private void script(String path){
        HashSet<String> scriptHistory = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (scriptHistory.contains(line)){
                    showAlert("result", "Рекурсия!");
                    break;
                }
                scriptHistory.add(line);
                showAlert("result", client.sendCommand(line, "").getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUsername(String username) {
        this.username = username;
        helloLabel.setText((resourceBundle.getString("hello_view")) + " "+ username + "!");
    }

    @FXML
    private void handleLanguageChange(ActionEvent event) {
        String selectedLanguage = languageComboBox.getValue();
        Locale locale;
        switch (selectedLanguage) {
            case "Русский":
                locale = new Locale("ru", "RU");
                break;
            case "Türkçe":
                locale = new Locale("tr", "TR");
                break;
            case "Lietuvių":
                locale = new Locale("lt", "LT");
                break;
            case "Español (México)":
                locale = new Locale("es", "MX");
                break;
            default:
                locale = Locale.getDefault();
        }
        client.setResourceBundle(ResourceBundle.getBundle("org.example.messages", locale));
        updateTexts();
    }

    private void updateTexts() {
        resourceBundle = client.getResourceBundle();
        helloLabel.setText(resourceBundle.getString("hello_view") + " " + username + "!");
        infoDescriptionLabel.setText(resourceBundle.getString("info_description"));
        addElementDescriptionLabel.setText(resourceBundle.getString("add_element_description"));
        updateIdElementDescriptionLabel.setText(resourceBundle.getString("update_id_element_description"));
        removeByIdDescriptionLabel.setText(resourceBundle.getString("remove_by_id_description"));
        clearDescriptionLabel.setText(resourceBundle.getString("clear_description"));
        removeFirstDescriptionLabel.setText(resourceBundle.getString("remove_first_description"));
        addIfMaxElementDescriptionLabel.setText(resourceBundle.getString("add_if_max_element_description"));
        removeGreaterElementDescriptionLabel.setText(resourceBundle.getString("remove_greater_element_description"));
        sumOfBudgetDescriptionLabel.setText(resourceBundle.getString("sum_of_budget_description"));
        minByNameDescriptionLabel.setText(resourceBundle.getString("min_by_name_description"));
        standardCommandsLabel.setText(resourceBundle.getString("standard_commands"));
        removalCommandsLabel.setText(resourceBundle.getString("removal_commands"));
        addFilterCommandsLabel.setText(resourceBundle.getString("add_filter_commands"));
        visualizeDescriptionLabel.setText(resourceBundle.getString("visualize_description"));
        filteredTableDescriptionLabel.setText(resourceBundle.getString("filtered_table_description"));
        executeScriptDescriptionLabel.setText(resourceBundle.getString("execute_script_description"));
    }

}
