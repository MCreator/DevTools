package net.mcreator.tools.utils.blockitem;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

public class RegistryUtils {
	public static ArrayList<String> listFolderAsRegistry(File folder) {
		return listFolderAsRegistry("minecraft:", folder);
	}

	private static ArrayList<String> listFolderAsRegistry(String prefix, File folder) {
		ArrayList<String> registryItems = new ArrayList<>();

		File[] files = folder.listFiles();

		if (files != null)
			for (File f : files)
				if (f.isDirectory()) {
					if (!f.getName().equals("recipes"))
						registryItems.addAll(listFolderAsRegistry(prefix + f.getName() + "/", f));
				} else
					registryItems.add(prefix + FilenameUtils.getBaseName(f.getName()));

		return registryItems;
	}
}
