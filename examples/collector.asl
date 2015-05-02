// Agent collector in project goldminers

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start
	<- makeArtifact(stats, "normmas.artifacts.StatisticsArtifact", [], Id);
	!work.

+!work : true 
	<- .wait(5000);
	dumpStatistics;
	!work.
