package comp3111.covid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * Building on the sample skeleton for 'ui.fxml' Controller Class generated by SceneBuilder 
 */
public class Controller {

    @FXML
    private Tab tabTaskZero;

    @FXML
    private TextField textfieldISO;

    @FXML
    private Button buttonConfirmedDeaths;

    @FXML
    private TextField textfieldDataset;

    @FXML
    private Button buttonRateOfVaccination;

    @FXML
    private Button buttonConfirmedCases;

    @FXML
    private Tab tabReport1;

    @FXML
    private Tab tabReport2;

    @FXML
    private Tab tabReport3;

    @FXML
    private Tab tabApp1;

    @FXML
    private Tab tabApp2;

    @FXML
    private Tab tabApp3;

    @FXML
    private TextArea textAreaConsole;

    @FXML 
    private ListView<CheckBox> countryCheckBoxList;
    
    private ObservableList<CheckBox> countryCheckbox = FXCollections.observableArrayList();
    
    @FXML
    private Button displaySelectedCountries;
    
    @FXML
    private DatePicker datePicker;
    
    @FXML
    private TableView<ConfirmedCase>  TableA;
    
    @FXML
    private TableColumn<ConfirmedCase, String> countryName;
    @FXML
    private TableColumn<ConfirmedCase, String> totalCases;
    @FXML
    private TableColumn<ConfirmedCase, String> totalCasesPerM;
    
    @FXML
    protected void initialize() {	
    	addCountryCheckbox();
    	setMinMaxOfDatePicker();
    	countryCheckBoxList.setItems(countryCheckbox);
    }
    
    /**
     *  Task Zero
     *  To be triggered by the "Confirmed Cases" button on the Task Zero Tab 
     *  
     */
    @FXML
    void doConfirmedCases(ActionEvent event) {
    	String iDataset = textfieldDataset.getText();
    	String iISO = textfieldISO.getText();
    	String oReport = DataAnalysis.getConfirmedCases(iDataset, iISO);
    	textAreaConsole.setText(oReport);
    }

  
    /**
     *  Task Zero
     *  To be triggered by the "Confirmed Deaths" button on the Task Zero Tab
     *  
     */
    @FXML
    void doConfirmedDeaths(ActionEvent event) {
    	String iDataset = textfieldDataset.getText();
    	String iISO = textfieldISO.getText();
    	String oReport = DataAnalysis.getConfirmedDeaths(iDataset, iISO);
    	textAreaConsole.setText(oReport);
    }

  
    /**
     *  Task Zero
     *  To be triggered by the "Rate of Vaccination" button on the Task Zero Tab
     *  
     */
    @FXML
    void doRateOfVaccination(ActionEvent event) {
    	String iDataset = textfieldDataset.getText();
    	String iISO = textfieldISO.getText();
    	String oReport = DataAnalysis.getRateOfVaccination(iDataset, iISO);
    	textAreaConsole.setText(oReport);
    }  
    
	@FXML 
    void doDisplayCountries(ActionEvent event) {
    	ArrayList<String> selectedCountries = new ArrayList<>();
    	for (CheckBox country: countryCheckBoxList.getItems()) {
    		if (country.isSelected()) {
    			selectedCountries.add(country.getText());
    		}
    	}
    	textAreaConsole.setText(String.join(", ", selectedCountries));

    	ObservableList<ConfirmedCase> confirmedCaseList = DataAnalysis.getConfirmedCases(selectedCountries, datePicker.getValue());

    	if (confirmedCaseList.size() == 0) {
    		noDataAlert();
    	}
    	else {
	    	countryName.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("countryName"));
	    	totalCases.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("totalCases"));
	    	totalCasesPerM.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("totalCasesPerM"));
	    	
	    	TableA.setItems(confirmedCaseList);
    	}
    	
    }
    
	void noDataAlert() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText("Data is not Found!");
		alert.setContentText("Please select different date or country!");
		alert.showAndWait();
	}
	// Add countries that can be checked for user input
    void addCountryCheckbox() {
    	ArrayList<String> countries = DataAnalysis.getCountries();
    	for (String country: countries) {
    		CheckBox checkbox = new CheckBox(country);
    		countryCheckbox.add(checkbox);
    	}
    	
    }
 
    // Disable Dates that are out of range
    void setMinMaxOfDatePicker(){
        
    	ArrayList<LocalDate> dates = DataAnalysis.getDates();
    	LocalDate minDate = Collections.min(dates);
    	LocalDate maxDate = Collections.max(dates);
//    	System.out.println(minDate.format(formatter) + " " + maxDate.format(formatter));
//    	System.out.println(minDate);
//    	System.out.println(maxDate);
//    	textAreaConsole.setText(minDate.format(formatter) + " " + maxDate.format(formatter));
    	
    	datePicker.setDayCellFactory(d ->
    	           new DateCell() {
    	               @Override 
    	               public void updateItem(LocalDate item, boolean empty) {
    	                   super.updateItem(item, empty);
    	                   setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
    	               }});
    	datePicker.setValue(maxDate);
    }
    

}

