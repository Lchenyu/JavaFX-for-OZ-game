import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.accessibility.AccessibleHyperlink;

public class AthleteInfoTab {

    public static TableView<Athlete> getTable(){
        TableView<Athlete> table = new TableView<>();

        TableColumn<Athlete, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(100);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("athleteID"));

        TableColumn<Athlete, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Athlete, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(100);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Athlete, Integer> pointColumn = new TableColumn<>("Points");
        pointColumn.setMinWidth(100);
        pointColumn.setCellValueFactory(new PropertyValueFactory<>("point"));

        table.setItems(getAthlete());
        table.getColumns().addAll(idColumn, nameColumn, typeColumn, pointColumn);

        return table;
    }

    public static ObservableList<Athlete> getAthlete(){
        ObservableList<Athlete> Athletes = FXCollections.observableArrayList();
        Athletes.add(new Athlete("S001", "John", "swimmer"));
        Athletes.add(new Athlete("S002", "Sam", "swimmer"));
        Athletes.add(new Athlete("S003", "Hole", "swimmer"));

        return Athletes;
    }
}
