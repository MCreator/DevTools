package net.mcreator.tools.utils;

import net.mcreator.io.FileIO;
import net.mcreator.tools.utils.blockitem.RegistryUtils;
import net.mcreator.tools.utils.blockitem.TextureUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DatalistUtils {
	public static final Pattern SOUNDS_CLASS_PATTERN = Pattern.compile(
			"(?:Holder.Reference<SoundEvent>|SoundEvent) .* = (?:register|registerForHolder)\\(\"(.*)\"\\);");
	public static final Pattern ENTITY_CLASS_PATTERN = Pattern.compile(
			"public static final EntityType<(.+)> (.+) = register\\(\"(.+)\", EntityType");

	public static ArrayList<String> readFileAsList(URL path) {
		try {
			return new ArrayList<>(
					Arrays.asList(FileIO.readFileToString(new File(path.toURI())).split("\\r?\\n")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> readListFromFile(URL path) {
		try {
			return new ArrayList<>(
					Arrays.asList(FileIO.readFileToString(new File(path.toURI())).replace("- ", "").split("\\r?\\n")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> readEntityListFromFile(URL path) {
		try {
			ArrayList<String> list = new ArrayList<>();
			for (String entry : FileIO.readFileToString(new File(path.toURI())).split("\\r?\\n"))
				if (entry.startsWith("- "))
					list.add(entry.replace("- ", "").replace(":", ""));
			return list;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> extractMatchListFromClass(URL path, Pattern pattern) {
		try {
			String file = FileIO.readFileToString(new File(path.toURI()));
			return pattern.matcher(file).results().map(m -> m.group(1))
					.collect(Collectors.toCollection(ArrayList::new));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<ArrayList<String>> extractEntityListFromClass(URL path) {
		try {
			String file = FileIO.readFileToString(new File(path.toURI()));
			return new ArrayList<>(Arrays.asList(ENTITY_CLASS_PATTERN.matcher(file).results().map(m -> m.group(1))
							.collect(Collectors.toCollection(ArrayList::new)),
					ENTITY_CLASS_PATTERN.matcher(file).results().map(m -> "EntityType." + m.group(2))
							.collect(Collectors.toCollection(ArrayList::new)),
					ENTITY_CLASS_PATTERN.matcher(file).results().map(m -> m.group(3))
							.collect(Collectors.toCollection(ArrayList::new))));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void compareLists(ArrayList<String> minecraft, ArrayList<String> mcreator) {
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : minecraft)
			if (!mcreator.contains(e))
				System.out.println(e);
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("Minecraft is missing:");
		System.out.println("----------------------------------------\n");

		for (String e : mcreator)
			if (!minecraft.contains(e))
				System.out.println(e);

	}

	public static ArrayList<String> listTextures(String resourceName) {
		ArrayList<String> icons = new ArrayList<>();
		try {
			icons = TextureUtils.listTextures(
					new File(ClassLoader.getSystemClassLoader().getResource(resourceName).toURI()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return icons;
	}

	public static ArrayList<String> listFilesInFoldersAsRegistryEntries(String resourceName) {
		ArrayList<String> registry = new ArrayList<>();
		try {
			registry = RegistryUtils.listFolderAsRegistry(
					new File(ClassLoader.getSystemClassLoader().getResource(resourceName).toURI()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return registry;
	}
}