package normmas;

import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.HashSet;
import java.util.Set;

public final class HashNormBase implements NormBase {

	private HashSet<Norm> norms, activeNorms;
	private static HashNormBase instance;

	private HashNormBase() {
		norms = new HashSet<Norm>();
		activeNorms = new HashSet<Norm>();
	}

	public static HashNormBase getInstance() {
		if (instance == null)
			instance = new HashNormBase();
		return instance;
	}

	public boolean addNorm(Norm nv) {
		if (nv != null) {
			return norms.add(nv);
		}
		return false;
	}

	public boolean addNorm(DeonticModality mu, EnforcementType kappa,
			Set<Term> chi, Literal tau, Literal rho, String id) {
		Norm norm = new StandardNorm(mu, kappa, chi, tau, rho, id);
		return addNorm(norm);
	}

	public boolean addNorm(DeonticModality mu, EnforcementType kappa,
			Set<Term> chi, Set<Term> tau, Literal rho, String id) {
		Norm norm = new StandardNorm(mu, kappa, chi, tau, rho, id);
		return addNorm(norm);
	}

	public boolean addNorm(String deonticModality, String enforcementType,
			String enforcementContext, String enforcedCondition,
			String sanction, String id) {
		Norm newNorm = StandardNorm.parseNorm(deonticModality, enforcementType,
				enforcementContext, enforcedCondition, sanction, id);
		return addNorm(newNorm);
	}

	public Set<Norm> getActiveNorms() {
		return new HashSet<Norm>(activeNorms);
	}

	/**
	 * Queries the list of System Norms using the given normId.
	 * 
	 * @param normId
	 * @return A Norm object found on the base with the given normId. null is
	 *         returned otherwise.
	 */
	public Norm getById(String normId) {
		for (Norm n : norms) {
			if (n.getNormId().equals(normId))
				return n;
		}
		return null;
	}

	public boolean contains(String normId) {
		Norm norm = getById(normId);
		return (norm != null);
	}

	public boolean contains(Norm norm) {
		return ((norm != null) && norms.contains(norm));
	}

	public boolean isActive(String normId) {
		Norm norm = getById(normId);
		return ((norm != null) && activeNorms.contains(norm));
	}

	public boolean isActive(Norm norm) {
		return ((norm != null) && activeNorms.contains(norm));
	}

	/**
	 * Activates a Norm by copying it from the list of System Norms to the list
	 * of Active Norms.
	 * 
	 * @param normId
	 *            The identification string of the Norm to be activated.
	 * @return True if activation was successful. False otherwise.
	 */
	public boolean activateNorm(String normId) {
		Norm normToActivate = getById(normId);

		if (normToActivate != null) {
			return activeNorms.add(normToActivate);
		}

		return false;
	}

	public boolean activateNorm(Norm normToActivate) {
		if (normToActivate != null && contains(normToActivate)) {
			return activeNorms.add(normToActivate);
		}

		return false;
	}

	/**
	 * Removes a Norm from the list of Active Norms. This does not completely
	 * remove the Norm from the system.
	 * 
	 * @param normId
	 *            The identification string of the Norm to be deactivated.
	 * @return True if deactivation was successful. False otherwise.
	 */
	public boolean deactivateNorm(String normId) {
		Norm normToDeactivate = getById(normId);

		if (normToDeactivate != null) {
			return activeNorms.remove(normToDeactivate);
		}

		return false;
	}

	public boolean deactivateNorm(Norm normToDeactivate) {
		if (normToDeactivate != null) {
			return activeNorms.remove(normToDeactivate);
		}

		return false;
	}

	/**
	 * Destroys a Norm.
	 * 
	 * @param normId
	 *            The identification string of the Norm to be destroyed.
	 * @return True if the Norm's been found and destroyed. False if there
	 *         aren't any norms with such an identification string.
	 */
	public boolean purgeNorm(String normId) {
		Norm normToRemove = getById(normId);

		if (normToRemove != null) {
			boolean success = true;
			if (activeNorms.contains(normToRemove))
				success = activeNorms.remove(normToRemove);

			success = success && norms.remove(normToRemove);
			return success;
		}

		return false;
	}

	public boolean purgeNorm(Norm normToRemove) {
		if (normToRemove != null) {
			boolean success = true;
			if (activeNorms.contains(normToRemove))
				success = activeNorms.remove(normToRemove);

			success = success && norms.remove(normToRemove);
			return success;
		}

		return false;
	}

	/**
	 * Queries the Active Norms base for the existence of a Norm which could be
	 * violated by the given action.
	 * 
	 * @param actionName
	 *            The name of the action to search for.
	 * @return True if there are any active Norms in the system that involve the
	 *         execution of the given action. False otherwise.
	 */
	public boolean isActionEnforced(String actionName) {
		for (Norm n : this.activeNorms) {
			if (n.getEnforcedConditionType() == EnforcementType.ACTION) {
				if (n.getEnforcedAction().getFunctor().equals(actionName))
					return true;
			}
		}
		return false;
	}

}
