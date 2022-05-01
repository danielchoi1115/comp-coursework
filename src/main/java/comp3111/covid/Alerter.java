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
		alert.setHeaderText("Data not Found!");
		alert.setContentText("Please select different date or country!");
		alert.showAndWait();
	}
	public static void dateNotInOrderAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Date(s) invalid!");
		alert.setContentText("Date From cannot be after Date To");
		alert.showAndWait();
	}
	public static void dateNotInRangeAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Date(s) invalid!");
		alert.setContentText("Please select date between Jan 1, 2020 and Jul 20, 2021.");
		alert.showAndWait();
	}
}
