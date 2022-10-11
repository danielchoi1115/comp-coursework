package comp3111.covid;

public class Testing{
	public static void main(String arg[]) {
		System.out.println("Hello, JUnit 4!");	
	}
	
	public static int multiply(int n, int m) {
		return n*m;
	}
	
	public static boolean isTrue() {
		return true;
	}
	
	public static boolean isEvenNumber(int num) {
		if ((num % 2) == 0)
			return true;
		else
			return false;
	}
	
}