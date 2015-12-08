// CArtAgO artifact code for project checkpoint

package example.immigration;

import cartago.*;

public class PassportGeneratorArtifact extends Artifact {

	@OPERATION
	void request_passport(OpFeedbackParam<Passport> passport) {
		String agentName = getOpUserName();
		Passport randomPassport = new Passport(agentName);
		passport.set(randomPassport);
	}
}

