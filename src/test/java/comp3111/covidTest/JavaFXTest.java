package comp3111.covidTest;

import static org.junit.Assert.*;

import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import static org.testfx.api.FxAssert.verifyThat;

import comp3111.covid.ConfirmedCase;
import comp3111.covid.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Test;

public class JavaFXTest extends ApplicationTest {
	private static final String UI_FILE = "/ui.fxml";  //file in the folder of src/main/resources/
	@Override
	public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource(UI_FILE));
   		VBox root = (VBox) loader.load();
   		Scene scene =  new Scene(root);
   		stage.setScene(scene);
   		stage.setTitle("Super Team T-29: Data Explorer on COVID-19");
   		stage.show();
	}
	@SuppressWarnings("unchecked")
	public <T extends Node> T find(final String query) {
		return (T) lookup(query).queryAll().iterator().next();
	}
	@After
	public void afterTest() throws TimeoutException {
		FxToolkit.hideStage();
		release(new KeyCode[] {});
		release(new MouseButton[] {});
	}
	
	/**
	 * 	Test for Table A
	 */
	@Test
	public void displayTableA_validInput() {
		clickOn("#tabReport1");
		sleep(300);
		ListView<CheckBox> list = find("#tableA_countryCheckBoxList");
		list.getItems().get(0).setSelected(true);
		clickOn("#displayTableA");
		sleep(1000);
		
		TableView<ConfirmedCase> table = find("#TableA");
		assertEquals(table.getItems().get(0).getTotalCases(), "142,414");
	}
	@Test
	public void displayTableA_noCountrySelected() {
		clickOn("#tabReport1");
		sleep(300);
		clickOn("#displayTableA");
		sleep(2000);
		press(KeyCode.ENTER).release(KeyCode.ENTER);
		TableView<ConfirmedCase> table = find("#TableA");
		assertEquals(table.getItems(), null);
	}
	@Test
	public void displayTableA_invalidDate() {
		clickOn("#tabReport1");
		sleep(300);
		DatePicker datepicker = find("#tableA_datePicker");
		datepicker.setValue(LocalDate.of(2020, 1, 1));
		
		clickOn("#displayTableA");
		sleep(2000);
		
		press(KeyCode.ENTER).release(KeyCode.ENTER);
		TableView<ConfirmedCase> table = find("#TableA");
		assertEquals(table.getItems(), null);
	}
	
	@Test
	public void testSelectAll_tableA() {
		clickOn("#tabReport1");
		clickOn("#tableA_selectAllCheckBox");
		ListView<CheckBox> list = find("#tableA_countryCheckBoxList");
		assertTrue(list.getItems().get(2).isSelected());
	}
	
	@Test	
	public void clearTableA() {	
		clickOn("#clearTableA");
		DatePicker datePicker = find("#tableA_datePicker");
		
		assertTrue(datePicker.getValue().equals(LocalDate.of(2021, 7, 20)));
	}		
	
	
	
/**
 * 	Test for Chart A
 */
	@Test
	public void displayChartA_validInput() {
		clickOn("#tabApp1");
		sleep(1000);
		ListView<CheckBox> list = find("#chartA_countryCheckBoxList");
		list.getItems().get(0).setSelected(true);
		clickOn("#displayChartA");
		sleep(1000);
		
		LineChart<String, Number> linechart = find("#chartA_lineChart");
		assertEquals(linechart.getData().get(0).getName(), "Afghanistan");
	}
	
	@Test
	public void displayChartA_noCountry() {
		clickOn("#tabApp1");
		sleep(1000);
		
		clickOn("#displayChartA");
		sleep(2000);
		press(KeyCode.ENTER).release(KeyCode.ENTER);
		LineChart<String, Number> linechart = find("#chartA_lineChart");
		assertEquals(linechart.getData().size(), 0);
	}
	
	@Test
	public void displayChartA_invalidDateOrder() {
		clickOn("#tabApp1");
		sleep(1000);
		ListView<CheckBox> list = find("#chartA_countryCheckBoxList");
		list.getItems().get(0).setSelected(true);
		DatePicker datePickerFrom = find("#chartA_datePickerFrom");
		datePickerFrom.setOnAction(null);
		datePickerFrom.setValue(LocalDate.of(2021, 1, 1));
		
		DatePicker datePickerTo = find("#chartA_datePickerTo");
		datePickerTo.setOnAction(null);
		datePickerTo.setValue(LocalDate.of(2020, 1, 1));
		sleep(1000);
		
		clickOn("#displayChartA");
		sleep(2000);
		press(KeyCode.ENTER).release(KeyCode.ENTER);
		LineChart<String, Number> linechart = find("#chartA_lineChart");
		assertEquals(linechart.getData().size(), 0);
	}
	
	@Test
	public void displayChartA_invalidDateRange() {
		clickOn("#tabApp1");
		sleep(1000);
		
		ListView<CheckBox> list = find("#chartA_countryCheckBoxList");
		list.getItems().get(0).setSelected(true);
		
		DatePicker datePickerFrom = find("#chartA_datePickerFrom");
		datePickerFrom.setOnAction(null);
		datePickerFrom.setValue(LocalDate.of(2019, 1, 1));
		DatePicker datePickerTo = find("#chartA_datePickerTo");
		datePickerTo.setOnAction(null);
		sleep(1000);
		press(KeyCode.ENTER).release(KeyCode.ENTER);
		
		clickOn("#displayChartA");
		sleep(2000);
		press(KeyCode.ENTER).release(KeyCode.ENTER);
		LineChart<String, Number> linechart = find("#chartA_lineChart");
		assertEquals(linechart.getData().size(), 0);
	}
	
	@Test
	public void displayCharteA_noDataFound() {
		clickOn("#tabApp1");
		sleep(1000);
		
		ListView<CheckBox> list = find("#chartA_countryCheckBoxList");
		list.getItems().get(0).setSelected(true);
		
		DatePicker datePickerFrom = find("#chartA_datePickerFrom");
		datePickerFrom.setValue(LocalDate.of(2020, 1, 1));
		DatePicker datePickerTo = find("#chartA_datePickerTo");
		datePickerTo.setValue(LocalDate.of(2020, 1, 1));
		clickOn("#displayChartA");
		sleep(2000);
		press(KeyCode.ENTER).release(KeyCode.ENTER);
		LineChart<String, Number> linechart = find("#chartA_lineChart");
		assertEquals(linechart.getData().size(), 0);
	}
	
	@Test
	public void testSelectAll_chartA() {
		clickOn("#tabApp1");
		clickOn("#chartA_selectAllCheckBox");
		ListView<CheckBox> list = find("#chartA_countryCheckBoxList");
		assertTrue(list.getItems().get(2).isSelected());
	}
	
	@Test
	public void testWorldMap() {
		clickOn("#worldMapA");
		Slider slider = find("#worldmapA_dateSlider");
		assertEquals((int) Math.round(slider.getMax()), 506);
	}
	

	
}
