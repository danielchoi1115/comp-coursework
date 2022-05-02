package comp3111.covid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
 * @author <a href=mailto:schoiak@connect.ust.hk>CHOI, Seung Ryeol</a>
 * @version	1.0
 * 
 */
public class DataAnalysis {
	/**
	   * Method written by TA.
	   * @param dataset dataset name
	   * @return {@code CSVParser}.
	  */
	public static CSVParser getFileParser(String dataset) {
	     FileResource fr = new FileResource("dataset/" + dataset);
	     return fr.getCSVParser(true);
		}

	/**
	   * This method is used to get a list of countries in COVID_Dataset_v1.0.
	   * @return {@code ArrayList} This returns all countries in the dataset.
	  */
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
	 
	 /**
	   * This method is used to get a list of dates in COVID_Dataset_v1.0 and 
	   * convert {@code Strings} in date format into {@code LocalDates}.
	   * @return {@code ArrayList} This returns all countries in the dataset.
	  */
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
	 
	 /**
	  * This method is used verify a date is between two dates. 
	  * @param date This is the date that is to be compared.
	  * @param dateFrom This is the lower bound of the date.
	  * @param dateTo This is the upper bound of the date.
	  * @return {@code boolean} This returns {@code true} if the date is within {@code dateFrom} and {@code dateTo}, otherwise {@code false}.
	  */
	 public static boolean isBetween(LocalDate date, LocalDate dateFrom, LocalDate dateTo) {
		if ((date.isAfter(dateFrom) && date.isBefore(dateTo)) || date.isEqual(dateFrom) || date.isEqual(dateTo)) {
 			return true;
 		}
 		return false;
	 }
	 
	 /**
	  * This method is to convert date String to LocalDate object. 
	  * @param dateString This is the date String in "M/d/yyyy" format that is to be converted.
	  * @return {@code LocalDate} This returns the corresponding {@code LocalDate} object. 
	  */
	 public static LocalDate stringToLocalDate(String dateString) {
		 return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/d/yyyy"));
	 }
	 
	 /**
	  * This method is used to format a number String into a number String with commas. 

	  * @param numberString This is the String that is to be formatted.
	  * @return {@code String} <br>   
	  * If the String represents an {@code integer}, it returns a number without decimals. <br>
	  * If the String represents a {@code double}, it returns a {@code double} rounded to two decimal places. <br>
	  * Otherwise returns {@code "N/A"} if the String is empty or failed to format.
	  */
	 public static String formatNumberWithComma(String numberString) {
		 if (numberString.strip().isEmpty()) {
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
				 DecimalFormat formatter = new DecimalFormat("#,##0.00");
				 return formatter.format(num);
			 }
			 catch (NumberFormatException e2) {
				 System.out.println("Number Parsing Error: " + numberString);
				 return "N/A";
			 }
		}
	 }
	 
	 /**
	  * This method is used to get a list of ConfirmedCases object. 
	  * It extracts Total confirmed Cases and total confirmed cases per million of user selected countries on a date of interest.
	  * @param selectedCountries This is the list of countries selected by the user.
	  * @param dateInput This is the date of interest.
	  * @return {@code ObservableList} This returns an observable list of {@code ConfirmedCase} objects.
	  */
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
	 
	 /**
	  * This method is used compare two LocalDate objects and return the one that is after. 
	  * @param date1 This is the first LocalDate parameter.
	  * @param date2 This is the second LocalDate parameter.
	  * @return {@code LocalDate} This returns {@code date1} or {@code date2} that is after the other.
	  */
	 public static LocalDate maxDate(LocalDate date1, LocalDate date2) {
		 if (date1.equals(LocalDate.of(0000,01,01)) || date1.isBefore(date2)) {
			 return date2;
		 }
		 return date1;
	 }
	 
	 /**
	  * This method is used compare two LocalDate objects and return the one that is before. 
	  * @param date1 This is the first LocalDate parameter.
	  * @param date2 This is the second LocalDate parameter.
	  * @return {@code LocalDate} This returns {@code date1} or {@code date2} that is before the other.
	  */
	 public static LocalDate minDate(LocalDate date1, LocalDate date2) {
		 if (date1.equals(LocalDate.of(0000,01,01)) || date1.isAfter(date2)) {
			 return date2;
		 }
		 return date1;
	 }
	 
	 /**
	  * This method is used to get each country's first and last date of record from the dataset.
	  * <p>Example output: {Afghanistan=[2020-02-24, 2021-07-20, 2020-02-24, 2021-07-20]} 
	  * @return {@code LinkedHashMap} This returns the key:value map in the form of {country : [date of first total cases record, lastDate]}.
	  */
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
	 
	 /**
	  * This method is used get the Total cases per million of selected country(s) on a period of interest.
	  * <p> Example input: <br>
	  * selectedCountries =  ["Afghanistan"], dateFrom = 2021-07-13, dateTo = 2021-07-16
	  * <br>
	  * <p> Example output: <br>	  
	  * Afghanistan={2021-07-13=3458.997, 2021-07-14=3510.116, 2021-07-15=3541.199, 2021-07-16=3541.199}}
	  * 
	  * @param selectedCountries This is list of countries chosen by user.
	  * @param dateFrom This is first date of the period of interest
	  * @param dateTo This is last date of the period of interest
	  * 
	  * @return {@code LinkedHashMap} This returns the key:value set in the form of {country : {Date : confirmed cases per million}}.
	  */
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
				 records.put(date, total_cases_per_million);
			 }
		 }
		 if (records.size() > 0) {
			 cumulativeMap.put(countryName, records);
		 }

		 return cumulativeMap;
	 }
	 
	 /**
	  * This method converts a string to corresponding double
	  *
	  * @param numberString this is {@code String} representing a number.
	  * @return {@code double} This returns a {@code double} corresponding to {@code numberString}, otherwise {@code -1.0}
	  */
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
	 
	 
	 /**
	  * This method is used to get the total cases per million of a continent on different dates.
	  *
	  * @return {@code LinkedHashMap} This returns a key:value map in the form of {date : {continent: date of first total cases record}}.
	  */
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
	 
	 /**
	  * This method validates that the first date is not after the second date
	  *
	  * @param dateFrom This is the first date
	  * @param dateTo This is the second date
	  * @return {@code boolean} This returns {@code true} if the {@code dateFrom} is not after the {@code dateTo}, otherwise {@code false}
	  */
	public static boolean validateDateInOrder(LocalDate dateFrom, LocalDate dateTo) {
	    	if (dateFrom.isEqual(dateTo) || (dateFrom.isBefore(dateTo))) {
	    		return true;
	    	}
	    	return false;
	}
	
	/**
	  * This method validates that a date is with a period.
	  * @param date This is the date being validated
	  * @param minDate This is the lower bound of the period
	  * @param maxDate This is the upper bound of the period
	  * @return {@code boolean} This returns {@code true} if the {@code date} is between {@code minDate} and {@code maxDate} inclusive, otherwise {@code false}
	  */
	public static boolean validateDateInRange(LocalDate date, LocalDate minDate, LocalDate maxDate) {
    	if (isBetween(date, minDate, maxDate)) {
    		return true;
    	}
    	return false;
	}
	
	/**
	  * This method validates the size of list is greater than 0
	  * @param size This is the size of the list
	  * @return {@code boolean} This returns {@code true} if the {@code size} is greater than 0, otherwise {@code false}
	  */
	public static boolean validateSize(int size) {
		if (size > 0) {
    		return true;
		}
		return false;
	}
	
	/**
	  * This method returns a color corresponding to the total confirmed cases per million
	  * @param confirmedCases This is total confirmed cases per million
	  * @return {@code Color} This returns a {@code Color} object with corresponding r,g,b value
	  */
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