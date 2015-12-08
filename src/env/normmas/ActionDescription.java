package normmas;

import java.util.ArrayList;
import java.util.List;

public class ActionDescription {
	
	private String name;
	private List<Object> parameters;
	
	public ActionDescription(String name) {
		this.name = name;
		this.parameters = new ArrayList<Object>();
	}
	public ActionDescription(String name, Object[] parameters) {
		
		this.name = name;
		this.parameters = new ArrayList<Object>(parameters.length);
		
		for (int i = 0; i < parameters.length; i++) {
			this.parameters.add(parameters[i]);
		}
	}

	public String getName() {
		return name;
	}

	public List<Object> getParameters() {
		return parameters;
	}
	
	public void addParameter(Object parameterValue) {
		this.parameters.add(parameterValue);
	}
	
	public String toString() {
		String action = String.format("%s(", getName());
		
		for (Object value: getParameters()) {
			action = String.format("%s%s,", action, value.toString());
		}
		
		action = action.substring(0, (action.length() - 1));
		action += ")";
		
		return action;
	}
}
