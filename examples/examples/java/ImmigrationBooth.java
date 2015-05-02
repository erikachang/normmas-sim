package examples.java;

import java.util.LinkedList;
import java.util.List;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;

public class ImmigrationBooth extends Artifact {
	
	private List<Passport> acceptedPassports, rejectedPassports;
	
	void init() {
		acceptedPassports = new LinkedList<Passport>();
		rejectedPassports = new LinkedList<Passport>();
	}
		
	@OPERATION
	void receivePassport(OpFeedbackParam<Passport> res) {
		if (this.hasObsProperty("valid"))
			this.removeObsProperty("valid");
		
		Passport passport = new Passport();
		res.set(passport);
		
		if (passport.isValid()) {
			defineObsProperty("valid", passport);
		}
	}
	
	@OPERATION
	void acceptPassport(Passport passport) {
		acceptedPassports.add(passport);
		
		signal("payment", 5);
		
		if (this.hasObsProperty("valid"))
			this.removeObsProperty("valid");
	}
	
	@OPERATION
	void rejectPassport(Passport passport) {
		rejectedPassports.add(passport);
		
		if (this.hasObsProperty("valid"))
			this.removeObsProperty("valid");
	}
}
