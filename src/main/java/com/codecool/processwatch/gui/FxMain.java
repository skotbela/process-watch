package com.codecool.processwatch.gui;
import com.codecool.processwatch.domain.ProcessDisplay;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.ProcessWatchApp;
import com.codecool.processwatch.domain.Query;
import com.codecool.processwatch.os.OsProcessSource;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;

import javax.security.auth.Refreshable;

import java.awt.*;

import static javafx.collections.FXCollections.observableArrayList;
import com.codecool.processwatch.domain.ProcessWatchApp;

/**
 * The JavaFX application Window.
 */
public class FxMain extends Application {
    private static final String TITLE = "Process Watch";

    private App app;

    /**
     * Entrypoint for the javafx:run maven task.
     *
     * @param args an array of the command line parameters.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Build the application window and set up event handling.
     *
     * @param primaryStage a stage created by the JavaFX runtime.
     */
    public void start(Stage primaryStage) {
        primaryStage.setTitle(TITLE);

        ObservableList<ProcessView> displayList = observableArrayList();
        app = new App(displayList);
        // TODO: Factor out the repetitive code
        var tableView = new TableView<ProcessView>(displayList);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        tableView.getSelectionModel().setCellSelectionEnabled(true);
//        tableView.getSelectionModel().selectAll();
        var pidColumn = new TableColumn<ProcessView, Long>("Process ID");
        pidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("pid"));
        var parentPidColumn = new TableColumn<ProcessView, Long>("Parent Process ID");
        parentPidColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, Long>("parentPid"));
        var userNameColumn = new TableColumn<ProcessView, String>("Owner");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("userName"));
        var processNameColumn = new TableColumn<ProcessView, String>("Name");
        processNameColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("processName"));
        var argsColumn = new TableColumn<ProcessView, String>("Arguments");
        argsColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("args"));
        var startTimeColumn = new TableColumn<ProcessView, String>("Start Time");
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("startTime"));
        var totalCpuTimeColumn = new TableColumn<ProcessView, String>("Total CPU Time");
        totalCpuTimeColumn.setCellValueFactory(new PropertyValueFactory<ProcessView, String>("totalCpuTime"));
        tableView.getColumns().add(pidColumn);
        tableView.getColumns().add(parentPidColumn);
        tableView.getColumns().add(userNameColumn);
        tableView.getColumns().add(processNameColumn);
        tableView.getColumns().add(argsColumn);
        tableView.getColumns().add(startTimeColumn);
        tableView.getColumns().add(totalCpuTimeColumn);



        var refreshButton = new Button("Refresh");
//----------------------------------------------------------------
//        GridPane grid = new GridPane();
//        grid.setPadding(new Insets(10,
//                10,
//                10,
//                10));
//        grid.setVgap(10);
//        grid.setHgap(10);
//        GridPane.setConstraints(refreshButton,0,0);
//        TextField textField=new TextField();
//        //textField.getMaxWidth();
//        textField.getText(0,10);
//        //textField.accessibleTextProperty();
//        //textField.setPrefColumnCount(10);
//        textField.setText("Filter by User");
//        GridPane.setConstraints(textField, 0, 0);
//        //grid.getChildren().add(textField);
//
//        Button submit = new Button("Submit");
//        GridPane.setConstraints(submit, 1, 0);
//        //grid.getChildren().add(submit);
//
//        Button submit1 = new Button("Submit1");
//        GridPane.setConstraints(submit1, 2, 0);
//
//        grid.getChildren().addAll(textField,submit,submit1);

        OsProcessSource os=new OsProcessSource();

        //refreshButton.setOnAction(ignoreEvent -> os.getProcesses());
        //refreshButton.setOnAction(ignoreEvent -> ProcessWatchApp.refresh());

//        OsProcessSource os=new OsProcessSource();
//        refreshButton.setOnAction(ignoreEvent -> os.getProcesses());
//        refreshButton.setOnAction(ignoreEvent -> ProcessWatchApp.refresh());

        refreshButton.setOnAction(ignoreEvent -> ProcessHandle.of(36012).ifPresent(ProcessHandle::destroy));
//------------------------------------------------------------------------------------


        var box = new VBox();
        var scene = new Scene(box, 640, 480);
        var elements = box.getChildren();
//        elements.addAll(textField,submit,submit1,
//                        tableView);
        elements.addAll(refreshButton,
                tableView);







        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
