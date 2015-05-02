!go_to_work.

+!go_to_work : true
	<- .wait("+checkpoint_ready");
	!open_office;
	!add_default_norms;
	.print("Enforcer ready. Waiting for reports.");
	.broadcast(tell, office_ready).

+!open_office : true
	<- ?prepare_table(R);
	?find_rulebook(E).

+?prepare_table(R) : true
	<- lookupArtifact("reporting_interface",R);
	focus(R).
-?prepare_table(R) : true
	<- .wait(100);
	?prepare_table(R).

+?find_rulebook(E) : true
	<- lookupArtifact("enforcement_interface", E);
	focus(E).
-?find_rulebook(E) : true
	<- .wait(100);
	?find_rulebook(E).

+!add_default_norms
	<- createNorm(prohibition, action, "[(not valid(X))]", acceptPassport(X), sanction(10), "def-01");
	activateNorm("def-01");
	createNorm(obligation, action, "[valid(X)]", acceptPassport(X), sanction(5), "def-02");
	activateNorm("def-02").
	
+newReport
	<- takeReport.

+got_report(A)
	<- detectViolation(A).
	
+violation(Agent, Norm, NormId) : true
	<- .print("Detected violation of norm ", NormId, " by agent ", Agent);
	sanction(Agent, Norm).