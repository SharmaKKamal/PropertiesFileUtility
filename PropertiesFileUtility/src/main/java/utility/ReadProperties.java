package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	private Properties readProp;
	private InputStream input;
	
	private static ReadProperties readProperties;
	
	public static ReadProperties getInstance() {
		if (readProperties == null) {
			readProperties = new ReadProperties();
		}
		return readProperties;
	}
	
	public Properties readProperties(String filePath) {
		readProp = new Properties();
		try {
			input = new FileInputStream(filePath);
			readProp.load(input);
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
		return readProp;
	}
	
}
