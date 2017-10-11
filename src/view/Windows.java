package view;

import java.util.ArrayList;

import controller.Driver;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Cyclist;
import model.Person;
import model.Result;
import model.Sprinter;
import model.SuperAthlete;
import model.Swimmer;

public class Windows extends Application{

	private boolean swim = false, run = false, cycle = false;
	private  Driver start;
	private ArrayList<Person> tempList;
    /*
     * same as Assignment 1's Ozlympic class as start game
     */
	private Driver getStart(){
    	 if (start == null) {
    		 start = new Driver();
         }
         return start;
    }


	private TabPane layout = new TabPane();
    private Tab tab1 = new Tab("Run Game");
    private Tab tab2 = new Tab("Game History");
    private Tab tab3 = new Tab("Athlete Information");
    private Tab tab4 = new Tab("Prediction");

    private BorderPane borderPane = new BorderPane();

    private boolean checkNum = false;
    private ArrayList<Person> addedAthletes;
    private ObservableList<Person> availableAthletes;

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    	getStart(); // initial the driver class
    	getStart().database(); // load athletes data

        Scene scene = new Scene(getPane(), 780, 600);
        primaryStage.setTitle("Ozlympic Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    protected TabPane getPane(){
         /*
         set TabPane's tab
          */
         layout.getTabs().addAll(tab1, tab2, tab3, tab4);
         layout.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
         layout.getSelectionModel().select(tab1); // set default tab showing

         tab1.setContent(getBorderPane());
         tab3.setContent(getAthletesAllPane());

         return layout;
    }

    private BorderPane getBorderPane(){
        /*
        contents for tab 1
         */
        borderPane.setLeft(getRadioPane());
        borderPane.setRight(getAvailableList());

        return borderPane;
    }

    private VBox getAthletesAllPane(){
        /*
        contents for tab 3
         */
        VBox vBox = new VBox();
        //vBox.getChildren().add(AthleteInfoTab.getTable());
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
        radio1.setOnAction(e -> {
        	swim = true;
        	if(radio1.isSelected()){
        		run = false; cycle =false;
        		borderPane.setRight(getAvailableList());
        		start.setGameType(1);
        	}
        });
        RadioButton radio2 = new RadioButton("Running");
        radio2.setOnAction(e -> {
        	run = true;
        	if(radio2.isSelected()){
        		swim = false; cycle =false;
        		borderPane.setRight(getAvailableList());
        		start.setGameType(2);
        	}
        });
        RadioButton radio3 = new RadioButton("Cycling");
        radio3.setOnAction(e -> {
        	cycle = true;
        	if(radio3.isSelected()){
        		run = false; swim =false;
        		borderPane.setRight(getAvailableList());
        		start.setGameType(3);
        	}
        });



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
        startBtn.setOnAction(e ->{
        	CheckAthleteNumber();
        	if(checkNum){

        		start.matchType();
        		start.createGame();
        		for(Person p : addedAthletes){
        			start.addAthletesByManual(p.getId());
        		}
        		start.gameStart();
        		borderPane.setBottom(getBottomPane());
        	}else{
        		Alarm.display();
        	}
        });

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

    private TableView<Person> getTable(){
        /*
        for tab 1 get available athletes table
         */
        TableView<Person> table = new TableView<>();
        ObservableList<TableColumn<Person, ?>> columns = table.getColumns();
        /*
         * clear columns when refresh the table
         */
        if(run || swim || cycle){
        	columns.clear();
        }

        TableColumn<Person, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

//        TableColumn<Person, String> typeColumn = new TableColumn<>("Type");
//        typeColumn.setMinWidth(100);
//        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Person, Integer> pointColumn = new TableColumn<>("Points");
        pointColumn.setMinWidth(100);
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        TableColumn<Person, CheckBox> selectColumn = new TableColumn<Person, CheckBox>("Select");
        selectColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Person, CheckBox>, ObservableValue<CheckBox>>(){
                    @Override
                    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Person, CheckBox> arg0) {
                    	Person data = arg0.getValue();
                        CheckBox checkBox = new CheckBox();
                        checkBox.selectedProperty().setValue(data.getSelected());
                        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            public void changed(ObservableValue<? extends Boolean> ov, Boolean old_val,
                                    Boolean new_val) {
                                data.setSelected(new_val);
                                checkBox.setSelected(new_val);
                                updateAvailableAthletes(data);
                            }
                        });
                        return new SimpleObjectProperty<CheckBox>(checkBox);
                    }
                });
        columns.addAll(idColumn, nameColumn, pointColumn, selectColumn);
        table.setItems(getAvailableAthletes(getAthletesOnType()));
        return table;
    }

    private ObservableList<Person> getAvailableAthletes(ArrayList<Person> temp){
    	/*
    	 * for tab 1 get available athletes table
    	 * set available Athletes which depends on the game type into ObservableList
    	 * and always put superAthletes in the ObservableList
    	 */
    	availableAthletes = FXCollections.observableArrayList();

    	if(swim){
        	for(int i = 0; i < temp.size(); i++){
        		availableAthletes.add( new Swimmer(temp.get(i).getId(),temp.get(i).getName(),
        				temp.get(i).getAge(),temp.get(i).getState()));
        	}
    	}

    	if(run){
          	for(int i = 0; i < temp.size(); i++){
        		availableAthletes.add( new Sprinter(temp.get(i).getId(),temp.get(i).getName(),
        				temp.get(i).getAge(),temp.get(i).getState()));
        	}
    	}

    	if(cycle){
          	for(int i = 0; i < temp.size(); i++){
        		availableAthletes.add( new Cyclist(temp.get(i).getId(),temp.get(i).getName(),
        				temp.get(i).getAge(),temp.get(i).getState()));
        	}
    	}

    	for(int i = 0; i < start.getSuperAthletes().size(); i++){
    		availableAthletes.add( new SuperAthlete(start.getSuperAthletes().get(i).getId(),start.getSuperAthletes().get(i).getName(),
    				start.getSuperAthletes().get(i).getAge(),start.getSuperAthletes().get(i).getState()));
    	}

    	return availableAthletes;
    }

    private ArrayList<Person> getAthletesOnType(){
    	/*
    	 * a trigger for choose available athletes to show in the table
    	 */
    	if(swim){
    		tempList = start.getSwimmers();
    	}

    	if(run){
    		tempList = start.getRunners();
    	}

    	if(cycle){
    		tempList = start.getCyclists();
    	}

    	return tempList;
    }

    private void updateAvailableAthletes(Person dataIn){
        int index = availableAthletes.indexOf(dataIn);
        availableAthletes.set(index, dataIn);
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

        TableView<Result> gameTable = new TableView<>();

        TableColumn<Result, String> games = new TableColumn<>("ID");
        games.setMinWidth(200);
        games.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Result, String> result = new TableColumn<>("Result");
        result.setMinWidth(200);
        result.setCellValueFactory(new PropertyValueFactory<>("re"));


        gameTable.getColumns().addAll(games, result);
        gameTable.setItems(getCurrentGame(start.getGames().get(start.getGames().size() -1).getResults()));




        id.setText(start.getGames().get(start.getGames().size() - 1).getGameID());
        reLabel.setText(start.getGames().get(start.getGames().size() - 1).getReferee());

        pane.add(gameIDLabel,0,0);
        pane.add(id, 1,0);
        pane.add(refereeLabel, 2, 0);
        pane.add(reLabel, 3, 0);
        pane.add(gameTable,0,1, 3,1);
        return pane;
    }


    private ObservableList<Result> getCurrentGame(ArrayList<Result> temp){

    	ObservableList<Result> gameResult = FXCollections.observableArrayList();

    	for(int i = 0; i < temp.size(); i++){
    		gameResult.add(new Result(temp.get(i).getId(), temp.get(i).getRe()));
    	}


    	return gameResult;

    }

    private void CheckAthleteNumber(){



    	addedAthletes = new ArrayList();
    	//System.out.print(addedAthletes.size()+"\n");

    	for(int i = 0; i< availableAthletes.size(); i++){
    	   	if(availableAthletes.get(i).getSelected()){
        		addedAthletes.add(availableAthletes.get(i));
        		//System.out.print(addedAthletes.size()+ "---------");
        	}
    	}

    	if(addedAthletes.size() >= 4 && addedAthletes.size() <= 8){
    		checkNum = true;
    	}else{
    		checkNum = false;
    	}
    }









}
