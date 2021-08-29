package net.mcreator.tools;

import java.util.ArrayList;
import java.util.Map;

public class ComparisonTool {

	private static final Compare WHAT = Compare.MAP;

	public enum Compare {
		LIST, MAP
	}

	public static void main(String[] args) {
		ArrayList<String> mcr, min;
		switch (WHAT) {
		case LIST:
			mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
			min = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/minecraft"));

			DatalistUtils.compareLists(min, mcr);
			break;
		case MAP:
			mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
			min = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/minecraft"));

			Map<String, String> mcrmap = MappingUtils.readSimpleMapFromFile(
					ClassLoader.getSystemClassLoader().getResource("maps/mcreator"));

			MappingUtils.compareSimpleMaps(mcr, mcrmap, min);

			break;
		}
	}
}
