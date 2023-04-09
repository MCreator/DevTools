package net.mcreator.tools.utils.blockitem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BlockItemUtils {
	private static final Pattern name_pattern = Pattern.compile("- (.*):");
	private static final Pattern read_name_pattern = Pattern.compile(" {2}readable_name: \"(.*)\"");
	private static final Pattern texture_pattern = Pattern.compile(" {2}texture: (.*)");
	private static final Pattern description_pattern = Pattern.compile(" {2}description: \"(.*)\"");
	private static final Pattern subtypes_pattern = Pattern.compile(" {2}subtypes: (.*)");
	private static final Pattern type_pattern = Pattern.compile(" {2}type: (.*)");

	public static LinkedHashMap<String, Entry> parseList(ArrayList<String> list) {
		LinkedHashMap<String, Entry> entryList = new LinkedHashMap<>();

		String key = null;
		Entry entry = new Entry();

		for (String line : list) {
			Matcher name = name_pattern.matcher(line);
			if (name.find())
				key = name.group(1);
			else {
				Matcher readable_name = read_name_pattern.matcher(line);
				if (readable_name.find())
					entry.setName(readable_name.group(1));
				else {
					Matcher texture = texture_pattern.matcher(line);
					if (texture.find())
						entry.setTexture(texture.group(1));
					else {
						Matcher description = description_pattern.matcher(line);
						if (description.find())
							entry.setDescription(description.group(1));
						else {
							Matcher subtypes = subtypes_pattern.matcher(line);
							if (subtypes.find())
								entry.setSubtypes(subtypes.group(1).equals("true"));
							else {
								Matcher type = type_pattern.matcher(line);
								if (type.find())
									switch (type.group(1)) {
									case "block" -> entry.setType(Type.block);
									case "item" -> entry.setType(Type.item);
									}
								else {
									if (key != null)
										entryList.put(key, entry);
									key = null;
									entry = new Entry();
								}
							}
						}
					}
				}
			}
		}

		if (key != null)
			entryList.put(key, entry);

		return entryList;
	}

	private static final Pattern map_name_pattern = Pattern.compile("(.*\\..*):");
	private static final Pattern class_name_pattern = Pattern.compile(" {2}- (.*\\..*)");
	private static final Pattern registry_name_pattern = Pattern.compile(" {2}- \"(.*)\"");

	public static LinkedHashMap<String, MapEntry> parseMap(ArrayList<String> list) {
		LinkedHashMap<String, MapEntry> entryMap = new LinkedHashMap<>();

		String key = null;
		MapEntry entry = new MapEntry();

		for (String line : list) {
			Matcher name = map_name_pattern.matcher(line);
			if (name.find()) {
				if (key != null)
					entryMap.put(key, entry);

				key = name.group(1);
				entry = new MapEntry();
			} else {
				Matcher class_name = class_name_pattern.matcher(line);
				if (class_name.find())
					entry.setClassName(class_name.group(1));
				else {
					Matcher registry_name = registry_name_pattern.matcher(line);
					if (registry_name.find())
						entry.setRegistryName(registry_name.group(1));
					else {
						if (key != null)
							entryMap.put(key, entry);
						key = null;
						entry = new MapEntry();
					}
				}
			}
		}

		if (key != null)
			entryMap.put(key, entry);

		return entryMap;
	}

	public static void compareBIMaps(LinkedHashMap<String, Entry> entryList, LinkedHashMap<String, MapEntry> entryMap,
			ArrayList<String> blocks, ArrayList<String> blocksreg, ArrayList<String> items, ArrayList<String> itemsreg,
			ArrayList<String> icons, boolean itemToBlockFallback) {

		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####            MCreator mappings           ####");
		System.out.println("################################################");
		System.out.println("################################################");

		System.out.println("----------------------------------------");
		System.out.println("            Map is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : entryMap.keySet())
			if (!entryList.containsKey(e))
				System.out.println(e);
		System.out.println("\n----------------------------------------");
		System.out.println("            List is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : entryList.keySet())
			if (!entryMap.containsKey(e))
				System.out.println(e);

		System.out.println("\n################################################\n");

		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####             Class mappings             ####");
		System.out.println("################################################");
		System.out.println("################################################");

		ArrayList<String> joinedClasses = blocks.parallelStream().map(entry -> "Blocks." + entry)
				.collect(Collectors.toCollection(ArrayList::new));
		joinedClasses.addAll(items.parallelStream().map(entry -> "Items." + entry).toList());

		ArrayList<String> classes = entryMap.values().parallelStream().map(MapEntry::getClassName)
				.collect(Collectors.toCollection(ArrayList::new));

		System.out.println("----------------------------------------");
		System.out.println("         MCreator is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : joinedClasses)
			if (!classes.contains(e) && !(itemToBlockFallback && e.startsWith("Items.") && classes.contains(
					"Blocks." + e.substring(6))))
				System.out.println(e);
		System.out.println("\n----------------------------------------");
		System.out.println("         Minecraft is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : classes)
			if (!joinedClasses.contains(e))
				System.out.println(e);

		System.out.println("\n################################################\n");

		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####         Block registry mappings        ####");
		System.out.println("################################################");
		System.out.println("################################################");

		ArrayList<String> filteredblocks = entryList.entrySet().parallelStream()
				.filter(e -> e.getValue().getType() == Type.block).map(e -> entryMap.get(e.getKey()).getRegistryName())
				.collect(Collectors.toCollection(ArrayList::new));

		System.out.println("----------------------------------------");
		System.out.println("         MCreator is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : blocksreg)
			if (!filteredblocks.contains(e))
				System.out.println(e);
		System.out.println("\n----------------------------------------");
		System.out.println("         Minecraft is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : filteredblocks)
			if (!blocksreg.contains(e) && !e.startsWith("#"))
				System.out.println(e);

		System.out.println("\n################################################\n");

		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####         Item registry mappings         ####");
		System.out.println("################################################");
		System.out.println("################################################");

		ArrayList<String> filteredItems = entryList.entrySet().parallelStream()
				.filter(e -> e.getValue().getType() == Type.item).map(e -> {
					if (!entryMap.containsKey(e.getKey()))
						System.out.println(e.getKey());
					return entryMap.get(e.getKey()).getRegistryName();
				}).collect(Collectors.toCollection(ArrayList::new));

		System.out.println("----------------------------------------");
		System.out.println("         MCreator is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : itemsreg)
			if (!filteredItems.contains(e))
				System.out.println(e);
		System.out.println("\n----------------------------------------");
		System.out.println("         Minecraft is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : filteredItems)
			if (!itemsreg.contains(e) && !blocksreg.contains(e) && !e.startsWith("#"))
				System.out.println(e);

		System.out.println("\n################################################\n");

		System.out.println("################################################");
		System.out.println("################################################");
		System.out.println("####                  Icons                 ####");
		System.out.println("################################################");
		System.out.println("################################################");

		ArrayList<String> textures = entryList.entrySet().parallelStream()
				.map(e -> e.getValue().getTexture() != null ? e.getValue().getTexture() : "MISSING: " + e.getKey())
				.collect(Collectors.toCollection(ArrayList::new));

		System.out.println("----------------------------------------");
		System.out.println("         Map is missing:");
		System.out.println("----------------------------------------\n");
		for (String e : icons)
			if (!textures.contains(e))
				System.out.println(e);
		System.out.println("\n----------------------------------------");
		System.out.println("         Textures are missing:");
		System.out.println("----------------------------------------\n");
		for (String e : textures)
			if (!icons.contains(e))
				System.out.println(e);

		System.out.println("\n################################################\n");

	}
}
