!create_and_use.

+!create_and_use : true
	<- !setupTool;
	.broadcast(tell, monitorReady);
	.wait("+enforcerReady");
	?monitoring_polling_interval(X);
	!read(X);
	.println("Monitor ready! Starting activity...").

+!setupTool
	<- makeArtifact("mon","normmas.artifacts.MonitoringArtifact", [], Id);
	focus(Id);
	makeArtifact("desk", "normmas.artifacts.ReportingArtifact", [], _);
	?monitoring_strategy(X);
	?monitoring_intensity(Y);
	.println("Initiating with strategy: ", X);
	.println("Monitoring Intensity set to: ", Y).
	
+!read(X)
	<- poll;
	.wait(X);
	!!read(X).
	
+actionAvailable
	<- readAction(A);
	fileReport(A).