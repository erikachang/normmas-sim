package normmas;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticsBase {
	private static StatisticsBase instance;
	
	protected ConcurrentHashMap<String, Double> base;
	
	private StatisticsBase() {
		this.base = new ConcurrentHashMap<String, Double>();
	}
	
	public static StatisticsBase getInstance() {
		if (instance == null) {
			instance = new StatisticsBase();
		}
		return instance;
	}
	
	public boolean containsStat(String name) {
		return this.base.containsKey(name);
	}
	
	public void addStat(String name, double value) {
		if (!this.base.containsKey(name)) {
			this.base.put(name, value);
		}
	}
	
	public void updateStat(String name, double value) {
		if (this.base.containsKey(name)) {
			this.base.replace(name, value);
		}
	}
	
	public void addOrUpdateStat(String name, double value) {
		if (this.base.containsKey(name)) {
			this.base.replace(name, value);
		} else {
			this.base.put(name, value);
		}
	}
	
	public void statUp(String name) {
		if (!this.base.containsKey(name)) {
			this.addStat(name, 0);
		}
		double value = this.base.get(name);
		this.base.replace(name, ++value);
	}
	
	public double readStat(String name) {
		if (this.base.containsKey(name)) {
			Double stat = this.base.get(name);
			if (stat == null)
				return 0;
			return stat;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		String result = "Statistic\tValue\n";
		for (Entry<String, Double> entry: this.base.entrySet()) {
			result = result.concat(entry.getKey() + "\t" + entry.getValue() + "\n");
		}
		return result;
	}
}
