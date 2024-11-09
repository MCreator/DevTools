package net.mcreator.tools;

import net.mcreator.tools.utils.DatalistUtils;
import net.mcreator.tools.utils.MappingUtils;
import net.mcreator.tools.utils.ParticleUtils;
import net.mcreator.tools.utils.blockitem.BlockItemEntry;
import net.mcreator.tools.utils.blockitem.BlockItemUtils;
import net.mcreator.tools.utils.blockitem.MapEntry;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ComparisonTool {

    // Use regex (?<!;)\n to compact java source code for parsers

    private static final Compare WHAT = Compare.BLOCKSTATEPROPERTIES;

    public enum Compare {
        LIST, MAP, BLOCK_ITEM, SOUND_MAP, ENTITY_MAP, ADVANCEMENT_MAP, PARTICLE_MAP, SCREENS, BLOCKSTATEPROPERTIES
    }

    @SuppressWarnings("DataFlowIssue")
    public static void main(String[] args) {
        ArrayList<String> mcr, mc;
        switch (WHAT) {
            case LIST -> {
                mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
                mc = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/minecraft"));
                DatalistUtils.compareLists(mc, mcr);
            }
            case MAP -> {
                mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
                mc = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/minecraft"));
                LinkedHashMap<String, String> mcrmap = MappingUtils.readSimpleMapFromFile(
                        ClassLoader.getSystemClassLoader().getResource("maps/mcreator"));
                MappingUtils.compareSimpleMaps(mcr, mcrmap, mc);
            }
            case SOUND_MAP -> {
                mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
                mc = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/SoundEvents.java"),
                        DatalistUtils.SOUNDS_CLASS_PATTERN);
                LinkedHashMap<String, String> mcrmap = MappingUtils.readSimpleMapFromFile(
                        ClassLoader.getSystemClassLoader().getResource("maps/mcreator"));
                MappingUtils.compareSimpleMaps(mcr, mcrmap, mc);
            }
            case ENTITY_MAP -> {
                mcr = DatalistUtils.readListFromFileComplex(
                        ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
                ArrayList<ArrayList<String>> mcs = DatalistUtils.extractEntityListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/entity/EntityType.java"));
                ArrayList<LinkedHashMap<String, String>> mcrmap = MappingUtils.readEntityMapFromFile(
                        ClassLoader.getSystemClassLoader().getResource("maps/mcreator"));
                MappingUtils.compareEntityMaps(mcr, mcrmap, mcs);
            }
            case BLOCKSTATEPROPERTIES -> {
                mcr = DatalistUtils.readListFromFileComplex(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));

                LinkedHashMap<String, String> mcrmap = new LinkedHashMap<>();
                MappingUtils.readComplexMapFromFile(
                                ClassLoader.getSystemClassLoader().getResource("maps/mcreator"), 0)
                        .forEach((key, value) -> mcrmap.put(key, value.replace("BlockStateProperties.", "")));
                mc = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/BlockStateProperties.java"),
                        DatalistUtils.BLOCKSTATEPROPERTY_CLASS_PATTERN);
                MappingUtils.compareSimpleMaps(mcr, mcrmap, mc);

                LinkedHashMap<String, String> mcrmap2 = new LinkedHashMap<>();
                MappingUtils.readComplexMapFromFile(
                                ClassLoader.getSystemClassLoader().getResource("maps/mcreator"), 1)
                        .forEach((key, value) -> mcrmap2.put(key, value.replace("BlockStateProperties.", "")));
                mc = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/BlockStateProperties.java"),
                        DatalistUtils.BLOCKSTATEPROPERTY_CLASS_PATTERN, 2).stream().map(e -> e.replaceAll("\"", "")
                        .trim()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                MappingUtils.compareSimpleMaps(mcr, mcrmap2, mc);
            }
            case BLOCK_ITEM -> {
                LinkedHashMap<String, BlockItemEntry> bilist = BlockItemUtils.parseList(
                        DatalistUtils.readFileAsList(ClassLoader.getSystemClassLoader().getResource("lists/mcreator")));

                LinkedHashMap<String, MapEntry> bimap = BlockItemUtils.parseMap(
                        DatalistUtils.readFileAsList(ClassLoader.getSystemClassLoader().getResource("maps/mcreator")));

                ArrayList<String> blocks = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/Blocks.java"),
                        BlockItemUtils.BLOCK_CLASS_PATTERN);

                ArrayList<String> blocksreg = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/Blocks.java"),
                        BlockItemUtils.BLOCK_REGISTRY_PATTERN);

                ArrayList<String> items = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/Items.java"),
                        BlockItemUtils.ITEM_CLASS_PATTERN);

                ArrayList<String> itemsreg = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/Items.java"),
                        BlockItemUtils.ITEM_REGISTRY_PATTERN);

                List<String> blockItems = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/Items.java"),
                        BlockItemUtils.BLOCKITEM_REGISTRY_PATTERN).stream().map(String::toLowerCase).toList();

                itemsreg.addAll(blockItems);

                ArrayList<String> icons = DatalistUtils.listTextures("img");

                BlockItemUtils.compareBIMaps(bilist, bimap, blocks, blocksreg, items, itemsreg, icons);
            }
            case ADVANCEMENT_MAP -> {
                mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
                LinkedHashMap<String, String> mcrmap = MappingUtils.readSimpleMapFromFile(
                        ClassLoader.getSystemClassLoader().getResource("maps/mcreator"));
                mc = DatalistUtils.listFilesInFoldersAsRegistryEntries("lists/classes/advancements");
                MappingUtils.compareSimpleMaps(mcr, mcrmap, mc);
            }
            case PARTICLE_MAP -> {
                mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));

                LinkedHashMap<String, MapEntry> particleMap = ParticleUtils.parseMap(
                        DatalistUtils.readFileAsList(ClassLoader.getSystemClassLoader().getResource("maps/mcreator")));

                ArrayList<String> particleClasses = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/ParticleTypes.java"),
                        ParticleUtils.PARTICLE_CLASS_PATTERN);

                ArrayList<String> particleRegistry = DatalistUtils.extractMatchListFromClass(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/ParticleTypes.java"),
                        ParticleUtils.PARTICLE_REGISTRY_PATTERN);

                MappingUtils.compareRegistryMaps(mcr, particleMap, particleClasses, particleRegistry);
            }
            case SCREENS -> {
                mcr = DatalistUtils.readListFromFile(ClassLoader.getSystemClassLoader().getResource("lists/mcreator"));
                mc = DatalistUtils.loadListFromFileStructure(
                        ClassLoader.getSystemClassLoader().getResource("lists/classes/src/net/minecraft/"),
                        DatalistUtils.SCREENS_CLASS_PATTERN);
                LinkedHashMap<String, String> mcrmap = MappingUtils.readSimpleMapFromFile(
                        ClassLoader.getSystemClassLoader().getResource("maps/mcreator"));
                MappingUtils.compareSimpleMaps(mcr, mcrmap, mc);
            }
            default -> System.out.println("No comparison selected/DEBUG mode");
        }
    }
}
