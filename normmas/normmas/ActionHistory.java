package normmas;

import jason.asSemantics.ActionExec;
import jason.asSyntax.Literal;

import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class ActionHistory {
	protected Logger logger = Logger.getLogger("ActionHistory");

	protected final StatisticsBase base = StatisticsBase.getInstance();	
	protected int actionsRecorded;	
	protected int lostActionRecords;

	private LinkedBlockingQueue<ActionRecord> history;
	private static ActionHistory instance;
	
	private ActionHistory() {
		history = new LinkedBlockingQueue<ActionRecord>();
		
		this.actionsRecorded = 0;
		this.lostActionRecords = 0;
		
		base.addOrUpdateStat("Actions Recorded", 0);
		base.addOrUpdateStat("Lost Action Records", 0);
		
		logger.info("ActionHistory created.");
	}

	public static ActionHistory getInstance() {
		if (instance == null) {
			instance = new ActionHistory();
		}
		return instance;
	}

	public void record(ActionExec action, String agentName, HashSet<Literal> beliefs) {
		try {
			ActionRecord newRecord = new ActionRecord(action, agentName, beliefs);
			history.put(newRecord);
			actionsRecorded++;
			base.addOrUpdateStat("Actions Recorded", actionsRecorded);
		} catch (InterruptedException ise) {
			logger.warning("The thread attempting to add to the action history has been interrupted.");
		}
	}
	
	public boolean hasNext() {
		return !(history.isEmpty());
	}

	public ActionRecord readNext() {
		try {
			return history.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ActionRecord preview() {
		return history.peek();
	}
	
	public void dispose() {
		try {
			history.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.lostActionRecords++;
		base.updateStat("Lost Action Records", this.lostActionRecords);
	}
}
