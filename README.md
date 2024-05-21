# MCreator Dev Tools
A set of tools to aid [MCreator](https://github.com/MCreator/MCreator) development.

## So what does it do?
While this project isn't much on its own, it can significantly reduce the time you spend checking MCreator data lists and mappings - which is at this point in time the only feature. Feel free to use it!

In case you have any of your own scripts for MCreator development lying around, you're welcome to open a pull request and we'll add them here for others to use.

### Setup

In order for this tool to work, make a new file called `gradle.properties` with the following contents:

```
mcreator_path=<path to MCreator core Gradle project directory>
```

### Resource structure
Resources folder has a fixed structure, but you may not need all the files for it to work (more on that in specific chapters below). In `img` you should put block/item images from MCreator core you are updating. In the `lists` folder you put the MCreator list you are testing and name it `mcreator`. File named `minecraft` is where you put stripped Minecraft list of entries. The maps folder takes the corresponding map named `mcreator`.

### Checking options
To use specific checks you change the `WHAT` field inside `ComparisonTool.java` to one of the following options:
#### LIST
* Requires both files in the `list` folder and compares their contents
* Both files need to be in the same list format (one item per line, preferably only item names without dashes or similair decorators)
#### MAP
* Requires both files inside the `list` folder and the `mcreator` map inside the `mappings` folder.
* For list formatting check the second bullet from [LIST](#list).
* Mappings are formatted one key/value pair per line similarly to lists. Entries are separated by a colon followed by a space (`k: v`).
#### BLOCK_ITEM
Copy the `img` folder from MCreator core to the `resources/img` folder. This will allow the tool to compare the image names of blocks and items from the list to the ones in the `img` folder.
Furthermore, copy the `Blocks.java` and `Items.java` files from a decompiled and deobfuscated (with official mappings) Minecraft jar to the `resources/lists/blockitem` folder.
Lastly copy the contents of MCreator mappings to the `resources/maps/mcreator` file, as well as the datalist to the `resources/lists/mcreator` file.
#### BLOCK_ITEM_LEGACY
Will not document as it's here only for legacy reasons. Will likely be removed in the future.

### Items based on blocks
Some items are based on blocks, so they are listed as blocks in MCreator to avoid duplicates. To avoid false positives, you can toggle the `ITEMS_BASED_ON_BLOCKS` field in `ComparisonTool.java` to `true` to check them against the block list instead.