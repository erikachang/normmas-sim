// CArtAgO artifact code for project goldminers

package normmas.artifacts;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import normmas.StatisticsBase;
import cartago.Artifact;
import cartago.OPERATION;

public class StatisticsArtifact extends Artifact {
	
	@OPERATION
	void incStat(String name) {
		StatisticsBase base = StatisticsBase.getInstance();
		base.statUp(name);
	}
	
	@OPERATION
	void updateStat(String name, int value) {
		StatisticsBase base = StatisticsBase.getInstance();
		base.addOrUpdateStat(name, value);
	}
	
	@OPERATION
	void dumpStatistics() {
		StatisticsBase base = StatisticsBase.getInstance();
		
		PrintWriter writer;
		try {
			writer = new PrintWriter("mas-statistics.txt", "UTF-8");
			
			writer.print(base.toString().replace(',', '\n'));
			
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

