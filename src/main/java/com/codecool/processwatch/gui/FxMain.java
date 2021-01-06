package com.codecool.processwatch.gui;
import com.codecool.processwatch.domain.ProcessDisplay;
import com.codecool.processwatch.domain.ProcessSource;
import com.codecool.processwatch.domain.ProcessWatchApp;
import com.codecool.processwatch.domain.Query;
import com.codecool.processwatch.os.OsProcessSource;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        refreshButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
//----------------------------------------------------------------
        var root = new HBox(5);
        root.setAlignment(Pos.BASELINE_LEFT);

        var rootRefres =new HBox(5);
        rootRefres.setAlignment(Pos.CENTER);

        var rootKiller =new HBox(5);
        rootRefres.setAlignment(Pos.CENTER);

        var roottextField =new HBox(5);
        roottextField.setAlignment(Pos.CENTER);
        root.setSpacing(400);

        var buttons =new HBox(5);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        buttons.setSpacing(457);

        final Label labelRefresh =new Label("?");
        final Tooltip tooltipRefresh= new Tooltip();
        tooltipRefresh.setText("Active processes re-requested and display");
        labelRefresh.setTooltip(tooltipRefresh);

        final TextField textField=new TextField();
        textField.setPrefWidth(100);

        textField.accessibleTextProperty();
        textField.setPrefColumnCount(2);
        textField.setText("Filter by pid");


        final Button submit = new Button("Submit");
        submit.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");

        final Button about = new Button("About");
        about.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");

        Label labelSubmit = new Label("?");
        labelSubmit.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        final Tooltip tooltipSubmit=new Tooltip();
        tooltipSubmit.setText("Submit search ");
        labelSubmit.setTooltip(tooltipSubmit);


        final Button killer =new Button("Process killer");
        killer.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");


        Label labelKiller = new Label("?");
        labelKiller.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        final Tooltip tooltipKiller=new Tooltip();
        tooltipKiller.setText("Kill the process ");
        labelKiller.setTooltip(tooltipKiller);

        rootRefres.getChildren().addAll(refreshButton,labelRefresh);
        rootKiller.getChildren().addAll(killer,labelKiller);
        roottextField.getChildren().addAll(textField,submit,labelSubmit);
        buttons.getChildren().addAll(rootKiller,rootRefres);

        root.getChildren().addAll(roottextField,about);





        OsProcessSource os=new OsProcessSource();

        //refreshButton.setOnAction(ignoreEvent -> os.getProcesses());
        //refreshButton.setOnAction(ignoreEvent -> ProcessWatchApp.refresh());

//        OsProcessSource os=new OsProcessSource();
//        refreshButton.setOnAction(ignoreEvent -> os.getProcesses());
//        refreshButton.setOnAction(ignoreEvent -> ProcessWatchApp.refresh());

        refreshButton.setOnAction(ignoreEvent -> ProcessHandle.of(36012).ifPresent(ProcessHandle::destroy));

//------------------------------------------------------------------------------------


        var box = new VBox(buttons,root);
        var scene = new Scene(box, 640, 480);

        var elements = box.getChildren();
        elements.addAll(tableView);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
