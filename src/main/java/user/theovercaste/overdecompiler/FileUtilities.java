package user.theovercaste.overdecompiler;

import java.io.File;

public class FileUtilities {
	public static String getFileExtention(File f) {
		String name = f.getName();
		int periodIndex = name.lastIndexOf(".");
		if ((periodIndex <= 0) || (periodIndex == name.length())) {
			return name;
		}
		return name.substring(periodIndex + 1, name.length());
	}

	public static String getFileName(File f) {
		String name = f.getName();
		int periodIndex = name.lastIndexOf(".");
		if ((periodIndex <= 0) || (periodIndex == name.length())) {
			return name;
		}
		return name.substring(0, periodIndex);
	}
}
