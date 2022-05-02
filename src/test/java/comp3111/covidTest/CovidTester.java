package comp3111.covidTest;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;

import comp3111.covid.ConfirmedCase;
import comp3111.covid.DataAnalysis;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class CovidTester {	
	ArrayList<String> getCountries;
	ArrayList<LocalDate> getDates;
	ObservableList<ConfirmedCase> getConfirmedCases;
	ArrayList<String> selectedCountries = new ArrayList<String>();
	LinkedHashMap<String, ArrayList<LocalDate>> getCountryDateMap;
	LinkedHashMap<String, LinkedHashMap<LocalDate, Double>> getCumulativeMap;
	String testCountry = "Zimbabwe";
	String testConfirmedCasesPerM = "88,415";
	LinkedHashMap<LocalDate, LinkedHashMap<String, Double>> getDateContinentMap;
	double r, g, b;
	
	@Before	
	public void setUp() throws Exception {		
		selectedCountries.add(testCountry);
	} 	

	/**
	 * 
	 *  DataAnalysis Testing
	 *  
	 */

//	getCountries()
	@Test	
	public void isGetCountriesNotNull() {	
		getCountries = DataAnalysis.getCountries();
		assertNotNull(getCountries);
	}		
	@Test	
	public void isGetCountriesLengthCorrect() {	
		getCountries = DataAnalysis.getCountries();
		assertEquals(getCountries.size(), 231);	
	}	
//	getDates()
	@Test	
	public void isGetDatesNotNull() {	
		getDates = DataAnalysis.getDates();
		assertNotNull(getDates);
	}		
	@Test	
	public void isGetDatesLengthCorrect() {	
		getDates = DataAnalysis.getDates();
		assertEquals(getDates.size(), 567);	
	}
//	isBetween()
	@Test	
	public void isBetween_validLB() {	
		assertTrue(DataAnalysis.isBetween(LocalDate.of(2020, 7, 1), LocalDate.of(2020, 7, 1), LocalDate.of(2020, 7, 20)));	
	}
	@Test	
	public void isBetween_validMiddle() {	
		assertTrue(DataAnalysis.isBetween(LocalDate.of(2020, 7, 10), LocalDate.of(2020, 7, 1), LocalDate.of(2020, 7, 20)));	
	}
	@Test	
	public void isBetween_validUB() {	
		assertTrue(DataAnalysis.isBetween(LocalDate.of(2020, 7, 20), LocalDate.of(2020, 7, 1), LocalDate.of(2020, 7, 20)));	
	}
	@Test	
	public void isBetween_invalidLB() {	
		assertFalse(DataAnalysis.isBetween(LocalDate.of(2020, 3, 1), LocalDate.of(2020, 7, 1), LocalDate.of(2020, 7, 20)));	
	}
	@Test	
	public void isBetween_invalidUB() {	
		assertFalse(DataAnalysis.isBetween(LocalDate.of(2020, 9, 1), LocalDate.of(2020, 7, 1), LocalDate.of(2020, 7, 20)));	
	}
	
//	stringToLocalDate()
	@Test	
	public void stringToLocalDate_correctParse() {	
		assertEquals(DataAnalysis.stringToLocalDate("3/5/2022"), LocalDate.of(2022, 3, 5));	
	}
	@Test	
	public void stringToLocalDate_incorrectParse() {	
		assertNotEquals(DataAnalysis.stringToLocalDate("3/5/2022"), LocalDate.of(2022, 5, 5));	
	}
	
//	formatNumberWithComma()
	@Test	
	public void formatNumber_NA() {	
		assertEquals(DataAnalysis.formatNumberWithComma(""), "N/A");	
	}
	@Test	
	public void formatNumber_int() {	
		assertEquals(DataAnalysis.formatNumberWithComma("100"), "100");	
	}
	@Test	
	public void formatNumber_double() {	
		assertEquals(DataAnalysis.formatNumberWithComma("140.00"), "140.00");	
	}
	@Test	
	public void formatNumber_invalid() {	
		assertEquals(DataAnalysis.formatNumberWithComma("asfw"), "N/A");	
	}
	
//	getConfirmedCases()
	@Test	
	public void getConfirmedCases_notNull() {	
		getConfirmedCases = DataAnalysis.getConfirmedCases(selectedCountries, LocalDate.of(2021, 7, 20));
		assertNotNull(getConfirmedCases);	
	}
	@Test	
	public void getConfirmedCases_noCountry() {	
		getConfirmedCases = DataAnalysis.getConfirmedCases(selectedCountries, LocalDate.of(2021, 7, 20));
		assertEquals(getConfirmedCases.get(0).getTotalCases(), testConfirmedCasesPerM);	
	}
	
//	maxDate()
	@Test	
	public void maxDate_validComparison() {	
		assertEquals(DataAnalysis.maxDate(
				LocalDate.of(2022, 3, 5), LocalDate.of(2022, 4, 5)), LocalDate.of(2022, 4, 5));	
	}
	@Test	
	public void maxDate_validBoundary() {	
		assertEquals(DataAnalysis.maxDate(
				LocalDate.of(2022, 4, 5), LocalDate.of(2022, 4, 5)), LocalDate.of(2022, 4, 5));	
	}
	
//	minDate()
	@Test	
	public void minDate_validComparison() {	
		assertEquals(DataAnalysis.minDate(
				LocalDate.of(2022, 4, 5), LocalDate.of(2022, 3, 5)), LocalDate.of(2022, 3, 5));	
	}
	@Test	
	public void minDate_validBoundary() {	
		assertEquals(DataAnalysis.minDate(
				LocalDate.of(2022, 4, 5), LocalDate.of(2022, 4, 5)), LocalDate.of(2022, 4, 5));	
	}

//	stringToDouble()
	@Test	
	public void stringToDouble_valid() {	
		assertEquals(DataAnalysis.stringToDouble("123.12"), 123.12, 0.001);
	}
	@Test	
	public void stringToDouble_validNone() {	
		assertEquals(DataAnalysis.stringToDouble(""), -1.0, 0.001);
	}
	@Test	
	public void stringToDouble_invalid() {	
		assertEquals(DataAnalysis.stringToDouble("asfa"), -1.0, 0.001);
	}
	
	
//	getCountryDateMap()
	@Test
	public void countryDateMap_notNull() {
		getCountryDateMap = DataAnalysis.getCountryDateMap();
		assertNotNull(getCountryDateMap);
	}
	@Test
	public void countryDateMap_correctSize() {
		getCountryDateMap = DataAnalysis.getCountryDateMap();
		assertEquals(getCountryDateMap.size(), 231);
	}
	@Test
	public void countryDateMap_correctDate() {
		getCountryDateMap = DataAnalysis.getCountryDateMap();
		assertEquals(getCountryDateMap.get(testCountry).get(0), LocalDate.of(2020, 3, 20));
	}
	
//	getCumulativeMap()
	@Test
	public void cumulativeMap_notNull() {
		getCumulativeMap = DataAnalysis.getCumulativeMap(selectedCountries, LocalDate.of(2021, 7, 11), LocalDate.of(2021, 7, 20));
		assertNotNull(getCumulativeMap);
	}
	@Test
	public void cumulativeMap_correctSize() {
		getCumulativeMap = DataAnalysis.getCumulativeMap(selectedCountries, LocalDate.of(2021, 7, 11), LocalDate.of(2021, 7, 20));
		assertEquals(getCumulativeMap.get(testCountry).size(), 10);
	}
	@Test
	public void cumulativeMap_correctValue() {
		getCumulativeMap = DataAnalysis.getCumulativeMap(selectedCountries, LocalDate.of(2021, 7, 11), LocalDate.of(2021, 7, 20));
		assertEquals(getCumulativeMap.get(testCountry).get(LocalDate.of(2021, 7, 20)), 5948.694, 0.001);
	}
	
//	getDateContinentMap()
	@Test
	public void dateContinentMap_notNull() {
		getDateContinentMap = DataAnalysis.getDateContinentMap();
		assertNotNull(getDateContinentMap);
	}
	@Test
	public void dateContinentMap_correctSize() {
		getDateContinentMap = DataAnalysis.getDateContinentMap();
		assertEquals(getDateContinentMap.get(LocalDate.of(2021,7,20)).size(), 6);
	}
	@Test
	public void dateContinentMap_correctValue() {
		getDateContinentMap = DataAnalysis.getDateContinentMap();
		assertEquals((double) getDateContinentMap.get(LocalDate.of(2021,7,20)).get("Asia"), 12803.607, 0.001);
	}
	
//	validateDate()
	@Test
	public void validateDateInOrder_validEqualDate() {
		assertTrue(DataAnalysis.validateDateInOrder(LocalDate.of(2020, 4, 5), LocalDate.of(2020, 4, 5)));
	}
	@Test
	public void validateDate_invalidWrongOrder() {
		assertFalse(DataAnalysis.validateDateInOrder(LocalDate.of(2020, 4, 5), LocalDate.of(2020, 4, 2)));
	}
	
	@Test
	public void validateDateInRange_inRange() {
		assertTrue(DataAnalysis.validateDateInRange(LocalDate.of(2020, 4, 5), LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)));
	}
	@Test
	public void validateDateInRange_inBoundary() {
		assertTrue(DataAnalysis.validateDateInRange(LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)));
	}
	@Test
	public void validateDateInRange_OutOfRange() {
		assertFalse(DataAnalysis.validateDateInRange(LocalDate.of(2020, 6, 5), LocalDate.of(2020, 4, 1), LocalDate.of(2020, 4, 30)));
	}
	
//	validateSize()
	@Test
	public void validateSize_validDataSize() {
		assertTrue(DataAnalysis.validateSize(10));
	}
	@Test
	public void validateSize_invalidDataSize() {
		assertFalse(DataAnalysis.validateSize(0));
	}

	@Test
	public void colorTest1() {
		r = 202; g = 235; b = 248;
		assertEquals(DataAnalysis.getColorByConfirmedCases(0), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest2() {
		r = 170; g = 221; b = 244;
		assertEquals(DataAnalysis.getColorByConfirmedCases(10), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest3() {
		r = 140; g = 201; b = 235;
		assertEquals(DataAnalysis.getColorByConfirmedCases(50), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest4() {
		r = 112; g = 182; b = 225;
		assertEquals(DataAnalysis.getColorByConfirmedCases(100), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest5() {
		r = 89; g = 158; b = 209;
		assertEquals(DataAnalysis.getColorByConfirmedCases(500), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest6() {
		r = 65; g = 128; b = 190;
		assertEquals(DataAnalysis.getColorByConfirmedCases(1000), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest7() {
		r = 49; g = 104; b = 174;
		assertEquals(DataAnalysis.getColorByConfirmedCases(3000), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest8() {
		r = 30; g = 80; b = 153;
		assertEquals(DataAnalysis.getColorByConfirmedCases(5000), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest9() {
		r = 40; g = 61; b = 117;
		assertEquals(DataAnalysis.getColorByConfirmedCases(10000), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest10() {
		r = 30; g = 41; b = 87;
		assertEquals(DataAnalysis.getColorByConfirmedCases(20000), new Color(r/255, g/255, b/255, 1));
	}
	@Test
	public void colorTest11() {
		r = 20; g = 31; b = 67;
		assertEquals(DataAnalysis.getColorByConfirmedCases(40000), new Color(r/255, g/255, b/255, 1));
	}


	
}
