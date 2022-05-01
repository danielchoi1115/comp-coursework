package comp3111.covid;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

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

//    Table A
    private LinkedHashMap<String, ArrayList<LocalDate>> countryDateMap;
    private ObservableList<CheckBox> tableA_countryCheckbox = FXCollections.observableArrayList();
    private ArrayList<LocalDate> dateLists;
    @FXML 
    private ListView<CheckBox> tableA_countryCheckBoxList;
    @FXML
    private CheckBox tableA_selectAllCheckBox;
    @FXML
    private Button displayTableA;
    @FXML
    private Button clearTableA;
    @FXML
    private DatePicker tableA_datePicker;
    @FXML
    private TableView<ConfirmedCase>  TableA;
    @FXML
    private TableColumn<ConfirmedCase, String> countryName;
    @FXML
    private TableColumn<ConfirmedCase, String> totalCases;
    @FXML
    private TableColumn<ConfirmedCase, String> totalCasesPerM;
    
//    Chart A
    @FXML
    private LineChart<String, Number> chartA_lineChart; 
    private ObservableList<CheckBox> chartA_countryCheckbox = FXCollections.observableArrayList();
    @FXML
    private DatePicker chartA_datePickerFrom;
    @FXML
    private DatePicker chartA_datePickerTo;
    @FXML 
    private ListView<CheckBox> chartA_countryCheckBoxList;
    @FXML
    private CheckBox chartA_selectAllCheckBox;
    @FXML
    private Button displayChartA;
    @FXML
    private Button clearChartA;
    @FXML
    private CategoryAxis chartA_xAxis ;
    @FXML
    private NumberAxis chartA_yAxis ;

//  WOW Factor world map
    @FXML 
    private Slider worldmapA_dateSlider;
    @FXML
    private Polygon worldmap_north_america = new Polygon();
    @FXML
    private Polygon worldmap_south_america = new Polygon();
    @FXML
    private Polygon worldmap_africa = new Polygon();
    @FXML
    private Polygon worldmap_europe = new Polygon();
    @FXML
    private Polygon worldmap_asia = new Polygon();
    @FXML
    private Polygon worldmap_oceania = new Polygon();
    private LinkedHashMap<LocalDate, LinkedHashMap<String, Double>> worldmapA_dateContinentMap;
    private LinkedHashMap<String, Polygon> worldmapA_polygonMap;
    
    @FXML
    protected void initialize() {	
    	dateLists = DataAnalysis.getDates();
    	countryDateMap = DataAnalysis.getCountryDateMap();
    	addCountryCheckbox();
    	setMinMaxOfDatePicker();
    	tableA_disableUnavailableCountry();
    	tableA_countryCheckBoxList.setItems(tableA_countryCheckbox);
    	
    	chartA_disableUnavailableCountry();
    	chartA_countryCheckBoxList.setItems(chartA_countryCheckbox);
    	chartA_xAxis.setAnimated(false);
    	chartA_lineChart.setCreateSymbols(false);
    	
    	worldmapA_setSlider();
    	worldmapA_dateContinentMap = DataAnalysis.getDateContinentMap();
    	worldmapA_setPolygonMap();
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
    void doDisplayTableA(ActionEvent event) {
		TableA.setItems(null);
    	ArrayList<String> selectedCountries = new ArrayList<>();
    	for (CheckBox checkbox: tableA_countryCheckBoxList.getItems()) {
    		if (checkbox.isSelected()) {
    			selectedCountries.add(checkbox.getText());
    		}
    	}
    	LocalDate dateSelected = tableA_datePicker.getValue();
    	
    	if (selectedCountries.size() == 0) {
    		Alerter.noCountrySelectedAlert();
    		return;
    	}
    	else if (!validateDate(dateSelected)) {
    		Alerter.notValidDateAlert();
    		return;
    	}

    	ObservableList<ConfirmedCase> confirmedCaseList = DataAnalysis.getConfirmedCases(selectedCountries, dateSelected);

    	if (confirmedCaseList.size() == 0) {
    		Alerter.noDataAlert();
    		return;
    	}
    	
    	countryName.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("countryName"));
    	totalCases.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("totalCases"));
    	totalCasesPerM.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("totalCasesPerM"));
    	TableA.setItems(confirmedCaseList);
    }
    
	@FXML 
    void doDisplayChartA(ActionEvent event) {
		
		chartA_lineChart.getData().clear();
		
		ArrayList<String> selectedCountries = new ArrayList<>();
    	for (CheckBox checkbox: chartA_countryCheckBoxList.getItems()) {
    		if (checkbox.isSelected()) {
    			selectedCountries.add(checkbox.getText());
    		}
    	}
    	
    	if (selectedCountries.size() == 0) {
    		Alerter.noCountrySelectedAlert();
    		return;
    	}
    	
    	LocalDate dateFrom = chartA_datePickerFrom.getValue();
    	LocalDate dateTo = chartA_datePickerTo.getValue();
    	
    	if (!validateDate(dateFrom, dateTo)) {
    		return;
    	}
    	
		LinkedHashMap<String, ArrayList<LinkedHashMap<LocalDate, Double>>> cumulativeMap = DataAnalysis.getCumulativeMap(selectedCountries, dateFrom, dateTo);
		
		if (cumulativeMap.size() == 0) {
			Alerter.noDataAlert();
		}
		
        chartA_lineChart.setTitle("Cumulative Confirmed COVID-19 Cases (per 1M)");
		ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
		
		cumulativeMap.forEach((countryName, dateMapList) -> {
			XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
			series.setName(countryName);
			dateMapList.forEach(dateMap -> {
				String date = dateMap.keySet().toArray()[0].toString();
				Number record = (Number) dateMap.values().toArray()[0];
				series.getData().add(new XYChart.Data<String, Number>(date, record)) ;
			});
			seriesList.add(series);
		});
	
		chartA_lineChart.setData(seriesList);
		
	}
	
	@FXML
	void clearTableA() {
		TableA.setItems(null);
		for (CheckBox checkbox: tableA_countryCheckBoxList.getItems()) {
			checkbox.setSelected(false);
    	}
		LocalDate maxDate = Collections.max(dateLists);
		tableA_datePicker.setValue(maxDate);
		tableA_selectAllCheckBox.setSelected(false);
	}
	@FXML
	void clearChartA() {
		chartA_lineChart.getData().clear();
		for (CheckBox checkbox: chartA_countryCheckBoxList.getItems()) {
			checkbox.setSelected(false);
    	}
		LocalDate maxDate = Collections.max(dateLists);
		chartA_datePickerFrom.setValue(maxDate);
		chartA_datePickerFrom.setValue(maxDate);
		chartA_selectAllCheckBox.setSelected(false);
	}
	
	
	@FXML
	void tableA_selectAll(){
		boolean selected = false;
		if (tableA_selectAllCheckBox.isSelected()){
			selected = true;
		}
		for (CheckBox checkbox: tableA_countryCheckBoxList.getItems()) {
			if (!checkbox.isDisabled()) {
				checkbox.setSelected(selected);
			}
    	}
	}
	@FXML
	void chartA_selectAll(){
		boolean selected = false;
		if (chartA_selectAllCheckBox.isSelected()){
			selected = true;
		}
		for (CheckBox checkbox: chartA_countryCheckBoxList.getItems()) {
			if (!checkbox.isDisabled()) {
				checkbox.setSelected(selected);
			}
    	}
	}
	boolean validateDate(LocalDate dateSelected) {
			
			LocalDate minDate = Collections.min(dateLists);
	    	LocalDate maxDate = Collections.max(dateLists);
	    	
	    	try {
	    		if (DataAnalysis.isBetween(dateSelected, minDate, maxDate)) {
	    			return true;
	    		}
	    		return false;
	    	} catch(Exception e) {
	    		textAreaConsole.setText(e.toString());
	    		return false;
	    	}
		}
	
	boolean validateDate(LocalDate dateFrom, LocalDate dateTo) {
    	if (validateDate(dateFrom) && validateDate(dateTo) && (dateFrom.isBefore(dateTo) || dateFrom.isEqual(dateTo))) {
    		return true;
    	}	
    	Alerter.notValidDateAlert();
    	return false;
	}
	
	
	// Add countries that can be checked for user input
	@FXML
    void addCountryCheckbox() {
    	ArrayList<String> countries = DataAnalysis.getCountries();
    	for (String country: countries) {
    		CheckBox checkbox = new CheckBox(country);
    		checkbox.setOnAction(new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent e) {
    		    	for (CheckBox checkbox: tableA_countryCheckBoxList.getItems()) {
    					if(!checkbox.isSelected() && !checkbox.isDisabled()) {
    						tableA_selectAllCheckBox.setSelected(false);
    						return;
    					}
    		    	}
    				tableA_selectAllCheckBox.setSelected(true);
    		    }
    		});
    		tableA_countryCheckbox.add(checkbox);
    		
    		CheckBox checkbox2 = new CheckBox(country);
    		checkbox2.setOnAction(new EventHandler<ActionEvent>() {
    		    public void handle(ActionEvent e) {
    		    	for (CheckBox checkbox: chartA_countryCheckBoxList.getItems()) {
    					if(!checkbox.isSelected() && !checkbox.isDisabled()) {
    						chartA_selectAllCheckBox.setSelected(false);
    						return;
    					}
    		    	}
    				chartA_selectAllCheckBox.setSelected(true);
    		    }
    		});
    		chartA_countryCheckbox.add(checkbox2);
    	}
    	
    }
    
    // Disable Dates that are out of range
    void setMinMaxOfDatePicker(){
    	LocalDate minDate = Collections.min(dateLists);
    	LocalDate maxDate = Collections.max(dateLists);
//    	tableA
    	tableA_datePicker.setDayCellFactory(d ->
    	           new DateCell() {
    	               @Override 
    	               public void updateItem(LocalDate item, boolean empty) {
    	                   super.updateItem(item, empty);
    	                   setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
    	               }});
    	tableA_datePicker.setValue(maxDate);
    
//    	chartA
    	chartA_datePickerFrom.setDayCellFactory(d ->
        new DateCell() {
            @Override 
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
            }});
    	chartA_datePickerFrom.setValue(maxDate);
    	chartA_datePickerTo.setDayCellFactory(d ->
        new DateCell() {
            @Override 
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
            }});
    	chartA_datePickerTo.setValue(maxDate);
    	
    }
    
//    disable countries that are not available on selected Dates
    @FXML
    void tableA_disableUnavailableCountry() {
    	LocalDate dateSelected = tableA_datePicker.getValue();
    	if (!validateDate(dateSelected)) {
    		return;
    	}
    	tableA_countryCheckbox.forEach(checkbox -> {
    		ArrayList<LocalDate> dateList = countryDateMap.get(checkbox.getText());
    		if (!DataAnalysis.isBetween(dateSelected, dateList.get(0), dateList.get(1))) {
    			checkbox.setDisable(true);
    		}
    		else {
    			checkbox.setDisable(false);
    		}
    	});
    }
    
    @FXML
    void chartA_disableUnavailableCountry() {
    	LocalDate dateFrom = chartA_datePickerFrom.getValue();
    	LocalDate dateTo = chartA_datePickerTo.getValue();
    	if (!validateDate(dateFrom, dateTo)) {
    		return;
    	}
    	chartA_countryCheckbox.forEach(checkbox -> {
    		ArrayList<LocalDate> dateList = countryDateMap.get(checkbox.getText());
    		if (!DataAnalysis.isBetween(dateFrom, dateList.get(2), dateList.get(3)) && !DataAnalysis.isBetween(dateTo, dateList.get(2), dateList.get(3))) {
    			checkbox.setDisable(true);
    		}
    		else {
    			checkbox.setDisable(false);
    		}
    	});
    }
    
    void worldmapA_setSlider() {
    	ArrayList<String> continents = new ArrayList<>();
    	Collections.addAll(continents, "Asia", "Africa", "Europe", "Oceania", "North America", "South America");

    	LocalDate dateFrom = LocalDate.of(2020, 3, 1);
    	LocalDate dateTo = LocalDate.of(2021, 7, 20);

    	Duration duration = Duration.between(dateFrom.atStartOfDay(), dateTo.atStartOfDay());
    	int days = Math.round(duration.toDays());
    	
    	worldmapA_dateSlider.setMin(0);
    	worldmapA_dateSlider.setMax(days);
    }
    
    @FXML
    void worldMapA_setColor() {
    	LocalDate dateFrom = LocalDate.of(2020, 3, 1);
    	int offset = (int) Math.round(worldmapA_dateSlider.getValue());
    	LocalDate selectedDate = dateFrom.plusDays(offset);
    	textAreaConsole.setText(selectedDate.toString());
    	
//    	worldmapA_polygonMap.forEach((continent, image) -> {
//    		
//    	});
    	LinkedHashMap<String, Double> continentMap = worldmapA_dateContinentMap.get(selectedDate);
    	continentMap.forEach((continent, confirmedCases) ->{
    		worldMapA_changeColor(worldmapA_polygonMap.get(continent), confirmedCases);
    	});

    }
    
    void worldMapA_changeColor(Polygon image, double confirmedCases) {
    	Color color = getColorByConfirmedCases(confirmedCases); 
    	image.setFill(color);
    }
    void worldmapA_setPolygonMap() {
    	worldmapA_polygonMap = new LinkedHashMap<String, Polygon>();
    	worldmapA_polygonMap.put("Africa", worldmap_africa);
    	worldmapA_polygonMap.put("North America", worldmap_north_america);
    	worldmapA_polygonMap.put("South America", worldmap_south_america);
    	worldmapA_polygonMap.put("Asia", worldmap_asia);
    	worldmapA_polygonMap.put("Oceania", worldmap_oceania);
    	worldmapA_polygonMap.put("Europe", worldmap_europe);
    	
    	LinkedHashMap<String, Double> continentMap = worldmapA_dateContinentMap.get(LocalDate.of(2020, 3, 1));
    	continentMap.forEach((continent, confirmedCases) ->{
    		worldMapA_changeColor(worldmapA_polygonMap.get(continent), confirmedCases);
    	});
    	
    }

    Color getColorByConfirmedCases(double confirmedCases) {
    	double r, g, b;
    	if (confirmedCases < 10) {
    		r = 202; g = 235; b = 248;
    	}
    	else if (confirmedCases < 50) {
    		r = 170; g = 221; b = 244;
    	}
    	else if (confirmedCases < 100) {
    		r = 140; g = 201; b = 235;
    	}
    	else if (confirmedCases < 500) {
    		r = 112; g = 182; b = 225;
    	}
    	else if (confirmedCases < 1000) {
    		r = 89; g = 158; b = 209;
    	}
    	else if (confirmedCases < 3000) {
    		r = 65; g = 128; b = 190;
    	}
    	else if (confirmedCases < 5000) {
    		r = 49; g = 104; b = 174;
    	}
    	else if (confirmedCases < 10000) {
    		r = 30; g = 80; b = 153;
    	}
    	else if (confirmedCases < 20000) {
    		r = 40; g = 61; b = 117;
    	}
    	else if (confirmedCases < 40000) {
    		r = 30; g = 41; b = 87;
    	}
    	else {
    		r = 20; g = 31; b = 67;
    	}
    	
    	return new Color(r/255, g/255, b/255, 1);
    }
    
}

