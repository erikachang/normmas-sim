!at_work.

+!at_work
	<- .wait("+office_open");
	?stationed(Strategy, Intensity, Interval);
	.println("Initiating with strategy: ", Strategy);
	.println("Monitoring Intensity set to: ", Intensity);
	.println("Will check for new actions every ", Interval, "ms");
	.println("Monitor ready! Starting activity...");
	.broadcast(tell, monitor_intensity(Intensity));
	.broadcast(tell, station_open);
	!!actions_observed.

+?stationed(Strategy, Intensity, Interval)
	<- ?monitoring_strategy(Strategy);
	?monitoring_intensity(Intensity);
	?monitoring_polling_interval(Interval).
-?stationed(S, I, In)
	<- .wait(100);
	?stationed(S, I, In).
	
+!actions_observed
	<- poll(Action);
	+action_to_report(Action);
	!!actions_observed.
-!actions_observed
    : monitoring_polling_interval(Interval)
    <- .wait(Interval);
    !!actions_observed.
//-!actions_observed
//	: monitoring_polling_interval(Interval) & agentsInQueue(Q) & Q > 0
//	<- .wait(Interval);
//	!actions_observed.
//-!actions_observed
//    : agentsInQueue(Q) & Q <= 0
//	<- .print("no more actions");
//	.
	
+action_to_report(Action)
    <- fileReport(Action).
	
//+!reports_filed
//	<- for ( action_to_report(Action)[source(self)] ) {
//           fileReport(Action);
//       }.
	
//+actionAvailable
//	<- readAction.
//	
//+got_record(A)
//	<- fileReport(A).
	
{ include("inc/common-cartago.asl") }