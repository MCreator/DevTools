# MCreator Dev Tools
A set of tools to aid [MCreator](https://github.com/MCreator/MCreator) development.

## So what does it do?
While this project isn't much on its own, it can significantly reduce the time you spend checking MCreator data lists and mappings - which is at this point in time the only feature. Feel free to use it!

In case you have any of your own scripts for MCreator development laying around, you're welcome to open a pull request and we'll add them here for others to use.

## How do I use this?
### Prerequisites
For this to work, you need:
* a working MCreator installation (currently Windows only, default install location).
* Intellij Idea IDE with this repo open as a project (no gradle target/task for launching the ComparisonTool atm.)

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