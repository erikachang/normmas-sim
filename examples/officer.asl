credits(0).
goal(50).
working.

!work.

+payment(S) 
	<-	?credits(C);
		Y = C+S;
		-+credits(Y);
		.my_name(Me);
		.concat("Credits for ", Me, Stat);
		updateStat(Stat, Y);
		.print("Received credits for work. Current total: ", (Y)).

+!work  
  	<-	.wait("+normsReady");
	  	!openBooth(Booth);
	  	focus(Booth);
	  	?subject_to_norms(Id);
		focus(Id); 
		register;
	    !receivePassports.

+!openBooth(Booth) 
	<-	.my_name(Me);
		makeArtifact(Me, "examples.java.ImmigrationBooth", [], Booth).

+?subject_to_norms(Id)
	<-	lookupArtifact("enforcement_interface", Id).
	
-?subject_to_norms(Id)
	<-	.wait(100);
		?subject_to_norms(Id).

+!receivePassports 
	<-	.wait(2000);
		receivePassport(Passport);
		!checkPassport(Passport);
		!!receivePassports.

+!checkPassport(Passport)
	: valid(Passport)
	<-	acceptPassport(Passport);
		.print("Passport approved.").

+!checkPassport(Passport) 
	: not valid(Passport)
  	<-	rejectPassport(Passport);
		.print("Passport rejected.").

