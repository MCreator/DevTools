package net.mcreator.tools.utils.blockitem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BlockItemUtils {
    public static final Pattern BLOCK_CLASS_PATTERN = Pattern.compile("public static final Block ([A-Z0-9_].*?)[; ]");
    public static final Pattern ITEM_CLASS_PATTERN = Pattern.compile("public static final Item ([A-Z0-9_].*?)[; ]");
    // Group 1 is collection type, group 2 is field name
    public static final Pattern BLOCK_COLLECTION_PATTERN = Pattern.compile("public static final (\\w+)<Block> ([A-Z0-9_]+) =");
    public static final Pattern ITEM_COLLECTION_PATTERN = Pattern.compile("public static final (\\w+)<Item> ([A-Z0-9_]+) =");
    private static final List<String> COLLECTION_COLORS = List.of("white", "orange", "magenta", "lightBlue", "yellow",
            "lime", "pink", "gray", "lightGray", "cyan", "purple", "blue", "brown", "green", "red", "black");
    private static final List<String> COLLECTION_COPPER_STATES = List.of("unaffected", "exposed", "weathered", "oxidized");
    // Fields where registry name does not match the field name in the MC code
    private static final Map<String, String> FIELD_REGISTRY_NAME_OVERRIDES = Map.of(
            "POTTED_AZALEA", "potted_azalea_bush",
            "POTTED_FLOWERING_AZALEA", "potted_flowering_azalea_bush",
            "DRY_SHORT_GRASS", "short_dry_grass",
            "DRY_TALL_GRASS", "tall_dry_grass",
            "CUT_STANDSTONE_SLAB", "cut_sandstone_slab");
    private static final Pattern name_pattern = Pattern.compile("- (.*):");
    private static final Pattern read_name_pattern = Pattern.compile(" {2}readable_name: \"(.*)\"");
    private static final Pattern texture_pattern = Pattern.compile(" {2}texture: (.*)");
    private static final Pattern description_pattern = Pattern.compile(" {2}description: \"(.*)\"");
    private static final Pattern subtypes_pattern = Pattern.compile(" {2}subtypes: (.*)");
    private static final Pattern type_pattern = Pattern.compile(" {2}type: (.*)");

    public static ArrayList<String> toRegistryNames(ArrayList<String> fields) {
        return fields.stream().map(e -> FIELD_REGISTRY_NAME_OVERRIDES.getOrDefault(e, e.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void expandCollections(ArrayList<String> types, ArrayList<String> fields, ArrayList<String> classes,
                                         ArrayList<String> registry) {
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            String name = field.toLowerCase(Locale.ROOT);
            switch (types.get(i)) {
                case "ColorCollection" -> {
                    for (String color : COLLECTION_COLORS) {
                        classes.add(field + "." + color + "()");
                        registry.add(color.replaceAll("([A-Z])", "_$1").toLowerCase(Locale.ROOT) + "_"
                                + name.replaceFirst("^dyed_", ""));
                    }
                }
                case "WeatheringCopperCollection" -> {
                    for (String waxed : List.of("weathering", "waxed")) {
                        for (String state : COLLECTION_COPPER_STATES) {
                            boolean unaffected = state.equals("unaffected");
                            classes.add(field + "." + waxed + "()." + state + "()");
                            // Only unaffected copper block has _block suffix
                            registry.add((waxed.equals("waxed") ? "waxed_" : "") + (unaffected ? "" : state + "_")
                                    + (name.equals("copper_block") && !unaffected ? "copper" : name));
                        }
                    }
                }
                default -> System.out.println(
                        "WARNING: Unknown collection type " + types.get(i) + " of " + field + ", its members are skipped");
            }
        }
    }

    public static LinkedHashMap<String, BlockItemEntry> parseList(ArrayList<String> list) {
        LinkedHashMap<String, BlockItemEntry> entryList = new LinkedHashMap<>();

        String key = null;
        BlockItemEntry blockItemEntry = new BlockItemEntry();

        for (String line : list) {
            Matcher name = name_pattern.matcher(line);
            if (name.find())
                key = name.group(1);
            else {
                Matcher readable_name = read_name_pattern.matcher(line);
                if (readable_name.find())
                    blockItemEntry.setName(readable_name.group(1));
                else {
                    Matcher texture = texture_pattern.matcher(line);
                    if (texture.find())
                        blockItemEntry.setTexture(texture.group(1));
                    else {
                        Matcher description = description_pattern.matcher(line);
                        if (description.find())
                            blockItemEntry.setDescription(description.group(1));
                        else {
                            Matcher subtypes = subtypes_pattern.matcher(line);
                            if (subtypes.find())
                                blockItemEntry.setSubtypes(subtypes.group(1).equals("true"));
                            else {
                                Matcher type = type_pattern.matcher(line);
                                if (type.find())
                                    switch (type.group(1)) {
                                        case "block" -> blockItemEntry.setType(Type.block);
                                        case "item" -> blockItemEntry.setType(Type.item);
                                        case "block_without_item" -> blockItemEntry.setType(Type.block_without_item);
                                    }
                                else {
                                    if (key != null)
                                        entryList.put(key, blockItemEntry);
                                    key = null;
                                    blockItemEntry = new BlockItemEntry();
                                }
                            }
                        }
                    }
                }
            }
        }

        if (key != null)
            entryList.put(key, blockItemEntry);

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

    public static void compareBIMaps(LinkedHashMap<String, BlockItemEntry> entryList,
                                     LinkedHashMap<String, MapEntry> entryMap, ArrayList<String> blocks, ArrayList<String> blocksreg,
                                     ArrayList<String> items, ArrayList<String> itemsreg, ArrayList<String> icons) {

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
            if (!classes.contains(e) && !(e.startsWith("Items.") && classes.contains(
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
                .filter(e -> entryMap.get(e.getKey()) != null)
                .filter(e -> (e.getValue().getType() == Type.block || e.getValue().getType() == Type.block_without_item))
                .map(e -> entryMap.get(e.getKey()).getRegistryName())
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
                .filter(e -> entryMap.get(e.getKey()) != null)
                .filter(e -> (e.getValue().getType() == Type.item || e.getValue().getType() == Type.block)).map(e -> {
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
			if (!itemsreg.contains(e) && !e.startsWith("#"))
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
