package net.mcreator.tools;

import net.mcreator.io.FileIO;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

class DatalistUtils {
	public static ArrayList<String> readListFromFile(URL path){
		try {
			return new ArrayList<>(Arrays.asList(FileIO.readFileToString(new File(path.toURI())).split("\\r?\\n")));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void compareLists(ArrayList<String> minecraft, ArrayList<String> mcreator){
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("MCreator is missing:");
		System.out.println("----------------------------------------\n");
		for(String e: minecraft)
			if(!mcreator.contains(e))
				System.out.println(e);
		System.out.println("\n\n----------------------------------------\n");
		System.out.println("Minecraft is missing:");
		System.out.println("----------------------------------------\n");

		for(String e: mcreator)
			if(!minecraft.contains(e))
				System.out.println(e);

	}
}