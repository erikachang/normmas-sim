// CArtAgO artifact code for project enmass

package normmas.artifacts;

import java.util.Random;
import java.util.logging.Logger;

import normmas.ActionHistory;
import normmas.ActionRecord;
import normmas.ConfigurationManager;
import normmas.HashNormBase;
import normmas.MonitoringStrategy;
import normmas.StatisticsBase;
import cartago.Artifact;
import cartago.GUARD;
import cartago.OPERATION;
import cartago.OpFeedbackParam;

public class MonitoringArtifact extends Artifact {
	protected int monitoringIntensity = Integer.parseInt(ConfigurationManager.readConfig("monitoring_intensity"));
	protected MonitoringStrategy monitoringStrategy = MonitoringStrategy.valueOf(ConfigurationManager.readConfig("monitoring_strategy"));
	protected int pollingInterval = Integer.parseInt(ConfigurationManager.readConfig("monitoring_polling_interval"));
	
	protected ActionHistory history;
	protected Logger logger = Logger.getLogger("MonitoringArtifact");
	protected int actionsRead;
	
	private final StatisticsBase statBase = StatisticsBase.getInstance();
	
	void init() {
		history = ActionHistory.getInstance();
		
		actionsRead = 0;
		logger.info("MonitoringArtifact created.");
		
		String monitorName = this.getCreatorId().getAgentName();
		String statName = monitorName.concat(" Intensity");
		statBase.addOrUpdateStat(statName, monitoringIntensity);
		
		defineObsProperty("monitoring_intensity", this.monitoringIntensity);
		defineObsProperty("monitoring_strategy", this.monitoringStrategy.name());
		defineObsProperty("monitoring_polling_interval", this.pollingInterval);
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
	void poll() {
		ActionHistory history = ActionHistory.getInstance();
		if (history.hasNext()) {
			if (this.monitoringStrategy == MonitoringStrategy.PROBABILISTIC) {
				int read = new Random().nextInt(100);
				if (read < monitoringIntensity) {
					this.actionsRead++;
					signal("actionAvailable");
				} else {
					history.dispose();
				}
			} else if (this.monitoringStrategy == MonitoringStrategy.ENFORCED_ACTIONS_ONLY) {
				ActionRecord record = history.preview();
				if (HashNormBase.getInstance().isActionEnforced(record.getAction().getActionTerm().getFunctor())) {
					this.actionsRead++;
					signal("actionAvailable");
				} else {
					history.dispose();
				}
			} else if (this.monitoringStrategy == MonitoringStrategy.ENFORCED_PROBABILISTIC) {
				ActionRecord record = history.preview();
				if (HashNormBase.getInstance().isActionEnforced(record.getAction().getActionTerm().getFunctor())) {
					int read = new Random().nextInt(100);
					if (read < monitoringIntensity) {
						this.actionsRead++;
						signal("actionAvailable");
					} else {
						history.dispose();
					}
				} else {
					history.dispose();
				}
			} else {
				logger.warning("No Monitoring Strategy configured! Agents will not be monitored.");
			}
		}
	}
	
	@OPERATION(guard="actionAvailable")
	void readAction(OpFeedbackParam<ActionRecord> action) {
		action.set(history.readNext());
	}
	
	@GUARD
	boolean actionAvailable(OpFeedbackParam<ActionRecord> action) {
		return history.hasNext();
	}
}

