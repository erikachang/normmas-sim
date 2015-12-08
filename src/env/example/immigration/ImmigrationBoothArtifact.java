package example.immigration;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.sun.istack.internal.logging.Logger;

import cartago.AgentId;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;
import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.bb.BeliefBase;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.RunCentralisedMAS;
import normmas.ActionDescription;
import normmas.ActionHistory;
import normmas.Norm;

public class ImmigrationBoothArtifact extends Artifact {

	private class PassportLogEntry{
		private int passportID;
		private PassportGrade grade;
		
		public PassportLogEntry(int id, PassportGrade grade) {
			passportID = id;
			this.grade = grade;
		}

		public int getPassportID() {
			return passportID;
		}

		@SuppressWarnings("unused")
		public PassportGrade getGrade() {
			return grade;
		}
		
		@Override
		public boolean equals(Object o) {
			return this.passportID == ((PassportLogEntry) o).getPassportID();
		}
	}
	
	private List<PassportLogEntry> acceptedPassports, rejectedPassports;
	
	void init() {
		acceptedPassports = new LinkedList<PassportLogEntry>();
		rejectedPassports = new LinkedList<PassportLogEntry>();
		
		defineObsProperty("balance", 0);
	}
	
	@OPERATION
	void hand_passport(Passport passport) {
		
		ObsProperty obsPassport = getObsProperty("passport");
		if (obsPassport != null) {
			obsPassport.updateValues(passport.getId(), passport.getGrade().toString());
		} else {
			defineObsProperty("passport", passport.getId(), passport.getGrade().toString());
		}
		signal(getCreatorId(), "passport");
	}
		
	@OPERATION
	void accept_passport(int pid, String grade) {		
		PassportLogEntry log = new PassportLogEntry(pid, PassportGrade.valueOf(grade));
		acceptedPassports.add(log);
		
		// If this approval is happening after a bribe.
		if (rejectedPassports.contains(log)) {
			rejectedPassports.remove(log);
		}
		
		ObsProperty balance = getObsProperty("balance");
		balance.updateValue(balance.intValue() + 5);
		
		AgentId officer = getOpUserId();
		signal(officer, "payment");
		
		CentralisedAgArch agent = RunCentralisedMAS.getRunner().getAg(getOpUserName());
		ActionDescription action = new ActionDescription(thisOpId.getOpName());
		action.addParameter(pid);
		action.addParameter(grade);		
		
		recordAction(action, getOpUserName(), agent.getTS().getAg().getBB());
	}
	
	@OPERATION
	void reject_passport(int pid, String grade) {
		rejectedPassports.add(new PassportLogEntry(pid, PassportGrade.valueOf(grade)));
		
		ObsProperty balance = getObsProperty("balance");
		balance.updateValue(balance.intValue() + 5);
		
		AgentId officer = getOpUserId();
		signal(officer, "payment");
		
		CentralisedAgArch agent = RunCentralisedMAS.getRunner().getAg(getOpUserName());
		ActionDescription action = new ActionDescription(thisOpId.getOpName());
		action.addParameter(pid);
		action.addParameter(grade);		
		
		recordAction(action, getOpUserName(), agent.getTS().getAg().getBB());
	}
	
	// TODO: Fix the way in which the immigrant's bribe value is generated.
	@OPERATION
	void pay_bribe(double bribeValue) {
		ObsProperty balance = getObsProperty("balance");
		balance.updateValue(balance.intValue() + ((Double)bribeValue).intValue());
		signal(getCreatorId(), "bribe_payment");
	}
	
	@OPERATION
	void execute_sanction(Norm norm) {
		try {
			ObsProperty balance = getObsProperty("balance");
			balance.updateValue(balance.intValue() - ((NumberTerm) norm.getSanction().getTerm(0))
					.solve());
		} catch (NoValueException e) {
			Logger.getLogger(this.getOpUserBody().getName(), this.getClass()).warning("Couldn't parse sanction function argument.");
		}
	}
	
	void recordAction(ActionDescription action, String agentName, BeliefBase beliefBase) {
		ActionHistory history = ActionHistory.getInstance();
		
		HashSet<Literal> beliefs = new HashSet<Literal>();
		for (Literal l: beliefBase) {
			beliefs.add(l);
		}
		history.record(action, getOpUserName(), beliefs);
	}
}
