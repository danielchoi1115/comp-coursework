package comp3111.covid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.*;
import edu.duke.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * 
 * Data Explorer on COVID-19
 * @author <a href=mailto:namkiu@ust.hk>Namkiu Chan</a>
 * @version	1.1
 * 
 */
public class DataAnalysis {
 
	public static CSVParser getFileParser(String dataset) {
	     FileResource fr = new FileResource("dataset/" + dataset);
	     return fr.getCSVParser(true);
		}
	

	public static String getConfirmedCases(String dataset, String iso_code) {
		String oReport = "";	
		long confirmedCases = 0;
		long numRecord = 0;
		long totalNumRecord = 0;
		
		for (CSVRecord rec : getFileParser(dataset)) {
			
			if (rec.get("iso_code").equals(iso_code)) {
				String s = rec.get("new_cases");
				if (!s.equals("")) {
					confirmedCases += Long.parseLong(s);
					numRecord++;
				}
			}		
			totalNumRecord++;
		}
		
		oReport = String.format("Dataset (%s): %,d Records\n\n", dataset, totalNumRecord);
		oReport += String.format("[Summary (%s)]\n", iso_code);
		oReport += String.format("Number of Confirmed Cases: %,d\n", confirmedCases);
		oReport += String.format("Number of Days Reported: %,d\n", numRecord);
		
		return oReport;
	}
	
	 public static String getConfirmedDeaths(String dataset, String iso_code) {
			String oReport = "";	
			long confirmedDeaths = 0;
			long numRecord = 0;
			long totalNumRecord = 0;
			
			for (CSVRecord rec : getFileParser(dataset)) {
				
				if (rec.get("iso_code").equals(iso_code)) {
					String s = rec.get("new_deaths");
					if (!s.equals("")) {
						confirmedDeaths += Long.parseLong(s);
						numRecord++;
					}
				}		
				totalNumRecord++;
			}
			
			oReport = String.format("Dataset (%s): %,d Records\n\n", dataset, totalNumRecord);
			oReport += String.format("[Summary (%s)]\n", iso_code);
			oReport += String.format("Number of Deaths: %,d\n", confirmedDeaths);
			oReport += String.format("Number of Days Reported: %,d\n", numRecord);
			
			return oReport;
	 }
	 
	 public static String getRateOfVaccination(String dataset, String iso_code) {
			String oReport = "";	
			long fullyVaccinated = 0;
			long numRecord = 0;
			long totalNumRecord = 0;
			long population = 0;
			float rate = 0.0f;
						
			for (CSVRecord rec : getFileParser(dataset)) {
				
				if (rec.get("iso_code").equals(iso_code)) {
					
					String s1 = rec.get("people_fully_vaccinated");
					String s2 = rec.get("population");		
					if (!s1.equals("") && !s2.equals("")) {
						fullyVaccinated = Long.parseLong(s1);
						population = Long.parseLong(s2);						
						numRecord++;
					}
				}		
				totalNumRecord++;
			}
			
			if (population !=0)
				rate = (float) fullyVaccinated / population * 100;
			
			oReport = String.format("Dataset (%s): %,d Records\n\n", dataset, totalNumRecord);
			oReport += String.format("[Summary (%s)]\n", iso_code);
			oReport += String.format("Number of People Fully Vaccinated: %,d\n", fullyVaccinated);
			oReport += String.format("Population: %,d\n", population);
			oReport += String.format("Rate of Vaccination: %.2f%%\n", rate);
			oReport += String.format("Number of Days Reported: %,d\n", numRecord);
			
			return oReport;
	 }
	 
	 public static ArrayList<String> getCountries() {
		 String dataset = "COVID_Dataset_v1.0.csv";
		 ArrayList<String> countries = new ArrayList<>();
		 for (CSVRecord rec : getFileParser(dataset)) {
			 countries.add(rec.get("location"));
		 }
		 Set<String> listWithoutDuplicates = new LinkedHashSet<String>(countries);
		 countries.clear();
		 countries.addAll(listWithoutDuplicates);
		 return countries;
	 }
	 
	 public static ArrayList<LocalDate> getDates() {
		 String dataset = "COVID_Dataset_v1.0.csv";
		 ArrayList<String> dateStringList = new ArrayList<>();
		 for (CSVRecord rec : getFileParser(dataset)) {
			 dateStringList.add(rec.get("date"));
		 }
		 Set<String> listWithoutDuplicates = new LinkedHashSet<String>(dateStringList);
		 dateStringList.clear();
		 dateStringList.addAll(listWithoutDuplicates);
		 
		 ArrayList<LocalDate> dates = new ArrayList<>();
         
    	 for (String dateString : dateStringList) {
    		 dates.add(stringToLocalDate(dateString));
    	 }
		 
		 return dates;
	 }
	 
	 public static boolean isBetween(LocalDate date, LocalDate dateFrom, LocalDate dateTo) {
		if ((date.isAfter(dateFrom) && date.isBefore(dateTo)) || date.isEqual(dateFrom) || date.isEqual(dateTo)) {
 			return true;
 		}
 		return false;
	 }
	 
	 public static LocalDate stringToLocalDate(String dateString) {
		 return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/d/yyyy"));
	 }
	 
	 public static String formatNumberWithComma(String numberString) {
		 if (numberString.strip() == "") {
			 return "N/A";
		 }
		 try {
			 // Check if it is integer
			 int num = Integer.parseInt(numberString);
			 return NumberFormat.getInstance().format(num);
		 } catch (NumberFormatException e1) {
			 try {
			 // Try if it is double
				 Double num = Double.parseDouble(numberString);
				 DecimalFormat formatter = new DecimalFormat("#,###.00");
				 return formatter.format(num);
			 }
			 catch (NumberFormatException e2) {
				 System.out.println("Number Parsing Error: " + numberString);
				 return "0";
			 }
		}
	 }
	 
	 public static ObservableList<ConfirmedCase> getConfirmedCases(ArrayList<String> selectedCountries, LocalDate dateInput) {
		 String dataset = "COVID_Dataset_v1.0.csv";
		 ObservableList<ConfirmedCase> confirmedCaseList = FXCollections.observableArrayList();
		 for (CSVRecord rec : getFileParser(dataset)) {
			 String countryName = rec.get("location");
			 if (selectedCountries.contains(countryName) && dateInput.isEqual(stringToLocalDate(rec.get("date")))) {
				 String totalCases = formatNumberWithComma(rec.get("total_cases"));
				 String totalCasesPerM	= formatNumberWithComma(rec.get("total_cases_per_million"));
				 ConfirmedCase confirmedcase = new ConfirmedCase(countryName, totalCases, totalCasesPerM);
				 confirmedCaseList.add(confirmedcase);
			 }
		 }
		 return confirmedCaseList;
	 }
	 
//	 chart A
	 public static LinkedHashMap<String, ArrayList<LocalDate>> getCountryDateMap() {
		 LinkedHashMap<String, ArrayList<LocalDate>> countryDateMap = new LinkedHashMap<>();
		 String dataset = "COVID_Dataset_v1.0.csv";
		 
		 ArrayList<LocalDate> dates = new ArrayList<>();
		 LocalDate minDate_new = LocalDate.of(0000,01,01);
		 LocalDate maxDate_new = LocalDate.of(0000,01,01);
		 LocalDate minDate_total = LocalDate.of(0000,01,01);
		 LocalDate maxDate_total = LocalDate.of(0000,01,01);
		 
		 String countryName = "";
		 for (CSVRecord rec : getFileParser(dataset)) {
			 if (countryName == "") {
				 countryName = rec.get("location");
			 }
			 else if (!countryName.equals(rec.get("location"))) {
				 dates.add(minDate_new);
				 dates.add(maxDate_new);
				 dates.add(minDate_total);
				 dates.add(maxDate_total);
				 countryDateMap.put(countryName, dates);
				 
				 countryName = rec.get("location");
				 dates = new ArrayList<LocalDate>();
				 minDate_new = LocalDate.of(0000,01,01);
				 maxDate_new = LocalDate.of(0000,01,01);
				 minDate_total = LocalDate.of(0000,01,01);
				 maxDate_total = LocalDate.of(0000,01,01);
			 }
			 LocalDate date = stringToLocalDate(rec.get("date"));
			 
			 if (!rec.get("new_cases").equals("")) {
				 if (minDate_new.equals(LocalDate.of(0000,01,01)) || minDate_new.isAfter(date)) {
					 minDate_new = date;
				 }
				 if (maxDate_new.equals(LocalDate.of(0000,01,01)) || maxDate_new.isBefore(date)) {
					 maxDate_new = date;
				 }
			 }
			 if (!rec.get("total_cases_per_million").equals("")) {
				 if (minDate_total.equals(LocalDate.of(0000,01,01)) || minDate_total.isAfter(date)) {
					 minDate_total = date;
				 }
				 if (maxDate_total.equals(LocalDate.of(0000,01,01)) || maxDate_total.isBefore(date)) {
					 maxDate_total = date;
				 }
			 }
		 }
		 dates.add(minDate_new);
		 dates.add(maxDate_new);
		 dates.add(minDate_total);
		 dates.add(maxDate_total);
		 countryDateMap.put(countryName, dates);
		 
		 return countryDateMap;
	 }
	 
	 
	 public static LinkedHashMap<String, ArrayList<LinkedHashMap<LocalDate, Double>>> getCumulativeMap(ArrayList<String> selectedCountries, LocalDate dateFrom, LocalDate dateTo) {
		 LinkedHashMap<String, ArrayList<LinkedHashMap<LocalDate, Double>>> cumulativeMap = new LinkedHashMap<>();
		 String dataset = "COVID_Dataset_v1.0.csv";
		 
		 ArrayList<LinkedHashMap<LocalDate, Double>> records = new ArrayList<>();
		 
		 String countryName = "";
		 for (CSVRecord rec : getFileParser(dataset)) {
			 if (countryName == "") {
				 countryName = rec.get("location");
			 }
			 else if (!countryName.equals(rec.get("location"))) {
				 if (records.size() > 0) {
					 cumulativeMap.put(countryName, records);
				 }
				 countryName = rec.get("location");
				 records = new ArrayList<LinkedHashMap<LocalDate, Double>>();
			 }
			 LocalDate date = stringToLocalDate(rec.get("date"));
			 Double total_cases_per_million = stringToDouble(rec.get("total_cases_per_million"));
			 
			 if (selectedCountries.contains(countryName) && isBetween(date, dateFrom, dateTo) && total_cases_per_million > -1.0) {
				 
				 LinkedHashMap<LocalDate, Double> dateMap = new LinkedHashMap<>();
				 dateMap.put(date, total_cases_per_million);
				 records.add(dateMap);		
			 }
		 }
		 if (records.size() > 0) {
			 cumulativeMap.put(countryName, records);
		 }
		 return cumulativeMap;
	 }
	 public static Double stringToDouble(String numberString) {
		 if (numberString == "") {
			 return -1.0;
		 }
		 else {
			 return Double.parseDouble(numberString);
		 }
	 }
	 
	 
//	 worldMap A
	 public static LinkedHashMap<LocalDate, LinkedHashMap<String, Double>> getDateContinentMap() {
		 LinkedHashMap<LocalDate, LinkedHashMap<String, Double>> dateContinentMap = new LinkedHashMap<>();
		 String dataset = "COVID_Dataset_v1.0.csv";
		 ArrayList<String> continents = new ArrayList<>();
	     Collections.addAll(continents, "Asia", "Africa", "Europe", "Oceania", "North America", "South America");

		 for (CSVRecord rec : getFileParser(dataset)) {
			 String location = rec.get("location");
			 LocalDate date = stringToLocalDate(rec.get("date"));
			 if (isBetween(date, LocalDate.of(2020, 3, 1), LocalDate.of(2021, 7, 20)) && !dateContinentMap.containsKey(date)) {
				 dateContinentMap.put(date, new LinkedHashMap<String, Double>());
			 }
			 
			 if (dateContinentMap.containsKey(date) && continents.contains(location)) {
				 dateContinentMap.get(date).put(location, stringToDouble(rec.get("total_cases_per_million")));
			 }

		 }
		 
		 
		 return dateContinentMap;
	 }
 
}