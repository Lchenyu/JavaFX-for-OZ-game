import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        tab3.setContent(getVpane());

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setTitle("Ozlympic Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private VBox getVpane(){
        VBox vBox = new VBox();
        vBox.getChildren().add(AthleteInfoTab.getTable());
        return  vBox;
    }





}
