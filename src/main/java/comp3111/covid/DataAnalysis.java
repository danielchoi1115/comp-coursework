package comp3111.covid;

import java.util.ArrayList;
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
	 
	 public static LocalDate stringToLocalDate(String dateString) {
		 return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("M/d/yyyy"));
	 }
	 
	 public static String formatNumberWithComma(String numberString) {
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
}