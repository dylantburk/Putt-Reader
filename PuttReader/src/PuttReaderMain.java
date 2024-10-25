import java.util.Scanner;

public class PuttReaderMain {
	private static boolean holed; // have you made the putt
	private static float puttDist; // how far away your ball is from the hole (feet)
	private static float elevCh; // how much uphill or downhill your putt is (from ball to hole) (inches)
	private static float greenAng; //how much the green slopes from right to left (left to right is negative) (degrees)
	private static float stimp = 1; // how fast the ball rolls (<1 is faster, >1 is slower) 
	private static float power; // adjusted distance considering stimp and elevation 
	private static float missDist; // how far short/long away your putt ended up from the hole (long is positive) (inches)
	private static float missAng; // the angle at which your putt missed 
	private static float angleMult = 1; // used to adjust how angle is calculated based on recorded misses
	private static float miss; //how far left/right your putt ended up from the hole (right is positive) (inches)
	
	
	public PuttReaderMain() { //starts the program
		holed = false;
		while (holed == false) { //stops running once you have made a putt
			Scanner scanner = new Scanner(System.in);
			System.out.println("Input the length of the putt in feet: ");
			puttDist = scanner.nextFloat();
			System.out.println("Input the elevation change of the putt in inches: ");
			elevCh = scanner.nextFloat();
			calcPower();
			System.out.println("Input the angle of the green: (positive angle means higher on the right)");
			greenAng = scanner.nextFloat();
			System.out.println(calculatePutt() + "\nDid you make the putt? (1 for yes 0 for no)");
			if (scanner.nextInt() == 0) {
				holed = false;
				System.out.println("how long was the putt in feet: (positive means past the hole, negative means short)");
				missDist = scanner.nextFloat();
				adjustStimp();
				System.out.println("how far off was the putt in inches: (positive means missed right, negative means left)");
				miss = scanner.nextFloat();
				adjustAngle();
			}
			else {
				holed = true;
			}
		}
	}
	public static void calcPower(){ //calculates how hard to hit ball
		float pow = 0;
		if (elevCh > -1 && elevCh < 1) { //if elevation is within 1 inch, disregard
			power = puttDist * stimp;
		}
		else {
			pow = puttDist + (elevCh/2); //increase / decrease power
		}
		power = pow * stimp; // consider stimp
	}
	public static String calcBreak(float power, float greenAng) { //calculates how far left or right to aim
		float angle = angleMult*(greenAng); 
		float aim = (puttDist*angle)/2; // for every degree of angle, the putt will break 1/2 an inch per foot
		if(power <= 3) { //if the adjusted distance is less than 3 feet do not aim outside of the hole
			if(angle > 0) { // will break right to left
				return "at the right edge";
			}
			else if(angle < 0){ // will break left to right
				return "at the left edge";
			}
			else { // no break
				return "straight";
			}
		}
		else { //adjusted distance is long enough to aim outside of hole
			if (angle > 0) { // breaks right to left
				return (aim/4.25) + " holes to the right"; //golf hole is 4.25 inches wide
			}
			else if(angle < 0) { // breaks left to right
				return (-aim/4.25) + " holes to the left";
			}
			else { // no break
				return "straight"; 
			}
		}
	}
	public static void adjustStimp() { //changes stimp based on miss length
		if (missDist > 0 ) { // missed long
			stimp -= (float) (missDist / puttDist); // decrease stimp
		}
		else if (missDist < 0){ // missed short
			stimp += (float) (-missDist / puttDist); //increase stimp
		}
	}
	public static void adjustAngle() { //changes angle calculation based on miss direction
		missAng = (2 * miss) / puttDist; //get miss angle from miss distance
		if (miss == 0) { // aim point was correct
			return; //do not change angle calculations
		}
		else if (greenAng > 0) { //breaks right to left
			if(missAng > 0) { //missed to right
				angleMult -= 1 - (1/missAng); //decrease angle multiplier
			}
			else{ //missed to left
				angleMult += 1 + (1/missAng); //increase angle multiplier
			}
		}
		else if (greenAng < 0) { // breaks left to right
			if(missAng < 0) { // missed to left
				angleMult -= 1 + (1/missAng); //decrease angle multiplier

			}
			else { // missed to right
				angleMult += 1 - (1/missAng); //increase angle multiplier

			}
		}	
	}
	public static String calculatePutt() { //puts together string to tell user where to aim
		return "hit the putt about " + power + " feet and aim " + calcBreak(power, greenAng);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new PuttReaderMain();	//constructor
	}

}