package net.mcreator.tools.utils;

import net.mcreator.io.FileIO;
import net.mcreator.tools.utils.blockitem.MapEntry;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class MappingUtils {
	public static LinkedHashMap<String, String> readSimpleMapFromFile(URL path) {
		try {
			return new ArrayList<>(
					Arrays.asList(FileIO.readFileToString(new File(path.toURI())).split("\\r?\\n"))).stream()
					.collect(LinkedHashMap::new, (map, item) -> map.put(item.split(": ")[0], item.split(": ")[1]),
							Map::putAll);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<LinkedHashMap<String, String>> readEntityMapFromFile(URL path) {
		ArrayList<LinkedHashMap<String, String>> mapList = new ArrayList<>();
		mapList.add(new LinkedHashMap<>());
		mapList.add(new LinkedHashMap<>());
		mapList.add(new LinkedHashMap<>());

		try {
			String currentKey = null;
			int currentIndex = 0;
			for (String line : FileIO.readFileToString(new File(path.toURI())).split("\\r?\\n")) {
				if (line.contains(":")) {
					String[] split = line.split(": ");
					currentKey = split[0].replace(":", "");
					currentIndex = 0;
					if (split.length > 1) {
						mapList.get(0).put(currentKey, split[1]);
						currentKey = null;
					}
				} else if (currentKey != null) {
					mapList.get(currentIndex).put(currentKey, line.replace("  - ", ""));
					currentIndex++;
				}
			}
			return mapList;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void compareSimpleMaps(ArrayList<String> mcr, Map<String, String> mcrmap, ArrayList<String> min) {
		compareSimpleMaps(mcr, mcrmap, min, null);
	}

	public static void compareSimpleMaps(ArrayList<String> mcr, Map<String, String> mcrmap, ArrayList<String> min,
			ArrayList<String> fallbackMapKeys) {
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator map is missing values:");
		System.out.println("----------------------------------------\n");
		for (String e : min)
			if (!mcrmap.containsValue(e))
				System.out.println(e);
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("Minecraft is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : mcrmap.values())
			if (!min.contains(e))
				System.out.println(e);

		System.out.println("\n################################################\n");

		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator list is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : mcrmap.keySet())
			if (!mcr.contains(e))
				System.out.println(e);
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator map is missing keys:");
		System.out.println("----------------------------------------\n");
		for (String e : mcr)
			if (!(mcrmap.containsKey(e) || (fallbackMapKeys != null && fallbackMapKeys.contains(e))))
				System.out.println(e);
	}

	public static void compareEntityMaps(ArrayList<String> mcr, ArrayList<LinkedHashMap<String, String>> mcrmap,
			ArrayList<ArrayList<String>> mins) {
		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####          Entity class mappings         ####");
		System.out.println("################################################");
		System.out.println("################################################");
		compareSimpleMaps(mcr, mcrmap.get(0), mins.get(0));

		System.out.println();
		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####           EntityType mappings          ####");
		System.out.println("################################################");
		System.out.println("################################################");
		compareSimpleMaps(mcr, mcrmap.get(1), mins.get(1), new ArrayList<>(mcrmap.get(0).keySet()));

		System.out.println();
		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####         Entity registry mappings       ####");
		System.out.println("################################################");
		System.out.println("################################################");
		compareSimpleMaps(mcr, mcrmap.get(2), mins.get(2), new ArrayList<>(mcrmap.get(0).keySet()));
	}

	public static void compareRegistryMaps(ArrayList<String> mcreatorList, LinkedHashMap<String, MapEntry> particleMap,
			ArrayList<String> particleClasses, ArrayList<String> particleRegistry) {
		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####         Particle class mappings        ####");
		System.out.println("################################################");
		System.out.println("################################################");

		compareSimpleMaps(mcreatorList, MapEntry.getClassMap(particleMap), particleClasses);

		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####        Particle registry mappings      ####");
		System.out.println("################################################");
		System.out.println("################################################");

		compareSimpleMaps(mcreatorList, MapEntry.getRegistryMap(particleMap), particleRegistry);
	}
}
