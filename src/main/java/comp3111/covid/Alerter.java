package comp3111.covid;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
 * 
 * This is an alert class that provides alert messages.
 * 
 */
public class Alerter {
	/**
     * This method is used show an alert message when no country is selected. 
     */
	public static void noCountrySelectedAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("No Country selected!");
		alert.setContentText("Please select at least one country!");
		alert.showAndWait();
		
	}
	/**
     * This method is used show an alert message when no data is available. 
     */
	public static void noDataAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Data not Found!");
		alert.setContentText("Please select different date or country!");
		alert.showAndWait();
	}
	/**
     * This method is used show an alert message when two dates are not in correct order. 
     */
	public static void dateNotInOrderAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Date(s) invalid!");
		alert.setContentText("Date From cannot be after Date To");
		alert.showAndWait();
	}
	/**
     * This method is used show an alert message when the date is not in valid range. 
     */
	public static void dateNotInRangeAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Date(s) invalid!");
		alert.setContentText("Please select date between Jan 1, 2020 and Jul 20, 2021.");
		alert.showAndWait();
	}
}
