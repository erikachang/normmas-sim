package normmas.artifacts;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import normmas.ActionRecord;
import cartago.Artifact;
import cartago.OPERATION;

public class ReportingArtifact extends Artifact {
	private LinkedBlockingQueue<ActionRecord> pendingReports;
	
	protected Logger logger = Logger.getLogger("Reporting Artifact");
	
	void init() {
		pendingReports = new LinkedBlockingQueue<ActionRecord>();

		logger.info("ReportingArtifact created.");
	}
	
	@OPERATION
	void fileReport(Object record) {
		try {
			pendingReports.put((ActionRecord)record);
			signal("newReport");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@OPERATION	
	void takeReport() {
		ActionRecord nextRecord = pendingReports.poll();
		if (nextRecord != null) {
			signal(getOpUserId(), "got_report", nextRecord);
		}
	}
}

