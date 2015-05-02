!open_checkpoint.

+!open_checkpoint : true
	<- !establish_monitoring_system;
	!establish_reporting_system;
	!establish_enforcement_system;
	.broadcast(tell, checkpoint_ready).

+!establish_monitoring_system : true
	<- makeArtifact("monitoring_interface", "normmas.artifacts.MonitoringArtifact", [], M).
+!establish_reporting_system : true
	<- makeArtifact("reporting_interface", "normmas.artifacts.ReportingArtifact", [], R).
+!establish_enforcement_system : true
	<- makeArtifact("enforcement_interface", "normmas.artifacts.NormativeArtifact", [], E).
