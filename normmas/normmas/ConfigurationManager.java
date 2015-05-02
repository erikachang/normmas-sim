package normmas;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigurationManager {
	public static boolean updateConfig(String configName, String value) {
		Properties prop = new Properties();
		OutputStream output = null;
		InputStream input = null;
	 
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			input.close();
			
			output = new FileOutputStream("config.properties");
			// set the properties value
			prop.setProperty(configName, value);

			// save properties to project root folder
			prop.store(output, null);
			
			return true;
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 
		}
		return false;
	}
	
	public static String readConfig(String configName) {
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
	 
			input = new FileInputStream("config.properties");
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			return prop.getProperty(configName);
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
}
