package net.mcreator.tools;

import net.mcreator.tools.utils.DatalistUtils;
import net.mcreator.tools.utils.MappingUtils;
import net.mcreator.tools.utils.blockitem.BlockItemUtils;
import net.mcreator.tools.utils.blockitem.Entry;
import net.mcreator.tools.utils.blockitem.MapEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ComparisonTool {

	private static final Compare WHAT = Compare.BLOCK_ITEM;
	private static final boolean ITEM_TO_BLOCK_FALLBACK = true;

	public enum Compare {
		LIST, MAP, BLOCK_ITEM, BLOCK_ITEM_LEGACY, NONE
	}

	public static void main(String[] args) {
		ArrayList<String> mcr, min;
		switch (WHAT) {
		case LIST -> {
			mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
			min = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/minecraft"));
			DatalistUtils.compareLists(min, mcr);
		}
		case MAP -> {
			mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
			min = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/minecraft"));
			LinkedHashMap<String, String> mcrmap = MappingUtils.readSimpleMapFromFile(
					ClassLoader.getSystemClassLoader().getResource("maps/mcreator"));
			MappingUtils.compareSimpleMaps(mcr, mcrmap, min);
		}
		case BLOCK_ITEM -> {
			LinkedHashMap<String, Entry> bilist = BlockItemUtils.parseList(
					DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator")));

			LinkedHashMap<String, MapEntry> bimap = BlockItemUtils.parseMap(
					DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("maps/mcreator")));

			ArrayList<String> blocks = DatalistUtils.extractMatchListFromClass(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/Blocks.java"),
					DatalistUtils.BLOCK_CLASS_PATTERN);

			ArrayList<String> blocksreg = DatalistUtils.extractMatchListFromClass(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/Blocks.java"),
					DatalistUtils.BLOCK_REGISTRY_PATTERN);

			ArrayList<String> items = DatalistUtils.extractMatchListFromClass(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/Items.java"),
					DatalistUtils.ITEM_CLASS_PATTERN);

			ArrayList<String> itemsreg = DatalistUtils.extractMatchListFromClass(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/Items.java"),
					DatalistUtils.ITEM_REGISTRY_PATTERN);

			ArrayList<String> icons = DatalistUtils.listTextures("img");

			BlockItemUtils.compareBIMaps(bilist, bimap, blocks, blocksreg, items, itemsreg, icons,
					ITEM_TO_BLOCK_FALLBACK);
		}
		case BLOCK_ITEM_LEGACY -> {
			LinkedHashMap<String, Entry> bilist = BlockItemUtils.parseList(
					DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator")));

			LinkedHashMap<String, MapEntry> bimap = BlockItemUtils.parseMap(
					DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("maps/mcreator")));

			ArrayList<String> blocks = DatalistUtils.readListFromFile(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/blocks"));

			ArrayList<String> blocksreg = DatalistUtils.readListFromFile(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/blocksreg"));

			ArrayList<String> items = DatalistUtils.readListFromFile(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/items"));

			ArrayList<String> itemsreg = DatalistUtils.readListFromFile(
					ClassLoader.getSystemClassLoader().getResource("lists/blockitem/itemsreg"));

			ArrayList<String> icons = DatalistUtils.listTextures("img");

			BlockItemUtils.compareBIMaps(bilist, bimap, blocks, blocksreg, items, itemsreg, icons,
					ITEM_TO_BLOCK_FALLBACK);
		}
		default -> System.out.println("No comparison selected/DEBUG mode");
		}
	}
}
