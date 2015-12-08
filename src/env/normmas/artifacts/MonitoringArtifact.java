// CArtAgO artifact code for project enmass

package normmas.artifacts;

import java.util.Random;
import java.util.logging.Logger;

import cartago.Artifact;
import cartago.OPERATION;
import cartago.OpFeedbackParam;
import normmas.ActionHistory;
import normmas.ActionRecord;
import normmas.ConfigurationManager;
import normmas.HashNormBase;
import normmas.MonitoringStrategy;
import normmas.StatisticsBase;

public class MonitoringArtifact extends Artifact {
	protected int monitoringIntensity;// = Integer.parseInt(ConfigurationManager.readConfig("monitoring_intensity"));
	protected MonitoringStrategy monitoringStrategy;// = MonitoringStrategy.valueOf(ConfigurationManager.readConfig("monitoring_strategy"));
	protected int pollingInterval;// = Integer.parseInt(ConfigurationManager.readConfig("monitoring_polling_interval"));
	
	protected ActionHistory history;
	protected Logger logger = Logger.getLogger("MonitoringArtifact");
	protected int actionsRead;
	
	// TODO: Restore statBase usage
	@SuppressWarnings("unused")
	private final StatisticsBase statBase = StatisticsBase.getInstance();
	
	void init(int intensity, String strategy, int polling) {
		history = ActionHistory.getInstance();
		
		actionsRead = 0;
		
//		String monitorName = this.getCreatorId().getAgentName();
//		String statName = monitorName.concat(" Intensity");
//		statBase.addOrUpdateStat(statName, monitoringIntensity);
		
		this.monitoringIntensity = intensity;
		this.monitoringStrategy = MonitoringStrategy.valueOf(strategy);
		this.pollingInterval = polling;
		
		defineObsProperty("monitoring_intensity", intensity);
		defineObsProperty("monitoring_strategy", MonitoringStrategy.valueOf(strategy));
		defineObsProperty("monitoring_polling_interval", polling);
		
		logger.info("MonitoringArtifact created.");
	}
	
	@OPERATION
	void setStrategy(String strategyName) {
		strategyName = strategyName.toUpperCase();
		
		boolean updated = ConfigurationManager.updateConfig("monitoring_strategy", strategyName);
		if (updated) {
			logger.info("Setting monitoring strategy to \"" + strategyName + "\".");
			this.monitoringStrategy = MonitoringStrategy.valueOf(strategyName);
		}
	}
	
	@OPERATION
	void setIntensity(int intensity) {
		boolean updated = ConfigurationManager.updateConfig("monitoring_intensity", Integer.toString(intensity));
		
		if (updated) {
			logger.info("Setting monitoring intensity to " + intensity + ".");
			this.monitoringIntensity = intensity;
		}
	}
	
	@OPERATION
	synchronized void poll(OpFeedbackParam<ActionRecord> actionRecord) {
		ActionHistory history = ActionHistory.getInstance();
		if (history.hasNext()) {
			if (this.monitoringStrategy == MonitoringStrategy.PROBABILISTIC) {
				int read = new Random().nextInt(100);
				if (read < monitoringIntensity) {
					this.actionsRead++;
//					signal("actionAvailable");
					actionRecord.set(history.readNext());
				} else {
					history.dispose();
				}
			} else if (this.monitoringStrategy == MonitoringStrategy.ENFORCED_ACTIONS_ONLY) {
				ActionRecord record = history.preview();
				if (HashNormBase.getInstance().isActionEnforced(record.getAction().getName())) {
					this.actionsRead++;
//					signal("actionAvailable");
					actionRecord.set(history.readNext());
				} else {
					history.dispose();
				}
			} else if (this.monitoringStrategy == MonitoringStrategy.ENFORCED_PROBABILISTIC) {
				ActionRecord record = history.preview();
				if (HashNormBase.getInstance().isActionEnforced(record.getAction().getName())) {
					int read = new Random().nextInt(100);
					if (read < monitoringIntensity) {
						this.actionsRead++;
//						signal("actionAvailable");
						actionRecord.set(history.readNext());
					} else {
						history.dispose();
					}
				} else {
					history.dispose();
				}
			} else {
				logger.warning("No Monitoring Strategy configured! Agents will not be monitored.");
			}
		} else {
			this.failed("No actions observed.");
		}
	}
	
	@OPERATION
	synchronized void readAction() {
		ActionRecord record = history.readNext();
		
		if (record != null) {
			signal(getOpUserId(), "got_record", record);
		}
	}
}

