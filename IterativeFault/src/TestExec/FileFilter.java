package TestExec;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter {

	private String type;
	
	public FileFilter(String type){
		this.type = type;
	} 
	
	@Override
	public boolean accept(File file, String name) {
		return name.endsWith(type);
	}

	
}
