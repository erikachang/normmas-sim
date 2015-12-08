package normmas.jason;

import java.util.HashSet;
import java.util.List;

import jason.asSemantics.ActionExec;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import normmas.ActionDescription;
import normmas.ActionHistory;

@Deprecated
public class MonitoredCAgentArch extends c4jason.CAgentArch  {

	public MonitoredCAgentArch() {
		super();
	}

	private void record(String agName, ActionExec actionExec, HashSet<Literal> beliefs) {
		ActionHistory history = ActionHistory.getInstance();
		ActionDescription action = new ActionDescription(actionExec.getActionTerm().getFunctor());
		Structure actionStructure = actionExec.getActionTerm();
		for(int i = 0; i < actionStructure.getTerms().size(); i++) {
			action.addParameter(actionStructure.getTerm(i));
		}
		history.record(action, agName, beliefs);
	}


	public void act(ActionExec actionExec, List<ActionExec> feedback) {
		super.act(actionExec, feedback);
		
		// Should only record actions that haven't failed.
		if (actionExec.getFailureMsg() == null) {
			HashSet<Literal> beliefs = new HashSet<Literal>();
			
			for (Literal belief: this.belBase) {
				beliefs.add(belief);
			}
			
			String agentName = this.getAgName();
			this.record(agentName, actionExec, beliefs);
		}
	}

}

