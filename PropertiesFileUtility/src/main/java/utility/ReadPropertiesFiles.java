package utility;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadPropertiesFiles {

	private static ReadPropertiesFiles readPropertiesFiles;
	private List<Path> fileName;
	private String folderPath;
	
	public static ReadPropertiesFiles getInstance(String folderPath) {
		if (readPropertiesFiles == null) {
			readPropertiesFiles = new ReadPropertiesFiles();
			readPropertiesFiles.folderPath = folderPath;
			readPropertiesFiles.fileName = new ArrayList<Path>();
		}
		return readPropertiesFiles;
	}
	
	public List<Path> getFilesName()
	{
		Path path = Paths.get(folderPath);
		if (Files.exists(path)) {
			try
			{
				DirectoryStream<Path> stream;
				stream = Files.newDirectoryStream(path);
				for (Path entry : stream)
				{
					if (entry.toFile().getName().contains("properties")) {
						fileName.add(entry);
					}
				}
				stream.close();
			} catch (IOException e) {
				//fail to create directory
				e.printStackTrace();
			}
		}
		return fileName;
	}
	
}
