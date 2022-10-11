package comp3111.covid;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

public class FxAlert {

	 public static void show(String title) {
		 Alert _alert = new Alert(Alert.AlertType.ERROR,title,new ButtonType("close", ButtonBar.ButtonData.NO));
		 _alert.show(); 
	 }
}
