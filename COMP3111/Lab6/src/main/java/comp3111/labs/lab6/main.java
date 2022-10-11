package comp3111.labs.lab6;

/*
 * note the bad coding style (deliberately written for debugging exercise) 
 */
public class main {
	public static void main(String[] args) {
		Animal animals[] = new Animal[10];
//		original code
//		for (Animal a : animals)
//			a = new Animal();

//		fixed code
//		HOW: use 'for' instead of 'for each' 
		for (int i = 0; i < animals.length; i++) {
			animals[i] = new Animal();	
		}
		for (int iii = 0; iii < 10; iii++) {
			int ii = 0;
//			                   WHERE: The bug was here. When entering animals[iii] where iii = 0
			for (; ii < 100 && animals[iii].isAlive() ; ii++) {

//				WHY: Values in array cannot be modified using 'for each' loop in JAVA 
//				Therefore, nothing was assigned in animals[iii]
//				and thus calling isAlive() method on a null object raised an error
				
				System.out.print(animals[iii].getWeight() + " ");
				animals[iii].eat();
				if (ii % 3 == 0)
				animals[iii].poo();
			}
		}	
	}
}
