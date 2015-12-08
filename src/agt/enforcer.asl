+!norms_established
	<- .print("creating norms...");
	createNorm(prohibition, action, "[(~valid(ID))]", accept_passport(ID, Grade), sanction(10), "bribe");
	activateNorm("bribe");
	createNorm(obligation, action, "[valid(ID)]", accept_passport(ID, Grade), sanction(5), "wrongful_rejection");
	activateNorm("wrongful_rejection");
    .print("Enforcer ready. Waiting for reports.");
    .broadcast(tell, office_open);
    !!actions_analyzed.
-!norms_established.

+!actions_analyzed
    <- takeReport(Report);
    +report_in_hands(Report);
    !!actions_analyzed;
    .
-!actions_analyzed
    <- .wait(300);
    !!actions_analyzed
    .

+report_in_hands(Report)
    <- detectViolation(Report).

//+!violations_sanctioned
//    <- for (report_in_hands(Report)[source(self)]) {
//           detectViolation(Report);
//       }
//       .
	
+violation(Agent, Norm, NormId) : true
	<- .print("Detected violation of norm ", NormId, " by agent ", Agent);
	lookupArtifact(Agent, ID);
    focus(ID);
    execute_sanction(Norm);
	sanction(Agent, Norm).
	
{ include("inc/common-cartago.asl") }