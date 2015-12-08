// CArtAgO artifact code for project checkpoint

package example.immigration;

import java.util.concurrent.ConcurrentLinkedQueue;

import cartago.AgentId;
import cartago.Artifact;
import cartago.OPERATION;
import cartago.ObsProperty;

public class WaitingRoomArtifact extends Artifact {
	private ConcurrentLinkedQueue<AgentId> agentQueue;
	
	void init() {
		agentQueue = new ConcurrentLinkedQueue<AgentId>();
		defineObsProperty("nextBooth", "None");
		defineObsProperty("agentsInQueue", 0);
	}
	
	@OPERATION
	synchronized void enter_queue() {
		AgentId currentAgent = getOpUserId();
		agentQueue.offer(currentAgent);
		ObsProperty agentsInQueue = getObsProperty("agentsInQueue");
		agentsInQueue.updateValue(agentsInQueue.intValue() + 1);
	}
	
	@OPERATION
	synchronized void call_next(String boothId) {
		AgentId agent = agentQueue.poll();
		ObsProperty agentsInQueue = getObsProperty("agentsInQueue");
		agentsInQueue.updateValue(agentsInQueue.intValue() - 1);
		getObsProperty("nextBooth").updateValue(boothId);
		signal(agent, "i_am_next");
	}
}

