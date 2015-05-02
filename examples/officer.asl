credits(0).
goal(50).
working.

!go_to_work.

+!go_to_work  
  	<-	.wait("+station_ready");
	  	!punch_the_clock;
	  	!open_booth.

+!open_booth 
	<-	.my_name(Me);
		makeArtifact(Me, "examples.java.ImmigrationBooth", [], Booth);
	  	focus(Booth);
	    !receive_passports.

+!punch_the_clock
	<-	lookupArtifact("enforcement_interface", Id);
		focus(Id);
		register.
-!punch_the_clock
	<-	.wait(100);
		!punch_the_clock.

+!receive_passports 
	<-	.wait(2000);
		receivePassport(Passport);
		!check_passport(Passport);
		!!receive_passports.

+!check_passport(Passport)
	: valid(Passport)
	<-	acceptPassport(Passport);
		.print("Passport approved.").

+!check_passport(Passport) 
	: not valid(Passport)
  	<-	rejectPassport(Passport);
		.print("Passport rejected.").

+payment(S) 
	<-	?credits(C);
		Y = C+S;
		-+credits(Y);
		.my_name(Me);
		.concat("Credits for ", Me, Stat);
		updateStat(Stat, Y);
		.print("Received credits for work. Current total: ", (Y)).