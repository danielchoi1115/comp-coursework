package comp3111.covid;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVRecord;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;

import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;


/**
 * 
 * Data Explorer on COVID-19
 * @author <a href=mailto:schoiak@connect.ust.hk>CHOI, Seung Ryeol</a>
 * @version	1.0
 * 
 */
public class Controller {
	
    @FXML
    private Tab tabReport1;

    @FXML
    private Tab tabReport2;

    @FXML
    private TextField textfieldDataset1;
    
    @FXML
    private DatePicker datepickerDate;
    
    
   
    @FXML
    private TreeView<String> MainTree;
    
   
    CheckBoxTreeItem<String> root = new CheckBoxTreeItem<String>("all country");
    
    Set<TreeItem<String>> selected = new HashSet<TreeItem<String>>();


    private void AddColumns(ObservableList<String> list){

        for (String country:list){
            CheckBoxTreeItem<String> itemColumn = new CheckBoxTreeItem<String>(country);
            root.getChildren().add(itemColumn);
        }
    }
    
    private ObservableList<String> list = FXCollections.observableArrayList();
    
    @FXML
    private Button buttonDeathTable;
    
    @FXML
    private Tab tabReport3;

    @FXML
    private Tab tabApp1;

    @FXML
    private Tab tabApp2;

    @FXML
    private TextField textfieldDataset2;
    
    //@FXML
    //private ComboBox<String> comboCountries1;
    @FXML
    private TreeView<String> MainTree2;
    
    @FXML
    private DatePicker dateStart;
    
    @FXML
    private DatePicker dateEnd;
    
    @FXML
    private Button buttonDeathChart;
    
    @FXML
    private Tab tabApp3;

    @FXML
    private Tab worldMapA;
    
    @FXML
    private TextArea textAreaConsole;

//  Table A
    private LinkedHashMap<String, ArrayList<LocalDate>> countryDateMap;
    private ObservableList<CheckBox> tableA_countryCheckbox = FXCollections.observableArrayList();
    private ArrayList<LocalDate> dateLists;
    LocalDate minDate;
	LocalDate maxDate;
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
    private LineChart<Number, Number> chartA_lineChart; 
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
    private NumberAxis chartA_xAxis ;
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
    @FXML
    private LinkedHashMap<LocalDate, LinkedHashMap<String, Double>> worldmapA_dateContinentMap;
    private LinkedHashMap<String, Polygon> worldmapA_polygonMap;
    
    /**
     * This is the initializer.
     */
    @FXML
    protected void initialize() {
    	dateLists = DataAnalysis.getDates();
        minDate = Collections.min(dateLists);
    	maxDate = Collections.max(dateLists);
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

    	String isoCode = "";
    	String isowithCountry = "";
    	String continent = "";
    	for (CSVRecord rec : DataAnalysis.getFileParser("COVID_Dataset_v1.0.csv")){
    		if(!rec.get("iso_code").equals(isoCode)) {
    			isoCode = rec.get("iso_code");
    			continent = rec.get("continent");
    			isowithCountry = continent + " - " + rec.get("location");
    			list.add(isowithCountry);
    		}
    	}

    	//comboCountries.setItems(list);
    	//comboCountries1.setItems(list);
    	
    	/****************************MainTree********************************/
    	AddColumns(list);
        MainTree.setRoot(root);
        MainTree.setShowRoot(true);
        
        MainTree2.setRoot(root);
        MainTree2.setShowRoot(true);
        
        root.setExpanded(true);       

        MainTree.setCellFactory(CheckBoxTreeCell.forTreeView());
        //MainTree.setCellFactory(p-> new CheckBoxTreeCell()); 
    	//MainTree.setCellFactory(p-> new CheckBoxTreeCell()); 
        MainTree2.setCellFactory(CheckBoxTreeCell.forTreeView());
        
        MainTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        MainTree2.setCellFactory(CheckBoxTreeCell.forTreeView());
        /*MainTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
              @Override
              public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                  CheckBoxTreeItem<String> treeItem = (CheckBoxTreeItem)newValue;

              }
          });*/
        
       
        root.addEventHandler(CheckBoxTreeItem.checkBoxSelectionChangedEvent(),(CheckBoxTreeItem.TreeModificationEvent<String>evt)-> {
           
            CheckBoxTreeItem<String> item = evt.getTreeItem();

            if (evt.wasIndeterminateChanged()) {
                if (item.isIndeterminate()) {
                    selected.remove(item);
                } else if (item.isSelected()) {
                    selected.add(item);
                }
            } else if (evt.wasSelectionChanged()) {
                if (item.isSelected()) {
                    selected.add(item);
                } else {
                    selected.remove(item);
                }
            }
            
        });
        
        MainTree.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<TreeItem <String>>() {
                @Override
                public void changed(ObservableValue<? extends TreeItem<String>> observableValue,
                TreeItem<String> oldItem, TreeItem<String> newItem) {
                    System.out.println("getSelectionModel:"+newItem.getValue());
                }
            });
    }
    
    /**
     * This method is used in initializer to add checkboxes for UI.
     */
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
    
	/**
     * This method is used in initializer to set the inital datepicker value and set its boundary.
     */
    void setMinMaxOfDatePicker(){
    	tableA_datePicker.setDayCellFactory(d ->
    	           new DateCell() {
    	               @Override 
    	               public void updateItem(LocalDate item, boolean empty) {
    	                   super.updateItem(item, empty);
    	                   setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
    	               }});
    	tableA_datePicker.setValue(maxDate);
    
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
    /**
     * This method is used in the initializer to set the slider of World Map A.
     */
    void worldmapA_setSlider() {
    	LocalDate dateFrom = LocalDate.of(2020, 3, 1);
    	LocalDate dateTo = LocalDate.of(2021, 7, 20);

    	Duration duration = Duration.between(dateFrom.atStartOfDay(), dateTo.atStartOfDay());
    	int days = Math.round(duration.toDays());
    	
    	worldmapA_dateSlider.setMin(0);
    	worldmapA_dateSlider.setMax(days);
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
    
    
    
    /**
     * This method is used to display the Table A
     * This retrieves data accordingly and displays total confirmed caese of countries on the table.
     */
	@FXML 
    void doDisplayTableA() {
		TableA.setItems(null);
		
    	ArrayList<String> selectedCountries = getSelectedCountries_tableA();
    	LocalDate dateSelected = getDateSelected_tableA();
    	
    	ObservableList<ConfirmedCase> confirmedCaseList = DataAnalysis.getConfirmedCases(selectedCountries, dateSelected);
    	if (validateListSize(selectedCountries.size(), confirmedCaseList.size()) && validateDate(dateSelected, dateSelected, minDate, maxDate)) {
			countryName.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("countryName"));
			totalCases.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("totalCases"));
			totalCasesPerM.setCellValueFactory(new PropertyValueFactory<ConfirmedCase, String>("totalCasesPerM"));
			TableA.setItems(confirmedCaseList);
    	}
    }
    
	
	/**
     * This method is used to display the Chart A.
     * This retrieves data accordingly and displays cumulative confirmed cases of countries on the chart.
     */
	@FXML 
    void doDisplayChartA() {
		
		chartA_lineChart.getData().clear();
		
		ArrayList<String> selectedCountries = getSelectedCountries_chartA();
    	LocalDate dateFrom = getDateFrom_chartA();
    	LocalDate dateTo = getDateTo_chartA();

		LinkedHashMap<String, LinkedHashMap<LocalDate, Double>> cumulativeMap = DataAnalysis.getCumulativeMap(selectedCountries, dateFrom, dateTo);
		if (validateListSize(selectedCountries.size(), cumulativeMap.size()) && validateDate(dateFrom, dateTo, minDate, maxDate)) {
			ObservableList<XYChart.Series<Number, Number>> seriesList = FXCollections.observableArrayList();
			
			cumulativeMap.forEach((countryName, dateMapList) -> {
				XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
				series.setName(countryName);
				dateMapList.forEach((date, totalConfirmedCase) -> {
					Number record = (Number) totalConfirmedCase;
					Number epochDate = DataAnalysis.localDatetoEpochNumber(date);
					series.getData().add(new XYChart.Data<Number, Number>(epochDate, record)) ;
				});
				seriesList.add(series);
			});
			
			chartA_lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
			chartA_lineChart.setData(seriesList);
			
			chartA_xAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(chartA_xAxis) {             
					@Override             
					public String toString(Number object) {      
						return DataAnalysis.epochToDateString(object.longValue());              
					}
				});
	    	
	    	long min = 99999;
	    	long max = 0;
	    	
	    	for(XYChart.Series<Number, Number> series: seriesList) {
	    		for(XYChart.Data<Number, Number> data: series.getData()) {
	    			long temp = data.getXValue().longValue();
	    			min = temp < min ? temp : min;
	    			max = temp > max ? temp : max;
	    		}
	    	}
	    	
			chartA_xAxis.setAutoRanging(false);
			chartA_xAxis.setTickUnit(Math.ceil((max-min)/7));
			chartA_xAxis.setLowerBound(min);
			chartA_xAxis.setUpperBound(max);
			chartA_xAxis.setTickLabelsVisible(true);
			chartA_yAxis.setTickLabelsVisible(true);
		}
	}
	
	/**
     * This method is used to clear Table A.
     */
	@FXML
	void clearTableA() {
		if (tableA_countryCheckBoxList.getItems().size() == 0) {
			return;
		}
		TableA.setItems(null);
		for (CheckBox checkbox: tableA_countryCheckBoxList.getItems()) {
			checkbox.setSelected(false);
    	}
		LocalDate maxDate = Collections.max(dateLists);
		tableA_datePicker.setValue(maxDate);
		tableA_selectAllCheckBox.setSelected(false);
	}
	
	/**
     * This method is used to clear Chart A.
     */
	@FXML
	void clearChartA() {
		if (chartA_countryCheckBoxList.getItems().size() == 0) {
			return;
		}
		chartA_lineChart.getData().clear();
		for (CheckBox checkbox: chartA_countryCheckBoxList.getItems()) {
			checkbox.setSelected(false);
    	}
		LocalDate maxDate = Collections.max(dateLists);
		chartA_datePickerTo.setValue(maxDate);
		chartA_datePickerFrom.setValue(maxDate);
		chartA_selectAllCheckBox.setSelected(false);
		chartA_xAxis.setTickLabelsVisible(false);
		chartA_yAxis.setTickLabelsVisible(false);
		chartA_xAxis.setLowerBound(0);
		chartA_xAxis.setUpperBound(100);
	}
	
	/**
     * This method is used to select every checkbox in table A.
     */
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
	
	/**
     * This method is used to select every checkbox in chart A.
     */
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

	
	
	
    /**
     * This method is used in dynamically disable a checkbox of a country when it is not available on a selected date for Table A.
     */

    @FXML
    public void generateTable(ActionEvent event) {
    	System.out.println("generateTable");
    	 
    	 List<String> allCountry=new ArrayList<String>();
    	 selected.stream().map(TreeItem::getValue).forEach(c->{
    		 allCountry.add(c);
    	 });
    	 
    	 
    	 if(selected.size()>=11) {
     		FxAlert.show("Please country&region max 10");
         	return;
    	 }
    	 
    	
    	
    	if(selected.size()==0) {
    		//
    		FxAlert.show("Please select at least one country&region");
        	return;
        }
        LocalDate l=datepickerDate.getValue();
        String date=null;
        if(l!=null) {
	    	int year=l.getYear();
	    	int month=l.getMonthValue();
	    	int day=l.getDayOfMonth();
	    	date=month+"/"+day+"/"+year;
	    	//date=(month<10?"0"+month:month)+"/"+(day<10?"0"+day:day)+"/"+year;
        }
    	String oReport = DataAnalysis.getRateOfTotalDeaths(date, allCountry);
    	textAreaConsole.setText(oReport);
    }
    
    @FXML
    public void generateChart(ActionEvent event) {
    	System.out.println("generateChart");
    	
    	List<String> allCountry=new ArrayList<String>();
   	 selected.stream().map(TreeItem::getValue).forEach(c->{
   		 allCountry.add(c);
   	 });
   	 
   	 
   	 if(selected.size()>=10) {
    		FxAlert.show("Please country&region max 10");
        	return;
   	 }
   	 
   	
   	
   	if(selected.size()==0) {
   		//
   		FxAlert.show("Please select at least one country&region");
       	return;
       }
    	
    	 LocalDate l1=dateStart.getValue();
         String ssdate=null;
         if(l1!=null) {
 	    	int year=l1.getYear();
 	    	int month=l1.getMonthValue();
 	    	int day=l1.getDayOfMonth();
 	    	ssdate=year+"-"+(month<10?"0"+month:month)+"-"+(day<10?"0"+day:day);
         }else {
        	 FxAlert.show("Please select dateStart");
         	return;
         }
         
         LocalDate l2=dateEnd.getValue();
         String eedate=null;
         if(l2!=null) {
 	    	int year=l2.getYear();
 	    	int month=l2.getMonthValue();
 	    	int day=l2.getDayOfMonth();
 	    	eedate=year+"-"+(month<10?"0"+month:month)+"-"+(day<10?"0"+day:day);
         }else {
        	 FxAlert.show("Please select dateEnd");
         	return;
         }
         
         
         //Please select a valid start/end date(2020-03-01 to 2021-07-20)
         long day=subDaysByDate(ssdate,eedate);
         if(day>30 ||day<=0 ) {
        	 FxAlert.show(String.format("Please select a valid start/end date(%s to %s)",ssdate,eedate));
          	return;
         }
         
       
         CategoryAxis xAxis = new CategoryAxis();
         NumberAxis yAxis = new NumberAxis();//1, 100, 10
         
         xAxis.setLabel("country");
         yAxis.setLabel("total deaths");
         
         LineChart linechart = new LineChart(xAxis, yAxis);
         
         XYChart.Series series = new XYChart.Series();
         Map<String,Double> map=DataAnalysis.getRateOfChart(ssdate,eedate, allCountry);
         for (String key : map.keySet()) {
        	    Double value = map.get(key);
        	    System.out.println("Key = " + key + ", Value = " + value);
        	    series.getData().add(new XYChart.Data(key, value));
         }	
     
         
         series.setName("totald eaths per million");
         
         linechart.getData().add(series);
         
         StackPane pane = new StackPane(linechart);
         pane.setPadding(new Insets(15, 15, 15, 15));
         pane.setStyle("-fx-background-color: BEIGE");
            Stage stage=new Stage();
    		Scene scene =  new Scene(pane);
    		stage.setScene(scene);
    		stage.setTitle("Cumulative Confirmed COVID-19 Deaths(per 1M)");
    		stage.show();
    		
    		
    		

    }
    
    
    /**
     * How to get the difference in days between two dates
     *
     * @param startDate String storing the start date
     * @param endDate String storing the end date
     * @return The difference in days, return -1 if parsing fails
     **/
    public static long subDaysByDate(String startDate, String endDate) {
    	String DATE_PATTERN ="yyyy-MM-dd";// ;
        long sub;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
            LocalDate start = LocalDate.parse(startDate, dateTimeFormatter);
            LocalDate end = LocalDate.parse(endDate, dateTimeFormatter);
 
            sub = end.toEpochDay() - start.toEpochDay();
        } catch (DateTimeParseException e) {
        	e.printStackTrace();
            sub = -1;
        }
        return sub;
    }
    /**
     * This method is used in dynamically disable a checkbox of a country when it is not available on a selected date for Table A.
     */
    @FXML
    void tableA_disableUnavailableCountry() {
    	LocalDate dateSelected = tableA_datePicker.getValue();
    	validateDate(dateSelected, dateSelected, minDate, maxDate);
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
    
    /**
     * This method is used in dynamically disable a checkbox of a country when it is not available on a selected date for Chart A.
     */
    @FXML
    void chartA_disableUnavailableCountry() {
    	LocalDate dateFrom = chartA_datePickerFrom.getValue();
    	LocalDate dateTo = chartA_datePickerTo.getValue();
    	validateDate(dateFrom, dateTo, minDate, maxDate);
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
    
    /**
     * This method is used change the color of continents when user slides the slider
     */
    @FXML
    void worldMapA_setColor() {
    	LocalDate dateFrom = LocalDate.of(2020, 3, 1);
    	int offset = (int) Math.round(worldmapA_dateSlider.getValue());
    	LocalDate selectedDate = dateFrom.plusDays(offset);
    	
    	LinkedHashMap<String, Double> continentMap = worldmapA_dateContinentMap.get(selectedDate);
    	continentMap.forEach((continent, confirmedCases) ->{
    		worldMapA_changeColor(worldmapA_polygonMap.get(continent), confirmedCases);
    	});

    }
    /**
     * This method is set the color of an image according to the confirmed cases per million
     */
    void worldMapA_changeColor(Polygon image, double confirmedCases) {
    	Color color = DataAnalysis.getColorByConfirmedCases(confirmedCases); 
    	image.setFill(color);
    }
    
    /**
     * This method is used retrieve the countries user selected for Table A
     * @return {@code ArrayList} This returns an {@code ArrayList} of country name in {@code String}.
     */
    public ArrayList<String> getSelectedCountries_tableA(){
		ArrayList<String> selectedCountries = new ArrayList<>();
		for (CheckBox checkbox: tableA_countryCheckBoxList.getItems()) {
    		if (checkbox.isSelected()) {
    			selectedCountries.add(checkbox.getText());
    		}
    	}
		return selectedCountries;
	}
    
    /**
     * This method is used retrieve the date user selected for Table A
     * @return {@code LocalDate} This returns an {@code LocalDate}.
     */
	public LocalDate getDateSelected_tableA() {
		return tableA_datePicker.getValue();
	}
	
	/**
     * This method is used retrieve the countries user selected for Chart A
     * @return {@code ArrayList} This returns an {@code ArrayList} of country name in {@code String}.
     */
	public ArrayList<String> getSelectedCountries_chartA(){
		ArrayList<String> selectedCountries = new ArrayList<>();
    	for (CheckBox checkbox: chartA_countryCheckBoxList.getItems()) {
    		if (checkbox.isSelected()) {
    			selectedCountries.add(checkbox.getText());
    		}
    	}
		return selectedCountries;
	}
	
	/**
     * This method is used retrieve the 'date from' user selected for Chart A
     * @return {@code LocalDate} This returns an {@code LocalDate}.
     */
	public LocalDate getDateFrom_chartA() {
		return chartA_datePickerFrom.getValue();
	}
	/**
     * This method is used retrieve the 'date to' user selected for Chart A
     * @return {@code LocalDate} This returns an {@code LocalDate}.
     */
	public LocalDate getDateTo_chartA() {
		return chartA_datePickerTo.getValue();
	}
	
	/**
     * This method is used validate that user has selected at least one country and there is a record.
     * If they are not valid, it shows an alert message and returns false.
     * @param countrySize The size of a list of countries that user selected.
     * @param dataSize The size of the list containing corresponding confirmed cases
     * 
     * @return {@code boolean} This returns {@code true} if both {@code countrySize} and {@code dataSize} are greater than 0, otherwise false.
     */
	public static boolean validateListSize(int countrySize, int dataSize) {
		if (!DataAnalysis.validateSize(countrySize)) {
    		Alerter.noCountrySelectedAlert();
    		return false;
    	}
    	else if (!DataAnalysis.validateSize(dataSize)) {
			Alerter.noDataAlert();
    		return false;
    	}
		return true;
	}
	/**
     * This method is used validate that user has selected valid date from the datepicker.
     * If they are not valid, it shows an alert message and returns false.
     * @param dateFrom This is starting date of the period.
     * @param dateTo This is last date of the period. 
     * @param minDate This is the lower bound of date that can be selected.
     * @param maxDate This is the upper bound of date that can be selected.
     * 
     * @return {@code boolean} This returns {@code true} if both {@code dateFrom} is before or equal to {@code dateTo} and both are within {@code minDate} and {@code maxDate}
     */
	public static boolean validateDate(LocalDate dateFrom, LocalDate dateTo, LocalDate minDate, LocalDate maxDate) {
		if (!DataAnalysis.validateDateInOrder(dateFrom, dateTo)) {
    		Alerter.dateNotInOrderAlert();
    		return false;
    	}
    	else if (!DataAnalysis.validateDateInRange(dateFrom, minDate, maxDate) || !DataAnalysis.validateDateInRange(dateTo, minDate, maxDate)) {
    		Alerter.dateNotInRangeAlert();
    		return false;
    	}
		return true;
	}
   
    
}

