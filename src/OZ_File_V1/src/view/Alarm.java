package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Alarm {


	public static void display(){
		Stage windows = new Stage();

		windows.initModality(Modality.APPLICATION_MODAL);
		windows.setTitle("Warning");

		Label label = new Label();
		label.setText("Please choose 4 to 8 athletes to run a game ");

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setPadding(new Insets(5, 5, 5, 5));
		gridPane.setVgap(20);

		Button btn = new Button("Ok");
		btn.setOnAction(e -> windows.close());

		
		gridPane.add(label, 0 , 0);
		gridPane.add(btn, 1 ,1);
		
		
		Scene scene = new Scene(gridPane);
		windows.setScene(scene);
		windows.showAndWait();
	}
}
