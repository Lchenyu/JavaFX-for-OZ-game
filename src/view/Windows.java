package view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import controller.Driver;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
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


    private BorderPane borderPane = new BorderPane();

    private boolean checkNum = false;
    private ArrayList<Person> addedAthletes;
    private ObservableList<Person> availableAthletes;
    private ListView gameIDview;
    private String gameID;
    private boolean flg = false;
    private Button show;

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	getStart(); // initial the driver class
    	getStart().database(); // load athletes data
    	getStart().fileReadGameHistory("result.txt");



        Scene scene = new Scene(getPane(), 780, 900);
        primaryStage.setTitle("Ozlympic Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e ->{
        	if(checkNum){
        		try {
    				start.overwriteGameId("result.txt");
    			} catch (IOException e1) {
    				e1.printStackTrace();
    			}
            	start.fileUpdateAthPoints("data.txt");
            	System.out.print("I am Closed");
        	}

        });
    }



    protected TabPane getPane(){
         /*
         set TabPane's tab
          */
         layout.getTabs().addAll(tab1, tab2, tab3);
         layout.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
         layout.getSelectionModel().select(tab1); // set default tab showing

         tab1.setContent(getBorderPane());
         tab2.setContent(getGamePane());

         tab3.setOnSelectionChanged(e ->{
        	  tab3.setContent(getAthletesAllPane());
         });
         tab2.setOnSelectionChanged(e ->{
        	setGameID();
         	tab2.setContent(getGamePane());
         });

         return layout;
    }

    private BorderPane getBorderPane(){
        /*
        contents for tab 1
         */
    	borderPane.setTop(getTopPane());
        borderPane.setLeft(getRadioPane());
        borderPane.setCenter(getAvailableList());

        return borderPane;
    }

    private VBox getAthletesAllPane(){
        /*
        contents for tab 3
         */
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(getAthleteTable());
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
        Button startBtn = new Button("Start Game");
        if(!swim || !run || !cycle  ){
        	 startBtn.setDisable(true);
        }
        startBtn.setDisable(true);
        RadioButton radio1 = new RadioButton("Swimming");
        radio1.setOnAction(e -> {
        	swim = true;
        	if(radio1.isSelected()){
        		run = false; cycle =false;
        		borderPane.setCenter(getAvailableList());
        		start.setGameType(1);
        		startBtn.setDisable(false);
        	}
        });
        RadioButton radio2 = new RadioButton("Running");
        radio2.setOnAction(e -> {
        	run = true;
        	if(radio2.isSelected()){
        		swim = false; cycle =false;
        		borderPane.setCenter(getAvailableList());
        		start.setGameType(2);
        		startBtn.setDisable(false);
        	}
        });
        RadioButton radio3 = new RadioButton("Cycling");
        radio3.setOnAction(e -> {
        	cycle = true;
        	if(radio3.isSelected()){
        		run = false; swim =false;
        		borderPane.setCenter(getAvailableList());
        		start.setGameType(3);
        		startBtn.setDisable(false);
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
        		tab1.setContent(getBorderPane());//xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx fix
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
    	 * for tab 1 back end
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
        pane.setPadding(new Insets(10,10,100,10));
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
        pane.add(gameTable,0,1, 5,1);
        return pane;
    }

    private ObservableList<Result> getCurrentGame(ArrayList<Result> temp){
    	/*
    	 * for tab 1
    	 *
    	 */

    	ObservableList<Result> gameResult = FXCollections.observableArrayList();

    	for(int i = 0; i < temp.size(); i++){
    		gameResult.add(new Result(temp.get(i).getId(), temp.get(i).getRe()));
    	}
    	return gameResult;
    }

    private void CheckAthleteNumber(){
    	/*
    	 * for tab 1
    	 *
    	 */
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

    private GridPane getGamePane(){
    	/*
    	 * for tab 2
    	 *
    	 */
    	GridPane gridPane = new GridPane();
    	Label label = new Label("Game Result History");

    	ObservableList ovList = FXCollections.observableArrayList();

    	gameIDview = new ListView();



    	for(int i = 0; i < start.getGames().size(); i++){
    		ovList.add(start.getGames().get(i).getGameID());


    	}

    	gameIDview.setItems(ovList);
    	gameIDview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    	gameIDview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    	 	setGameID();
        	tab2.setContent(getGamePane());
    	});

    	gridPane.setAlignment(Pos.CENTER);
    	gridPane.setPadding(new Insets(10, 10, 10, 10));
    	gridPane.setVgap(20);
    	gridPane.setHgap(50);


        gridPane.add(getRightResult(), 1, 0, 1, 2);
        gridPane.add(gameIDview, 0, 0);
    	return gridPane;
    }


    private void setGameID(){
    	/*
    	 * for tab 2
    	 *
    	 */
    		gameID = (String) gameIDview.getSelectionModel().getSelectedItem();
    }


    private VBox getRightResult(){
    	/*
    	 * for tab 2
    	 *
    	 */
    	VBox vbox = new VBox();

        ObservableList<Result> gameResult = FXCollections.observableArrayList();
        TableView<Result> resultTable = new TableView();

        if (gameID != null)
	        for(int i = 0; i < start.getGames().size(); i++){
	        	if (start.getGames().get(i) == null)
	        		continue;
	        	//System.out.println(i + " " + start.getGames().get(i).getGameID()));
	        	if(start.getGames().get(i).getGameID().equalsIgnoreCase(gameID)){
	        		for(int j = 0; j < start.getGames().get(i).getResults().size(); j++)
	        		gameResult.add(new Result(start.getGames().get(i).getResults().get(j).getId(), start.getGames().get(i).getResults().get(j).getRe()));
	        	}
	        }

        TableColumn<Result, String> games = new TableColumn<>("ID");
        games.setMinWidth(200);
        games.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Result, String> result = new TableColumn<>("Result");
        result.setMinWidth(200);
        result.setCellValueFactory(new PropertyValueFactory<>("re"));


        resultTable.getColumns().addAll(games, result);
        resultTable.setItems(gameResult);

    	vbox.getChildren().add(resultTable);

    	return vbox;
    }

    private ObservableList<Person> getAthletesList(){
    	/*
    	 * for tab 3
    	 *
    	 */
    	ObservableList<Person> Athletes = FXCollections.observableArrayList();

    	for(Person p : start.getSwimmers()){
    		Athletes.add(new Swimmer(p.getId(),p.getName(),p.getPoints()));
    	}
    	for(Person p : start.getRunners()){
    		Athletes.add(new Sprinter(p.getId(),p.getName(),p.getPoints()));
    	}
    	for(Person p : start.getCyclists()){
    		Athletes.add(new Cyclist(p.getId(),p.getName(),p.getPoints()));
    	}

    	for(Person p : start.getSuperAthletes()){
    		Athletes.add(new SuperAthlete(p.getId(),p.getName(),p.getPoints()));
    	}

    	return Athletes;
    }

    private TableView<Person> getAthleteTable(){
    	/*
    	 * for tab 3
    	 *
    	 */
    	TableView<Person> table = new TableView<>();
    	TableColumn<Person, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Person, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Person, Integer> pointColumn = new TableColumn<>("Points");
        pointColumn.setMinWidth(100);
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        table.setItems(getAthletesList());
        table.getColumns().addAll(idColumn, nameColumn, pointColumn);
    	 return table;
    }

    private Pane getAnimation(){
    	Pane sPane = new Pane();


		Circle circle1 = new Circle(100, 100, 30);
		Circle circle2 = new Circle(150, 100, 30);
		Circle circle3 = new Circle(200, 100, 30);
		Circle circle4 = new Circle(125, 145, 30);
		Circle circle5 = new Circle(175, 145, 30);

		circle1.setFill(null);
		circle2.setFill(null);
		circle3.setFill(null);
		circle4.setFill(null);
		circle5.setFill(null);


		circle1.setStroke(Color.BLUE);
		circle2.setStroke(Color.BLACK);
		circle3.setStroke(Color.RED);
		circle4.setStroke(Color.YELLOW);
		circle5.setStroke(Color.GREEN);

		circle1.setStrokeWidth(4);
		circle2.setStrokeWidth(4);
		circle3.setStrokeWidth(4);
		circle4.setStrokeWidth(4);
		circle5.setStrokeWidth(4);

		Timeline animation = new Timeline(new KeyFrame(Duration.millis(500), e ->{

			if(circle1.isVisible() && circle2.isVisible() && circle3.isVisible() && circle4.isVisible() && circle5.isVisible()){
				circle1.setVisible(false);
			}

			if(!circle1.isVisible() && circle2.isVisible()){
				circle1.setVisible(true);
				circle2.setVisible(false);
			}else if(!circle2.isVisible() && circle3.isVisible()){
				circle2.setVisible(true);
				circle3.setVisible(false);
			}else if(!circle3.isVisible() && circle4.isVisible()){
				circle3.setVisible(true);
				circle4.setVisible(false);
			}else if(!circle4.isVisible() && circle5.isVisible()){
				circle4.setVisible(true);
				circle5.setVisible(false);
			}else if(!circle5.isVisible() && circle1.isVisible()){
				circle5.setVisible(true);
				circle1.setVisible(false);
			}
		}));

		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play(); // Start animation

		sPane.getChildren().addAll(circle1, circle2, circle3, circle4, circle5);
		return sPane;
    }

    private HBox getTopPane(){
    	HBox hbox = new HBox(20);

    	hbox.getChildren().add(getAnimation());
    	hbox.getChildren().add(getImagePane());


    	return hbox;
    }


    private Pane getImagePane(){
    	Pane pane = new Pane();
    	File a = new File("running.gif");
    	File a1 = new File("swimming.gif");
    	File a2 = new File("cycling.gif");
    	Image m = new Image(a.toURI().toString());
    	Image m1 = new Image(a1.toURI().toString());
    	Image m2 = new Image(a2.toURI().toString());
    	ImageView runImage = new ImageView(m);
    	ImageView swimImage = new ImageView(m1);
    	ImageView cycleImage = new ImageView(m2);

    	if(run){
    		pane.getChildren().add(runImage);
    	}else if(swim){
    		pane.getChildren().add(swimImage);
    	}else if(cycle){
    		pane.getChildren().add(cycleImage);
    	}

    	return pane;
    }


}
