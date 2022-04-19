package comp3111.covid;

public class ConfirmedCase {
	String countryName;
	String totalCases;
	String totalCasesPerM;
	
	public ConfirmedCase(String countryName, String totalCases, String totalCasesPerM) {
		this.countryName = countryName;
		this.totalCases = totalCases;
		this.totalCasesPerM = totalCasesPerM;
	}
	public String getCountryName() { return countryName; }
	public String getTotalCases() { return totalCases; }
	public String getTotalCasesPerM() { return totalCasesPerM; }
}
