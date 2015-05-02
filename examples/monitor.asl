!go_to_work.

+!go_to_work : true
	<- .wait("+checkpoint_ready");
	!station;
	?monitoring_polling_interval(X);
	!read(X);
	.println("Monitor ready! Starting activity...").

+!station
	<- ?punch_clock(M);
	focus(M);
	?greet_enforcers(R);
	focus(R);
	?monitoring_strategy(X);
	?monitoring_intensity(Y);
	.println("Initiating with strategy: ", X);
	.println("Monitoring Intensity set to: ", Y).

+?punch_clock(M) : true
	<- lookupArtifact("monitoring_interface", M).
	
-?punch_clock(M) : true
	<- .wait(100);
	?punch_clock(M).

+?greet_enforcers(R) : true
	<- lookupArtifact("reporting_interface", R).
	
-?greet_enforcers(R) : true
	<- .wait(100);
	?greet_enforcers(R).

+!read(X)
	<- poll;
	.wait(X);
	!!read(X).
	
+actionAvailable
	<- readAction(A);
	fileReport(A).