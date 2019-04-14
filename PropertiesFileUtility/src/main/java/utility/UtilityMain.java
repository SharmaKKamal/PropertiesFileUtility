package utility;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class UtilityMain {

	private String pathLocation;
	private String baseFilePath;
	private Properties enProperties;
	
	private ReadPropertiesFiles readPropertiesFiles;
	private ReadProperties readProperties;
	private static UtilityMain commandLineRunner;

	public static void main(String[] args) {
		commandLineRunner = new UtilityMain();
		commandLineRunner.pathLocation = args[0];
		commandLineRunner.baseFilePath = args[1];
		
		System.out.println(commandLineRunner.pathLocation);
		System.out.println(commandLineRunner.baseFilePath);
		
		commandLineRunner.readProperties = ReadProperties.getInstance();
		commandLineRunner.readPropertiesFiles = ReadPropertiesFiles.getInstance(commandLineRunner.pathLocation);
		Path path = Paths.get(commandLineRunner.baseFilePath);
		List<Path> propertiesFiles = commandLineRunner.readPropertiesFiles.getFilesName();
		commandLineRunner.enProperties = commandLineRunner.readProperties.readProperties(path.toString());
		propertiesFiles.removeAll(new ArrayList<Path>(Arrays.asList(path)));
		for (Path localePropertiesPath : propertiesFiles) {
			Properties localeProperties = commandLineRunner.readProperties.readProperties(localePropertiesPath.toString());
			Enumeration<?> e = commandLineRunner.enProperties.propertyNames();
			Enumeration<?> le = localeProperties.propertyNames();
			List<String> englishKeys = new ArrayList<>();
			List<String> localeKeysSet = new ArrayList<>();
			while (e.hasMoreElements()) {
				if (!localeProperties.containsKey(e.nextElement())) {
					englishKeys.add((String)e.nextElement());
				}
			}
			while (le.hasMoreElements()) {
				if (!commandLineRunner.enProperties.containsKey(le.nextElement())) {
					localeKeysSet.add((String)le.nextElement());
				}
			}
			commandLineRunner.writeReportFile(path.toFile().getName(), localePropertiesPath.toFile().getName(), englishKeys, localeKeysSet);
		}
	}
	
	private void writeReportFile(String baseName, String fileName, List<String> englishKeys, List<String> localeKeys)
	{
		String basefileIntital = baseName.replace(".properties", "");
		String localFileName = fileName.replace(".properties", "").replace(basefileIntital + "_", "");
		String reportFilePath = commandLineRunner.pathLocation + "/UnMapped_" + localFileName +"_Keys.txt";
		Path reportFile = Paths.get(reportFilePath);
		
		try
		{
			PrintWriter writer = new PrintWriter(reportFile.toFile(), "UTF-8");
			writer.println("####### Keys Exists in EN locale but not for " + localFileName.toUpperCase() +" locale #######");
			writer.println("####### UnMapped Keys Count : " + englishKeys.size() + " #######");
			for (String testScript: englishKeys) {
				writer.println(testScript);
			}
			
			writer.println("\n\n\n####### Keys Exists in " + localFileName.toUpperCase() + " locale but not for EN locale #######");
			writer.println("####### UnMapped Keys Count : " + localeKeys.size() + " #######");
			for (String testScript: localeKeys) {
				writer.println(testScript);
			}
			
			writer.close();
		}
		catch (FileNotFoundException | UnsupportedEncodingException e)
		{
			e.printStackTrace();
			System.exit(40);
		}
	}
	
}
