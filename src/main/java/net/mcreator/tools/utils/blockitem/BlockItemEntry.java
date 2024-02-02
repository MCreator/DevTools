package net.mcreator.tools.utils.blockitem;

public class BlockItemEntry {
	private String name, texture, description;
	private Type type;
	private boolean subtypes;

	public BlockItemEntry(String name, String texture, String description, boolean subtypes, Type type) {
		this.name = name;
		this.texture = texture;
		this.description = description;
		this.subtypes = subtypes;
		this.type = type;
	}

	public BlockItemEntry() {
		this(null, null, null, false, null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean hasSubtypes() {
		return subtypes;
	}

	public void setSubtypes(boolean subtypes) {
		this.subtypes = subtypes;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override public String toString() {
		return "Entry{" + "name='" + name + '\'' + ", texture='" + texture + '\'' + ", description='" + description
				+ '\'' + ", type=" + type + ", subtypes=" + subtypes + '}';
	}
}

enum Type {
	block, item
}