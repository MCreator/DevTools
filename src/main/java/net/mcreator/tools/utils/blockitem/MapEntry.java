package net.mcreator.tools.utils.blockitem;

import java.util.HashMap;
import java.util.Map;

public class MapEntry {
	private String className, registryName;

	public MapEntry() {
		this("", "");
	}

	public MapEntry(String className, String registryName) {
		this.className = className;
		this.registryName = registryName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRegistryName() {
		return registryName;
	}

	public void setRegistryName(String registryName) {
		this.registryName = registryName;
	}

	@Override public String toString() {
		return "MapEntry{" + "className='" + className + '\'' + ", registryName='" + registryName + '\'' + '}';
	}

	public static Map<String, String> getClassMap(Map<String, MapEntry> map) {
		Map<String, String> classMap = new HashMap<>();
		for (Map.Entry<String, MapEntry> entry : map.entrySet()) {
			classMap.put(entry.getKey(), entry.getValue().getClassName());
		}
		return classMap;
	}

	public static Map<String, String> getRegistryMap(Map<String, MapEntry> map) {
		Map<String, String> registryMap = new HashMap<>();
		for (Map.Entry<String, MapEntry> entry : map.entrySet()) {
			registryMap.put(entry.getKey(), entry.getValue().getRegistryName());
		}
		return registryMap;
	}
}
