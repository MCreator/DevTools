package net.mcreator.tools.utils.blockitem;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

public class TextureUtils {
	public static ArrayList<String> listTextures(File folder) {
		ArrayList<String> textures = new ArrayList<>();

		File[] files = folder.listFiles();

		if (files != null)
			for (File f : files)
				textures.add(FilenameUtils.getBaseName(f.getName()));

		return textures;
	}
}
