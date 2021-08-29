package net.mcreator.tools;

import net.mcreator.io.FileIO;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class MappingUtils {
	public static Map<String, String> readSimpleMapFromFile(URL path) {
		try {
			return new ArrayList<>(
					Arrays.asList(FileIO.readFileToString(new File(path.toURI())).split("\\r?\\n"))).stream()
					.collect(Collectors.toMap(element -> element.split(": ")[0], element -> element.split(": ")[1]));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void compareSimpleMaps(ArrayList<String> mcr, Map<String, String> mcrmap, ArrayList<String> min) {
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator map is missing values:");
		System.out.println("----------------------------------------\n");
		for(String e: min)
			if(!mcrmap.containsValue(e))
				System.out.println(e);
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("Minecraft is missing:");
		System.out.println("----------------------------------------\n");
		for(String e: mcrmap.values())
			if(!min.contains(e))
				System.out.println(e);

		System.out.println("\n################################################\n");

		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator list is missing:");
		System.out.println("----------------------------------------\n");
		for(String e: mcrmap.keySet())
			if(!mcr.contains(e))
				System.out.println(e);
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator map is missing keys:");
		System.out.println("----------------------------------------\n");
		for(String e: mcr)
			if(!mcrmap.containsKey(e))
				System.out.println(e);
	}
}
