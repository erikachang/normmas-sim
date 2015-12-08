package example.immigration;

import java.util.Random;

public class Passport {
	
	static int PASSPORT_ID = 0;
	
	private String holder;
	private int id;
	private PassportGrade grade;
	
	public Passport(String holder) {
		this.id = PASSPORT_ID++;
		this.holder = holder;
		int random = new Random().nextInt(100);
		
		if (random < 20) {
			grade = PassportGrade.A;
		} else if (random < 40) {
			grade = PassportGrade.B;
		} else if (random < 60) {
			grade = PassportGrade.C;
		} else if (random < 80) {
			grade = PassportGrade.D;
		} else {
			grade = PassportGrade.E;
		}
	}
	
	public String getHolder() {
		return this.holder;
	}
	
	public PassportGrade getGrade() {
		return this.grade;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String toString() {
		return "Passport #" + this.id;
	}
}
