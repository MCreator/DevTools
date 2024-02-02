package net.mcreator.tools.utils;

import net.mcreator.tools.utils.blockitem.MapEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParticleUtils {

	public static final Pattern PARTICLE_CLASS_PATTERN = Pattern.compile(
			"public static final SimpleParticleType (.*?) = register.*\\(\".*\"");
	public static final Pattern PARTICLE_REGISTRY_PATTERN = Pattern.compile(
			"public static final SimpleParticleType .*? = register.*\\(\"(.*)\"");
	private static final Pattern map_name_pattern = Pattern.compile("(.*):");
	private static final Pattern class_name_pattern = Pattern.compile(" {2}- ParticleTypes\\.(.*)");
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
}
