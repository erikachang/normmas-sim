package normmas;

import java.util.Set;

import jason.asSyntax.Literal;

public class ActionRecord {

	private String agentName;
	private ActionDescription action;
	private Set<Literal> beliefs;

	public ActionRecord(ActionDescription alpha, String gamma, Set<Literal> beta) {
		agentName = gamma;
		action = alpha;
		beliefs = beta;
	}

	public Set<Literal> getBeliefs() {
		return beliefs;
	}

	public ActionDescription getAction() {
		return action;
	}

	public String getAgentName() {
		return agentName;
	}

	@Override
	public String toString() {
		String str = "<";

		str += agentName + ", " + action.toString() + ", " + beliefs.toString()
				+ ">";

		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
		result = prime * result
				+ ((agentName == null) ? 0 : agentName.hashCode());
		result = prime * result + ((beliefs == null) ? 0 : beliefs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionRecord other = (ActionRecord) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (agentName == null) {
			if (other.agentName != null)
				return false;
		} else if (!agentName.equals(other.agentName))
			return false;
		if (beliefs == null) {
			if (other.beliefs != null)
				return false;
		} else if (!beliefs.equals(other.beliefs))
			return false;
		return true;
	}

}
