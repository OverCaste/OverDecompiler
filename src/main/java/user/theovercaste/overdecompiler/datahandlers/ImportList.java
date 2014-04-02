package user.theovercaste.overdecompiler.datahandlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ImportList {
	private HashMap<String, String> imports = new HashMap<String, String>();

	public boolean addQualifiedPath(String path) {
		String externalPath = path.replace("/", "."); // Java paths look like package.declaration.Class externally, Package/Declaration/Class internally
		String qualifiedName;
		if (externalPath.endsWith(".Map.Entry")) {
			qualifiedName = "Map.Entry";
		} else {
			int dotIndex = externalPath.lastIndexOf(".");
			qualifiedName = path.substring(dotIndex + 1, externalPath.length());
		}
		this.imports.put(path, qualifiedName);
		return true;
	}

	public Collection<String> getImports( ) {
		ArrayList<String> ret = new ArrayList<String>();
		for (String s : this.imports.keySet()) {
			if (!s.startsWith("java.lang.")) {
				ret.add("import " + s + ";");
			}
		}
		return ret;
	}

	public String getQualifiedName(String path) {
		return this.imports.get(path);
	}
}
