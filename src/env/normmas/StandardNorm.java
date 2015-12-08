package normmas;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jason.asSyntax.parser.ParseException;

import java.util.HashSet;
import java.util.Set;

public final class StandardNorm extends Norm {

	public static int NORM_ID = 0;

	public StandardNorm(DeonticModality mu, EnforcementType kappa,
			Set<Term> chi, Literal tau, Literal rho, String nId) {
		deonticModality = mu;
		enforcedConditionType = kappa;
		enforcementContext = chi;
		enforcedAction = tau;
		sanction = rho;
		id = nId;
	}

	public StandardNorm(DeonticModality mu, EnforcementType kappa,
			Set<Term> chi, Set<Term> tau, Literal rho, String nId) {
		deonticModality = mu;
		enforcedConditionType = kappa;
		enforcementContext = chi;
		enforcedState = tau;
		sanction = rho;
		id = nId;
	}

	public Set<Term> getEnforcedState() {
		return enforcedState;
	}

	public EnforcementType getEnforcedConditionType() {
		return enforcedConditionType;
	}

	public String getNormId() {
		return id;
	}

	public DeonticModality getDeonticModality() {
		return deonticModality;
	}

	public Set<Term> getEnforcementContext() {
		return enforcementContext;
	}

	public Literal getEnforcedAction() {
		return enforcedAction;
	}

	public Literal getSanction() {
		return sanction;
	}

	public static Norm parseNorm(String deonticModality,
			String enforcementType, String enforcementContext,
			String enforcedCondition, String sanction, String id) {
		try {
			DeonticModality modality = DeonticModality.valueOf(deonticModality
					.toUpperCase());
			EnforcementType enforcement = EnforcementType
					.valueOf(enforcementType.toUpperCase());
			HashSet<Term> context = new HashSet<Term>(ASSyntax.parseList(
					enforcementContext).getAsList());

			Literal sanctionPredicate = (Literal) ASSyntax
					.parseLiteral(sanction);

			Norm norm = null;
			if (enforcement == EnforcementType.STATE) {
				HashSet<Term> underlyingCondition = new HashSet<Term>(ASSyntax
						.parseList(enforcedCondition).getAsList());
				norm = new StandardNorm(modality, enforcement, context,
						underlyingCondition, sanctionPredicate, id);
			} else {
				Literal underlyingCondition = (Literal) ASSyntax
						.parseFormula(enforcedCondition);
				norm = new StandardNorm(modality, enforcement, context,
						underlyingCondition, sanctionPredicate, id);
			}

			return norm;

		} catch (ParseException e) {
			String log = ("Couldn't parse context string. ");
			if (e.currentToken != null)
				log = log.concat("Token: " + e.currentToken + ". ");
			log = log.concat("Error: " + e.getMessage());
			System.err.println(log);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String toString() {
		String str;
		if (this.getEnforcedConditionType() == EnforcementType.STATE) {
			str = "<" + deonticModality + ", " + enforcedConditionType + ", "
					+ enforcementContext.toString() + ", "
					+ enforcedState.toString() + ", " + sanction.toString()
					+ ">";
		} else {
			str = "<" + deonticModality + ", " + enforcedConditionType + ", "
					+ enforcementContext.toString() + ", "
					+ enforcedAction.toString() + ", " + sanction.toString()
					+ ">";
		}
		return str;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((deonticModality == null) ? 0 : deonticModality.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		StandardNorm other = (StandardNorm) obj;
		if (deonticModality != other.deonticModality)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
