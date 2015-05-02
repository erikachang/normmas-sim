credits(0).
goal(50).
penalty(0).
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
	<- 	lookupArtifact("enforcement_interface", Id);
		focus(Id);
		register.
-!punch_the_clock
	<- 	.wait(100);
		!punch_the_clock.

+!receive_passports 
	: suspended
	<- 	.wait("-suspended");
		!receive_passports.
+!receive_passports
	<- 	.wait(2000);
	  	receivePassport(Passport);
		!check_passport(Passport);
		!!receive_passports.

+!check_passport(Passport) 
	: valid(Passport)
	<- 	acceptPassport(Passport);
		.print("Passport approved.").

+!check_passport(Passport) 
	: not valid(Passport)
	<- 	acceptPassport(Passport);
		incStat("Violations");
		.print("Passport should've been rejected, but was rather approved since I need the credits.").
	
+sanction(X)
	<- 	?credits(C);
		Y = C - X;
		-+credits(Y);
		?penalty(P);
		T = P + X;
		-+penalty(T);
		.print("Oops! I've been discovered and lost ", X, " credits for my transgression.");
		-working;
		+suspended.
	
+suspended
	<- 	.my_name(Me);
		.print(Me, " suspended from action."); 
		.wait(10000);
		-suspended;
		.print(Me, " resuming activity.");
		+working. 
		
+payment(S)
	<-	?credits(C);
		Y = C+S;
		-+credits(Y);
		.my_name(Me);
		.concat("Credits for ", Me, Stat);
		updateStat(Stat, Y);
		.print("Received credits for work. Current total: ", (Y)).