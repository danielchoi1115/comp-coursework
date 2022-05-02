package comp3111.covid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import edu.duke.FileResource;


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
	 
	 public static String getRateOfTotalDeaths(String date, List<String> allCountry) {
			String oReport = "";	
		  for(String continent:allCountry) {
			    Double fullyVaccinated = 0D;
				long totalNumRecord = 0;
				Double population = 0D;
			for (CSVRecord rec : getFileParser("COVID_Dataset_v1.0.csv")) {
				if (!rec.get("iso_code").equals("")) {
					String s0=rec.get("date");
					String c1=rec.get("location");
				    String cparam=continent.split("-")[1].trim();
				if(date==null || s0.equals(date) && cparam.equals(c1.trim())) {	
					String s1 = rec.get("total_deaths");
					String s2 = rec.get("total_deaths_per_million");		
					if (!s1.equals("")) {
						fullyVaccinated = Double.parseDouble(s1);
						fullyVaccinated=fullyVaccinated+fullyVaccinated;
					}
					if (!s2.equals("")) {
						population = Double.parseDouble(s2);
						population=population+population;
					}
					
					totalNumRecord++;
				}		
				
				}
			}
		
			oReport += "----------------"+continent+"----------------------\n";
			oReport += String.format("date (%s): %,d Records\n\n", date, totalNumRecord);
			oReport += String.format("[continent (%s)]\n", continent);
			oReport += "Number of total_deaths : "+fullyVaccinated.toString()+"\n";
			oReport += "total_deaths_per_million: "+population.toString()+"\n";
			//oReport += "----------------------------------------------------\n";
			}
		    return oReport;
	 }
	 
	 
	 public static Map<String,Double>  getRateOfChart(String sdate,String edate,List<String> allCountry) {
			String format="yyyy-MM-dd";
			 long lsdate=gettimeStemp(sdate,format); 
			 long ledate=gettimeStemp(edate,format);
			 Map<String,Double> map=new HashMap<String,Double>();		
			for(String continent:allCountry) {
				 Double population = 0D;
				 Double fullyVaccinated = 0D;
				 long totalNumRecord = 0;
			for (CSVRecord rec : getFileParser("COVID_Dataset_v1.0.csv")) {
				if (!rec.get("iso_code").equals("")) {
					String s0=rec.get("date");
					String c1=rec.get("location");
				    String cparam=continent.split("-")[1].trim();
				     //2/24/2020
				    String[] arrayDate=s0.split("/");
				    s0=arrayDate[2]+"-"+(Integer.parseInt(arrayDate[0])<10?"0"+arrayDate[0]:arrayDate[0])+"-"+(Integer.parseInt(arrayDate[1])<10?"0"+arrayDate[1]:arrayDate[1]);
				    long ldate=gettimeStemp(s0,format);   
				if(lsdate<=ldate && ldate<=ledate && cparam.equals(c1.trim())) {	
					String s1 = rec.get("total_deaths");
					String s2 = rec.get("total_deaths_per_million");		
					if (!s1.equals("")) {
						fullyVaccinated = Double.parseDouble(s1);
					}
					if (!s2.equals("")) {
						population = Double.parseDouble(s2);
						Double dd=map.get(cparam);
						if(dd==null) {
						map.put(cparam, population);
						}else {
							dd=dd+population;
							map.put(cparam, dd);	
						}
					}
					
					totalNumRecord++;
				}		
				
				}
			  }
			}
			
			return map;
	 }
	 
	 
	 public static long gettimeStemp(String time, String format) {
		 
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			if (!"".equals(format)) {
				simpleDateFormat = new SimpleDateFormat(format);
			}
	 
			long timeStemp = 0;
			try {
	 
				Date date = simpleDateFormat.parse(time);
	 
				timeStemp = date.getTime();
	 
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return timeStemp;
		}
 

}