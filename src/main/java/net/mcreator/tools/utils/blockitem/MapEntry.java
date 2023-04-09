package net.mcreator.tools.utils.blockitem;

public class MapEntry {
	private String className, registryName;

	public MapEntry() {
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
}
