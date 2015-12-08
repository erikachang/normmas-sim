// Agent immigrant in project checkpoint

/* Initial beliefs and rules */

/* Initial goals */
!immigrated.
/* Plans */

+!immigrated
    <- !money_checked;
    !bribe_considered;
    !passport_acquired;
    enter_queue;
    !passport_checked.

+!money_checked
    <- .random(MoneyGen);
    M = MoneyGen * 20;
    +have_money(M).

+!bribe_considered
    <- .random(Value);
    B = (Value * 10) + 5;
    +bribe_value(B).

+!passport_acquired
    <- 
    .print("Requesting passport...");
    request_passport(Passport);
    +holding(Passport);
    .print("Passport acquired. Entering queue.").

@i_am_next[atomic]
+!passport_checked
    : holding(Passport)
    <- .print("Waiting in line...");
    .wait("+i_am_next");
    ?nextBooth(BoothID);
    lookupArtifact(BoothID, Booth);
    focus(Booth);
    +at_booth(BoothID);
    .print("Arrived at Booth ", BoothID);
    .send(BoothID, tell, greet);
    hand_passport(Passport);
    -holding(Passport);
    .print("Handed passport in.").
    
+passport_accepted
    : .my_name(Me)
    <- .print("Thank you Mr. Officer!").
+passport_rejected
    : at_booth(BoothID) & bribe_value(V) & have_money(M) & V < M
    <- .print("My passport got rejected? How about we do this differently, Mr. Officer?");
    .send(BoothID, tell, bribe(V));.
//    .wait("+bribe_answer");
//    !bribe_attempted.
+passport_rejected
    <- .print("Shoot! Passport was rejected and I have no money for a bribe...").
    
//+!bribe_attempted
//    : bribe_answer(Answer) & Answer == "accepted"
+bribe_answer(accepted)
    <- .print("yay");
    .my_name(Me);
    ?bribe_value(V);
    ?have_money(M);
    N = M - V;
    -+have_money(N);
    pay_bribe(V).
//+!bribe_attempted
//    : bribe_answer(Answer) & Answer == "rejected"
+bribe_answer(rejected)
    <- .print("caught in the act!");
    .my_name(Me).
    

{ include("inc/common-cartago.asl") }
