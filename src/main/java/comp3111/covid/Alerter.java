package comp3111.covid;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerter {
	public static void noCountrySelectedAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("No Country selected!");
		alert.setContentText("Please select at least one country!");
		alert.showAndWait();
	}
	public static void noDataAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Data is not Found!");
		alert.setContentText("Please select different date or country!");
		alert.showAndWait();
	}
	public static void notValidDateAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Date is invalid!");
		alert.setContentText("Please provide valid date!");
		alert.showAndWait();
	}
}
