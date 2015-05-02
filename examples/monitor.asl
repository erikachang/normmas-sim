!go_to_work.

+!go_to_work : true
	<- .wait("+office_ready");
	!station;
	?monitoring_polling_interval(X);
	!read(X);
	.println("Monitor ready! Starting activity...");
	.broadcast(tell, station_ready).

+!station
	<- ?punch_clock(M);
	?greet_enforcers(R);
	?monitoring_strategy(X);
	?monitoring_intensity(Y);
	.println("Initiating with strategy: ", X);
	.println("Monitoring Intensity set to: ", Y).

+?punch_clock(M) : true
	<- lookupArtifact("monitoring_interface", M);
	focus(M);.
-?punch_clock(M) : true
	<- .wait(100);
	?punch_clock(M).

+?greet_enforcers(R) : true
	<- lookupArtifact("reporting_interface", R);
	focus(R);.
-?greet_enforcers(R) : true
	<- .wait(100);
	?greet_enforcers(R).

+!read(X)
	<- poll;
	.wait(X);
	!!read(X).
	
+actionAvailable
	<- readAction.
	
+got_record(A)
	<- fileReport(A).