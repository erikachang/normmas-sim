// CArtAgO artifact code for project goldminers

package normmas.artifacts;

import jason.NoValueException;
import jason.asSemantics.ActionExec;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.asSyntax.parser.ParseException;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import normmas.ActionRecord;
import normmas.DeonticModality;
import normmas.EnforcementType;
import normmas.HashNormBase;
import normmas.Norm;
import normmas.NormBase;
import normmas.StatisticsBase;
import cartago.AgentId;
import cartago.Artifact;
import cartago.OPERATION;

public class NormativeArtifact extends Artifact {
	private List<AgentId> agentslist;

	protected final Logger logger = Logger.getLogger("NormativeArtifact");
	protected StatisticsBase base;
	protected NormBase normsBase;

	void init() {
		agentslist = new LinkedList<AgentId>();
		base = StatisticsBase.getInstance();
		base.addStat("Violation Detections", 0);
		normsBase = HashNormBase.getInstance();

		logger.info("NormativeArtifact created by "
				+ this.getCreatorId().getAgentName() + ".");
	}

	@OPERATION
	void register() {
		AgentId registeringAgent = this.getOpUserId();
		this.agentslist.add(registeringAgent);
		logger.info("Registering " + registeringAgent.getAgentName()
				+ " as an observer.");
	}

	@OPERATION
	void sanction(String agentName, Norm norm) {
		for (AgentId aid : agentslist) {
			if (aid.getAgentName().equals(agentName)) {
				try {
					logger.info("Signaling penalty "
							+ norm.getSanction().getFunctor() + " for "
							+ norm.getSanction().getTerm(0));
					signal(aid, norm.getSanction().getFunctor(),
							((NumberTerm) norm.getSanction().getTerm(0))
									.solve());
					this.base.statUp("Sanctions executed");
				} catch (NoValueException e) {
					logger.warning("Couldn't parse sanction function argument.");
				}
			}
		}
	}

	@OPERATION
	void createNorm(String normType, String conditionType, String context,
			String condition, String sanction, String id) {
		normsBase.addNorm(normType, conditionType, context, condition,
				sanction, id);
		signal("normsUpdated");
	}

	@OPERATION
	void activateNorm(String nId) {
		this.normsBase.activateNorm(nId);
		signal("normActivated", nId);
	}

	@OPERATION
	void deactivateNorm(String nId) {
		this.normsBase.deactivateNorm(nId);
		signal("normDeactivated", nId);
	}

	@OPERATION
	void purgeNorm(String nId) {
		this.normsBase.purgeNorm(nId);
		signal("normPurged", nId);
	}

	@OPERATION
	void detectViolation(Object report) {
		if (report == null)
			return;

		ActionRecord record = null;
		if (report.getClass().equals(ActionRecord.class)) {
			record = (ActionRecord) report;
		} else {
			logger.warning("Parameter not recognized. Aborting violation detection attempt.");
			return;
		}
		boolean isViolated;
		List<Norm> violatedNorms = new LinkedList<Norm>();

		for (Norm norm : normsBase.getActiveNorms()) {
			isViolated = false;
			if (contextApplies(norm.getEnforcementContext(),
					record.getBeliefs())) {
				if (norm.getEnforcedConditionType() == EnforcementType.ACTION) {
					ActionExec action = record.getAction();
					Structure actionStr = action.getActionTerm();
					if (norm.getEnforcedAction().getFunctor()
							.equals(actionStr.getFunctor())) {
						isViolated = (norm.getDeonticModality() == DeonticModality.PROHIBITION);
					} else {
						isViolated = (norm.getDeonticModality() == DeonticModality.OBLIGATION);
					}
				} else {
					if (contextApplies(norm.getEnforcedState(),
							record.getBeliefs())) {
						isViolated = (norm.getDeonticModality() == DeonticModality.PROHIBITION);
					} else {
						isViolated = (norm.getDeonticModality() == DeonticModality.OBLIGATION);
					}
				}

				if (isViolated) {
					this.base.statUp("Violation Detections");
					violatedNorms.add(norm);
				}
			}
		}

		for (Norm norm : violatedNorms) {
			signal("violation", record.getAgentName(), norm, norm.getNormId());
		}
	}

	private boolean contextApplies(Set<Term> context, Set<Literal> beliefs) {
		for (Term predicate : context) {
			boolean not = predicate.toString().startsWith("not");

			if (not) {
				try {
					predicate = ASSyntax.parseTerm(predicate.toString().split(
							" ")[1]);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			boolean contains = false;
			for (Literal belief : beliefs) {
				// Check if Functors match. Otherwise there's no need to
				// continue.

				if (belief.getFunctor().equals(
						((Literal) predicate).getFunctor().toString())) {
					// If Functors match, see if terms also match. Otherwise,
					// there's no need to continue.
					int beliefTerms = belief.getTerms().size();
					int predicateTerms = ((Literal) predicate).getTerms()
							.size();

					if (beliefTerms == predicateTerms) {
						for (int i = 0; i < beliefTerms; i++) {
							if (!belief.getTerm(i).equals(
									((Literal) predicate).getTerm(i))) {
								if (((Literal) predicate).getTerm(i).isVar()) {

								} else {
									return false;
								}
							}
						}
						contains = true;
					} else {
						return false;
					}
					break;
				}
			}

			if (not == contains)
				return false;
		}
		return true;
	}
}
