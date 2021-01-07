package com.codecool.processwatch.gui;
import com.codecool.processwatch.domain.ProcessWatchApp;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.SelectionMode;

import java.util.concurrent.atomic.AtomicReference;


import javax.security.auth.Refreshable;
import javax.swing.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * The JavaFX application Window.
 */
public class FxMain extends Application {
    private static final String TITLE = "Process Watch";

    private App app;
//    private TableView tableView;

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
        tableColumn(tableView, pidColumn, parentPidColumn, userNameColumn, processNameColumn, argsColumn, startTimeColumn, totalCpuTimeColumn);





//----------------------------------------------------------------
        var containerBox = new HBox();
        var refreshButtonBox =new HBox();
        var killButtonBox =new HBox();
        var textFieldBox =new HBox();

        final TextField textField=new TextField();

        ChoiceBox choiceBox = new ChoiceBox();
        choiceBox.setItems(FXCollections.observableArrayList(
        "pid", "name"));
        String[] choiceBoxList= new String[]{"pid","name"};

        TilePane r = new TilePane();

        final Label labelChoiceBox = new Label("Filter by");
        final Label labelKiller = new Label(" ?");
        final Label labelSubmit = new Label(" ?");
        final Label labelRefresh =new Label(" ?");

        r.getChildren().add(labelChoiceBox);
        var refreshButton = new Button("Refresh");
        final Button killer =new Button("Process kill");
        final Button submit = new Button("Submit");
        final Button about = new Button("About");

        final Tooltip tooltipKiller=new Tooltip();
        final Tooltip tooltipRefresh= new Tooltip();
        final Tooltip tooltipSubmit=new Tooltip();
        AtomicReference<String> textFieldInput= new AtomicReference<>("");
//        submit.setOnAction(e-> textFieldInput.set(textField.getText()));
        submit.setOnAction(e-> search(textField.getText(), tableView));
//        System.out.println(textFieldInput.toString());

        containerBox.setAlignment(Pos.BASELINE_LEFT);
        refreshButtonBox.setAlignment(Pos.BASELINE_CENTER);
        killButtonBox.setAlignment(Pos.BASELINE_CENTER);
        textFieldBox.setAlignment(Pos.BASELINE_CENTER);

        containerBox.setSpacing(150);
        containerBox.setPadding(new Insets(5,5,3,5));

        buttonStyles(labelKiller, labelSubmit, refreshButton, killer, submit, about);

        textField.setPrefWidth(110);
        textField.accessibleTextProperty();
        textField.setPrefColumnCount(2);

        toolTipSetting(choiceBoxList,choiceBox,textField, labelKiller, labelSubmit, labelRefresh, tooltipKiller, tooltipRefresh, tooltipSubmit);

        refreshButtonBox.getChildren().addAll(refreshButton,labelRefresh);
        killButtonBox.getChildren().addAll(killer,labelKiller);
        textFieldBox.getChildren().addAll(labelChoiceBox,choiceBox,textField,submit,labelSubmit);

        containerBox.getChildren().addAll(textFieldBox,killButtonBox,refreshButtonBox,about);
        refreshButton.setOnAction(ignoreEvent -> ProcessHandle.of(36012).ifPresent(ProcessHandle::destroy));


        refreshButton.setOnAction(ignoreEvent -> ProcessWatchApp.refresh());

        killer.setOnAction(ignoreEvent ->
        {   ArrayList < Long > arrList = new ArrayList < Long > ();
            Integer selectedItemNumber = tableView.getSelectionModel().getSelectedItems().size();
            for (int i = 0; i<selectedItemNumber; i++) {
                Long selectedItem = tableView.getSelectionModel().getSelectedItems().get(i).getPid();
                arrList.add(selectedItem);
                ProcessHandle.of(selectedItem).ifPresent(ProcessHandle::destroy);
                ;}
            ProcessWatchApp.refresh();
        });

//------------------------------------------------------------------------------------

        var box = new VBox(containerBox);
        var scene = new Scene(box, 1020, 480);

        var elements = box.getChildren();

        elements.addAll(tableView);

        primaryStage.setScene(scene);
        primaryStage.show();

        aboutPopUpWindow(about);

   }

    private void search(String text,TableView <ProcessView> tableView) {
        ArrayList <ProcessView>filteredList = new ArrayList<ProcessView>();
        ObservableList<ProcessView> allListItems = tableView.getItems();
        for (ProcessView processView: allListItems) {
            if (processView.getUserName().equals(text)) {
                filteredList.add(processView);
            }
        }


    }

    private void buttonStyles(Label labelKiller, Label labelSubmit, Button refreshButton, Button killer, Button submit, Button about) {
        refreshButton.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        labelSubmit.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        labelKiller.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        killer.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        submit.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
        about.setStyle("-fx-font: 12 arial; -fx-base: #b6e7c9;");
    }

    private void toolTipSetting(String[] choiceBoxlist,ChoiceBox choiceBox,TextField textField, Label labelKiller, Label labelSubmit, Label labelRefresh, Tooltip tooltipKiller, Tooltip tooltipRefresh, Tooltip tooltipSubmit) {
        tooltipRefresh.setText("Active processes re-requested and display");
        labelRefresh.setTooltip(tooltipRefresh);
        tooltipKiller.setText("Kill the process ");
        labelKiller.setTooltip(tooltipKiller);
        tooltipSubmit.setText("Submit search ");
        labelSubmit.setTooltip(tooltipSubmit);
        //textField.setText("");
       choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
           @Override
           public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
               int k=t1.intValue();
               System.out.println(choiceBoxlist[k]);
               if(k==0){
                   textField.setText("only numbers");
               }else{textField.setText("");}
               textField.setOnMouseClicked(e->textField.setText(""));
           }
       });

    }

    private void tableColumn(TableView<ProcessView> tableView, TableColumn<ProcessView, Long> pidColumn, TableColumn<ProcessView, Long> parentPidColumn, TableColumn<ProcessView, String> userNameColumn, TableColumn<ProcessView, String> processNameColumn, TableColumn<ProcessView, String> argsColumn, TableColumn<ProcessView, String> startTimeColumn, TableColumn<ProcessView, String> totalCpuTimeColumn) {
        tableView.getColumns().add(pidColumn);
        tableView.getColumns().add(parentPidColumn);
        tableView.getColumns().add(userNameColumn);
        tableView.getColumns().add(processNameColumn);
        tableView.getColumns().add(argsColumn);
        tableView.getColumns().add(startTimeColumn);
        tableView.getColumns().add(totalCpuTimeColumn);
    }

    private void aboutPopUpWindow(Button about) {
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("About Process Watch");
        Label label=new Label("Process Watch");
        label.setPadding(new Insets(30,30,20,30));
        label.setFont(new Font(20));

        Label label2= new Label(" A task manager is a utility that provides a view of active processes or tasks, as well as related information, and may also allow users to enter commands that will manipulate those tasks ");
        label2.setWrapText(true);
        label2.setPadding(new Insets(0,10,10,10));

        about.setOnAction(e -> popupwindow.showAndWait());

        VBox layout= new VBox(10);

        layout.getChildren().addAll(label,label2);

        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 200);

        popupwindow.setScene(scene1);
    }
}
