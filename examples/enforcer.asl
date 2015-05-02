!create_reporting_system.

+!create_reporting_system
	<- .wait("+monitorReady");
	!connect_normative_interface(A);
	focus(A);
	?sit_at_desk(D);
	focus(D);
	!add_default_norms;
	.broadcast(tell, enforcerReady);
	.print("Enforcer ready. Waiting for reports.");
	.broadcast(tell, normsReady);.

+!connect_normative_interface(A)
	<- makeArtifact("norms", "normmas.artifacts.NormativeArtifact", [], A).
	
-!connect_normative_interface(D)
	<- lookupArtifact("norms", D);
	print("Connecting to norms").

+?sit_at_desk(D)
	<- lookupArtifact("desk",D).

-?sit_at_desk(D)
	<- .wait(100);
	?sit_at_desk(D).
	
+newReport
	<- takeReport(A);
	detectViolation(A).
	
+violation(Agent, Norm, NormId)
	<- .print("Detected violation of norm ", NormId, " by agent ", Agent);
	sanction(Agent, Norm).
	
+!add_default_norms
	<- createNorm(prohibition, action, "[(not valid(X))]", acceptPassport(X), sanction(10), "def-01");
	activateNorm("def-01");
	createNorm(obligation, action, "[valid(X)]", acceptPassport(X), sanction(5), "def-02");
	activateNorm("def-02");
	createNorm(obligation, state, "[(not suspended)]", "[working]", sanction(20), "def-03");
	activateNorm("def-03");
	createNorm(prohibition, state, "[suspended]", "[working]", sanction(10), "def-04");
	activateNorm("def-04").