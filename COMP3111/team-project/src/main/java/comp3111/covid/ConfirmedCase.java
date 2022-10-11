package comp3111.covid;

/**
 * 
 * This is a class that will be used in making tables for Task A1.
 * 
 */
public class ConfirmedCase {
	String countryName;
	String totalCases;
	String totalCasesPerM;
	/**
     * Initializer of ConfirmedCase object. 
     * @param countryName name of the country 
     * @param totalCases total confirmed cases on a date
     * @param totalCasesPerM total confirmed cases per million on a date
     */
	public ConfirmedCase(String countryName, String totalCases, String totalCasesPerM) {
		this.countryName = countryName;
		this.totalCases = totalCases;
		this.totalCasesPerM = totalCasesPerM;
	}
	/**
     * This method is used return countryName of ConfirmedCase object. 
     * @return {@code String} countryName.
     */
	public String getCountryName() { return countryName; }
	/**
     * This method is used return totalCases of ConfirmedCase object.
     * @return {@code String} totalCases.
     */
	public String getTotalCases() { return totalCases; }
	/**
     * This method is used return totalCasesPerM of ConfirmedCase object. 
     * @return {@code String} totalCasesPerM.
     */
	public String getTotalCasesPerM() { return totalCasesPerM; }
}
