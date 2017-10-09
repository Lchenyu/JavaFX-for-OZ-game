import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Windows extends Application{

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        TabPane layout = new TabPane();
        Tab tab1 = new Tab("Run Game");
        Tab tab2 = new Tab("Game History");
        Tab tab3 = new Tab("Athlete Information");
        Tab tab4 = new Tab("Prediction");
        /*
        set TabPane's tab
         */
        layout.getTabs().addAll(tab1, tab2, tab3, tab4);
        layout.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        layout.getSelectionModel().select(tab1); // set default tab showing

        tab1.setContent(getBorderPane());
        tab3.setContent(getVpane());

        Scene scene = new Scene(layout, 780, 600);
        primaryStage.setTitle("Ozlympic Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane getBorderPane(){
        /*
        contents for tab 1
         */
        BorderPane borderPane = new BorderPane();

        borderPane.setLeft(getRadioPane());
        borderPane.setRight(getAvailableList());
        borderPane.setBottom(getBottomPane());

        return borderPane;
    }

    private VBox getVpane(){
        /*
        contents for tab 3
         */
        VBox vBox = new VBox();
        vBox.getChildren().add(AthleteInfoTab.getTable());
        return  vBox;
    }



    private GridPane getRadioPane(){
        /*
        for tab 1
        radio button for select game type
         */
        VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label("Game Type: ");
        RadioButton radio1 = new RadioButton("Swimming");
        RadioButton radio2 = new RadioButton("Running");
        RadioButton radio3 = new RadioButton("Cycling");

        ToggleGroup group = new ToggleGroup();
        radio1.setToggleGroup(group);
        radio2.setToggleGroup(group);
        radio3.setToggleGroup(group);

        vBox.getChildren().addAll(label, radio1, radio2, radio3);


        GridPane gridPane = new GridPane();
       // gridPane.setAlignment(Pos.CENTER_LEFT);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Button startBtn = new Button("Start Game");

        gridPane.add(vBox,0 , 0);
        gridPane.add(startBtn, 0, 1);

        gridPane.setPadding(new Insets(10,10,10,50));

        return gridPane;
    }

    private VBox getAvailableList(){
        /*
        for tab 1
        show the available athletes list by different game type
         */

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 30, 10, 10));
        vBox.getChildren().add(getTable());

        return vBox;
    }


    private TableView<Athlete> getTable(){
        /*
        for tab 1 get available athletes table
         */
        TableView<Athlete> table = new TableView<>();
        ObservableList<TableColumn<Athlete, ?>> columns = table.getColumns();

        TableColumn<Athlete, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("athleteID"));

        TableColumn<Athlete, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Athlete, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(100);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Athlete, Integer> pointColumn = new TableColumn<>("Points");
        pointColumn.setMinWidth(100);
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("point"));


        TableColumn<Athlete, CheckBox> selectColumn = new TableColumn<Athlete, CheckBox>("Select");
        selectColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Athlete, CheckBox>, ObservableValue<CheckBox>>(){
                    @Override
                    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Athlete, CheckBox> arg0) {
                        Athlete data = arg0.getValue();
                        CheckBox checkBox = new CheckBox();
                        checkBox.selectedProperty().setValue(data.isSelected());

                        return new SimpleObjectProperty<CheckBox>(checkBox);
                    }
                });

        columns.addAll(idColumn, nameColumn, typeColumn, pointColumn, selectColumn);

        table.setItems(AthleteInfoTab.getAthlete());


        return table;
    }



    private GridPane getBottomPane(){
        /*
        for tab 1, at bottom of pane and show the current game results
         */
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10,10,10,10));
        pane.setVgap(10);
        pane.setHgap(10);

        Label gameIDLabel = new Label("Game ID: ");
        Label id = new Label();
        Label refereeLabel = new Label("Referee: ");
        Label reLabel = new Label();

        TableView<Game> gameTable = new TableView<>();

        ObservableList<Game> items = FXCollections.observableArrayList();
        items.add(new Game("SW001","ref1"));
        items.add(new Game("SW002","ref2"));
        items.add(new Game("SW003","ref3"));

        TableColumn<Game, String> games = new TableColumn<>("ID");
        games.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Game, String> referee = new TableColumn<>("Referee");
        referee.setCellValueFactory(new PropertyValueFactory<>("referee"));

        gameTable.setItems(items);
        gameTable.getColumns().addAll(games, referee);

        pane.add(gameIDLabel,0,0);
        pane.add(id, 1,0);
        pane.add(refereeLabel, 2, 0);
        pane.add(reLabel, 3, 0);
        pane.add(gameTable,0,1, 3,1);
        return pane;
    }


}
