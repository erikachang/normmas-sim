credits(0).
goal(50).
penalty(0).
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
	<- 	lookupArtifact("norms", Id).
	
-?subject_to_norms(Id)
	<- 	.wait(100);
		?subject_to_norms(Id).

+!receivePassports
	: true & not suspended
	<- 	.wait(2000);
	  	receivePassport(Passport);
		!checkPassport(Passport);
		!!receivePassports.

+!receivePassports 
	: suspended
	<- 	.wait("-suspended");
		!receivePassports.

+!checkPassport(Passport) 
	: valid(Passport)
	<- 	acceptPassport(Passport);
		.print("Passport approved.").

+!checkPassport(Passport) 
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
	<- 	.print("suspended"); 
		.wait(10000);
		.print("not suspended");
		-suspended;
		+working. 