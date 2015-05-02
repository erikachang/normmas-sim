package normmas.jason;

import jason.asSemantics.ActionExec;
import jason.asSyntax.Literal;

import java.util.HashSet;
import java.util.List;

import normmas.ActionHistory;
import c4njason.CAgentArch;

public class NCAgentArch extends CAgentArch {

	public NCAgentArch() {
		super();
	}

	private void record(String agName, ActionExec actionExec, HashSet<Literal> beliefs) {
		ActionHistory history = ActionHistory.getInstance();
		history.record(actionExec, agName, beliefs);
	}

	@Override
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