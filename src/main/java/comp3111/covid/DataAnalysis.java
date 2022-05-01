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
import javafx.scene.paint.Color;


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
	

//	public static String getConfirmedCases(String dataset, String iso_code) {
//		String oReport = "";	
//		long confirmedCases = 0;
//		long numRecord = 0;
//		long totalNumRecord = 0;
//		
//		for (CSVRecord rec : getFileParser(dataset)) {
//			
//			if (rec.get("iso_code").equals(iso_code)) {
//				String s = rec.get("new_cases");
//				if (!s.equals("")) {
//					confirmedCases += Long.parseLong(s);
//					numRecord++;
//				}
//			}		
//			totalNumRecord++;
//		}
//		
//		oReport = String.format("Dataset (%s): %,d Records\n\n", dataset, totalNumRecord);
//		oReport += String.format("[Summary (%s)]\n", iso_code);
//		oReport += String.format("Number of Confirmed Cases: %,d\n", confirmedCases);
//		oReport += String.format("Number of Days Reported: %,d\n", numRecord);
//		
//		return oReport;
//	}
//	
//	 public static String getConfirmedDeaths(String dataset, String iso_code) {
//			String oReport = "";	
//			long confirmedDeaths = 0;
//			long numRecord = 0;
//			long totalNumRecord = 0;
//			
//			for (CSVRecord rec : getFileParser(dataset)) {
//				
//				if (rec.get("iso_code").equals(iso_code)) {
//					String s = rec.get("new_deaths");
//					if (!s.equals("")) {
//						confirmedDeaths += Long.parseLong(s);
//						numRecord++;
//					}
//				}		
//				totalNumRecord++;
//			}
//			
//			oReport = String.format("Dataset (%s): %,d Records\n\n", dataset, totalNumRecord);
//			oReport += String.format("[Summary (%s)]\n", iso_code);
//			oReport += String.format("Number of Deaths: %,d\n", confirmedDeaths);
//			oReport += String.format("Number of Days Reported: %,d\n", numRecord);
//			
//			return oReport;
//	 }
//	 
//	 public static String getRateOfVaccination(String dataset, String iso_code) {
//			String oReport = "";	
//			long fullyVaccinated = 0;
//			long numRecord = 0;
//			long totalNumRecord = 0;
//			long population = 0;
//			float rate = 0.0f;
//						
//			for (CSVRecord rec : getFileParser(dataset)) {
//				
//				if (rec.get("iso_code").equals(iso_code)) {
//					
//					String s1 = rec.get("people_fully_vaccinated");
//					String s2 = rec.get("population");		
//					if (!s1.equals("") && !s2.equals("")) {
//						fullyVaccinated = Long.parseLong(s1);
//						population = Long.parseLong(s2);						
//						numRecord++;
//					}
//				}		
//				totalNumRecord++;
//			}
//			
//			if (population !=0)
//				rate = (float) fullyVaccinated / population * 100;
//			
//			oReport = String.format("Dataset (%s): %,d Records\n\n", dataset, totalNumRecord);
//			oReport += String.format("[Summary (%s)]\n", iso_code);
//			oReport += String.format("Number of People Fully Vaccinated: %,d\n", fullyVaccinated);
//			oReport += String.format("Population: %,d\n", population);
//			oReport += String.format("Rate of Vaccination: %.2f%%\n", rate);
//			oReport += String.format("Number of Days Reported: %,d\n", numRecord);
//			
//			return oReport;
//	 }
	 
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
				 double num = Double.parseDouble(numberString);
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
				 if (!totalCases.equals("N/A") && !totalCasesPerM.equals("N/A")) {
					 confirmedCaseList.add(confirmedcase);
				 }
			 }
		 }
		 return confirmedCaseList;
	 }
	 
//	 helper
	 public static LocalDate maxDate(LocalDate date1, LocalDate date2) {
		 if (date1.equals(LocalDate.of(0000,01,01)) || date1.isBefore(date2)) {
			 return date2;
		 }
		 return date1;
	 }
	 public static LocalDate minDate(LocalDate date1, LocalDate date2) {
		 if (date1.equals(LocalDate.of(0000,01,01)) || date1.isAfter(date2)) {
			 return date2;
		 }
		 return date1;
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
			 if (countryName.isEmpty()) {
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
				 
				 minDate_new = minDate(minDate_new, date);
				 maxDate_new = maxDate(maxDate_new, date);

			 }
			 if (!rec.get("total_cases_per_million").equals("")) {
				 minDate_total = minDate(minDate_total, date);
				 maxDate_total = maxDate(maxDate_total, date);
			 }
		 }
		 dates.add(minDate_new);
		 dates.add(maxDate_new);
		 dates.add(minDate_total);
		 dates.add(maxDate_total);
		 countryDateMap.put(countryName, dates);
		 
		 return countryDateMap;
	 }
	 
	 
	 public static LinkedHashMap<String, LinkedHashMap<LocalDate, Double>> getCumulativeMap(ArrayList<String> selectedCountries, LocalDate dateFrom, LocalDate dateTo) {
		 LinkedHashMap<String, LinkedHashMap<LocalDate, Double>> cumulativeMap = new LinkedHashMap<>();
		 String dataset = "COVID_Dataset_v1.0.csv";
		 
		 LinkedHashMap<LocalDate, Double> records = new LinkedHashMap<>();
		 
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
				 records = new LinkedHashMap<LocalDate, Double>();
			 }
			 LocalDate date = stringToLocalDate(rec.get("date"));
			 double total_cases_per_million = stringToDouble(rec.get("total_cases_per_million"));
			 
			 if (selectedCountries.contains(countryName) && isBetween(date, dateFrom, dateTo) && total_cases_per_million > -1.0) {
				 
//				 LinkedHashMap<LocalDate, Double> dateMap = new LinkedHashMap<>();
				 records.put(date, total_cases_per_million);
//				 records.add(dateMap);		
			 }
		 }
		 if (records.size() > 0) {
			 cumulativeMap.put(countryName, records);
		 }
		 return cumulativeMap;
	 }
	 
	 public static double stringToDouble(String numberString) {
		 if (numberString == "") {
			 return -1.0;
		 }
		 else {
			 try {
				 return Double.parseDouble(numberString);
			 } catch (Exception e) {
				 return -1.0; 
			 }
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
	 
//	 Validator
	public static boolean validateDateInOrder(LocalDate dateFrom, LocalDate dateTo) {
	    	if (dateFrom.isEqual(dateTo) || (dateFrom.isBefore(dateTo))) {
	    		return true;
	    	}
	    	return false;
	}
	
	public static boolean validateDateInRange(LocalDate date, LocalDate minDate, LocalDate maxDate) {
    	if (isBetween(date, minDate, maxDate)) {
    		return true;
    	}
    	return false;
	}
	
	public static boolean validateSize(int size) {
		if (size == 0) {
    		return false;
		}
		return true;
	}
	
	public static Color getColorByConfirmedCases(double confirmedCases) {
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