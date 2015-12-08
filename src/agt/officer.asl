acceptableGrades(["A", "C"]).
monitor_intensity(100).

+!booth_open
	<- .wait("+station_open");
	.my_name(Me);
	makeArtifact(Me, "example.immigration.ImmigrationBoothArtifact", [], Booth);
	+at_booth(Me);
	focus(Booth);
	register;
	.print("Officer at Booth ", Me, " ready!");
	!!immigrants_attended.

+!clean_temporary_percepts
    <- for ( passport(PID, G) ) {
        -passport(PID,G);
    };
    for ( immigrant_at_booth(I) ) {
        -immigrant_at_booth(I);
    };
    for (valid(PID)) {
        -valid(PID);
    }
    for (~valid(PID)) {
        -~valid(PID);
    }. 

+!immigrants_attended
	: at_booth(AtBoothID)
	<- !clean_temporary_percepts;
    .print("Next!");
	call_next(AtBoothID);
	.wait("+passport", 1000);
	!passport_analyzed.
-!immigrants_attended[error(wait_timeout)]
    : agentsInQueue(Q) & Q > 0
    <- !immigrants_attended.
-!immigrants_attended
    <- .print("My work here is done!").

+!passport_analyzed
    : passport(PID, Grade) & immigrant_at_booth(I) & acceptableGrades(List) & .member(Grade ,List) & .random(R) & R > 0.1
	<- .print("Passport ", PID, " is valid."); 
    +valid(PID);
	accept_passport(PID, Grade);
	.send(I, tell, passport_accepted);
    .print("Accepting passport ", PID, ".");
    !immigrants_attended;
    .
+!passport_analyzed
    : passport(PID, Grade) & immigrant_at_booth(I) & acceptableGrades(List) & .member(Grade ,List)
    <- .print("Passport ", PID, " is valid."); 
    +valid(PID);
    reject_passport(PID, Grade);
    .send(I, tell, passport_accepted);
    .print("Rejecting passport ", PID, ".");
    !immigrants_attended;
    .
+!passport_analyzed
    : passport(PID, G) & immigrant_at_booth(I) 
    <- .print("Passport ", PID, " is invalid.");
    +~valid(PID);
    reject_passport(PID, G);
    .send(I, tell, passport_rejected);
    .print("Rejecting passport ", PID, ".");
    .wait("+bribe", 1000);
    .wait("+bribe_dealt_with");
    !immigrants_attended;
    .
-!passport_analyzed
    : passport(PID, G) & immigrant_at_booth(I)
    <- !immigrants_attended;
    .

+greet[source(Agent)]
    <- -+immigrant_at_booth(Agent).

+bribe(V)[source(Agent)]
    : bribe_threshold(T) & V >= T
    <- .print("Yes, yes... why don't we?");
    .send(Agent, tell, bribe_answer(accepted));
//    ?credits(C);
//    Y = C + V;
//    -+credits(Y);
    ?passport(PID, G);
    accept_passport(PID, G);
    .my_name(Me);
    .concat("Credits for ", Me, Stat);
    .print("Now off you go, you crazy bird.");
    +bribe_dealt_with.
+bribe(V)[source(Agent)]
    : bribe_threshold(T) & V < T
    <- .print("There's no different way to do this. Now, off you go!");
    .send(Agent, tell, bribe_answer(rejected));
    +bribe_dealt_with.

@bribe_payment[atomic]
+bribe_payment
    <- ?balance(Y);
    .print("Received credits from bribe. Current total: ", (Y)).
@payment[atomic]
+payment
	: balance(C)
	<- .my_name(Me);
	.print("Received credits for work. Current total: ", (C)).
		
@sanction[atomic]
+sanction(S)
    : balance(C)
    <- Y = C - S;
    -+balance(Y);
    .print("Ugh! They've found me. I now have only: ", Y, " credits...").

+normActivated("bribe")
    : not honest(Me)
    <- ?monitor_intensity(M);
    checkNormIntegerSanctionValue("bribe", S);
    X = ((M/100) * S) + 1;
    .print("Perceived norm activation with sanction value of ", S, ". My bribe threshold is ", X);
    -+bribe_threshold(X).
		
{ include("inc/common-cartago.asl") }
