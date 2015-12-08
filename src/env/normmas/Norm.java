package normmas;

import java.util.Set;

import jason.asSyntax.Literal;
import jason.asSyntax.Term;

public abstract class Norm {

	protected DeonticModality deonticModality;
	protected EnforcementType enforcedConditionType;
	protected Set<Term> enforcementContext;
	protected Set<Term> enforcedState;
	protected Literal enforcedAction;
	protected Literal sanction;
	protected String id;

	public abstract String getNormId();

	public abstract DeonticModality getDeonticModality();

	public abstract EnforcementType getEnforcedConditionType();

	public abstract Set<Term> getEnforcementContext();

	public abstract Literal getEnforcedAction();

	public abstract Set<Term> getEnforcedState();

	public abstract Literal getSanction();

}
