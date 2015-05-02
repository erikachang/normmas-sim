package examples.java;

import java.util.Random;

public class Passport {
	
	static int PASSPORT_ID = 0;
	
	private int id;
	private boolean valid;
	
	public Passport() {
		this.id = PASSPORT_ID++;
		int random = new Random().nextInt(100);
		valid = random < 50;
	}
	
	public boolean isValid() {
		return this.valid;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String toString() {
		return "Passport #" + this.id;
	}
}
