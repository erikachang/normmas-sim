package normmas.artifacts;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import normmas.ActionRecord;

public class ReportingArtifact extends Artifact {
	private LinkedBlockingQueue<ActionRecord> pendingReports;
	
	protected Logger logger = Logger.getLogger("Reporting Artifact");
	
	void init() {
		pendingReports = new LinkedBlockingQueue<ActionRecord>();

		logger.info("ReportingArtifact created.");
	}
	
	@OPERATION
	synchronized void fileReport(Object record) {
		try {
			pendingReports.put((ActionRecord)record);
			signal("new_report");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@OPERATION	
	synchronized void takeReport(OpFeedbackParam<ActionRecord> report) {
		ActionRecord nextRecord = pendingReports.poll();
		if (nextRecord != null) {
//			signal(getOpUserId(), "got_report", nextRecord);
			report.set(nextRecord);
		} else {
			failed("No new reports.");
		}
	}
}

