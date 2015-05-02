package normmas;

import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.Set;

public interface NormBase {

	boolean addNorm(Norm nv);

	boolean addNorm(DeonticModality mu, EnforcementType kappa, Set<Term> chi,
			Literal tau, Literal rho, String id);

	boolean addNorm(DeonticModality mu, EnforcementType kappa, Set<Term> chi,
			Set<Term> tau, Literal rho, String id);

	boolean addNorm(String mu, String kappa, String chi, String tau,
			String rho, String id);

	Set<Norm> getActiveNorms();

	Norm getById(String normId);

	boolean contains(String normId);
	
	boolean contains(Norm norm);

	boolean isActive(String normId);
	
	boolean isActive(Norm norm);

	boolean activateNorm(String normId);

	boolean activateNorm(Norm norm);

	boolean deactivateNorm(String normId);

	boolean deactivateNorm(Norm norm);

	boolean purgeNorm(String normId);

	boolean purgeNorm(Norm norm);

}
