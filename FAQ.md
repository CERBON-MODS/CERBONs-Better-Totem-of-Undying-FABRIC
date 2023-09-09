# Frequently asked questions

**Q: Can I use this mod in my modpack?**

A: Yes, you can.

**Q: Is there a Forge version?**

A: Yes, there is a forge version. You can get it at [Curseforge](https://www.curseforge.com/minecraft/mc-mods/better-totem-of-undying) or [Modrinth](https://modrinth.com/mod/better-totem-of-undying)

**Q: Will this mod be backported to versions below 1.18.2?**

A: No, I will not backport to versions below 1.18.2.

**Q: I have blacklisted a dimension/structure and it's not working. Why?**

A: Probably you have written the dimension/structure ID incorrectly. Please double-check if you have entered the correct mod ID and dimension/structure ID. Another possibility is that you haven't saved the file.

**Q: Is it possible to blacklist damage types?**

A: Yes, it is. You can do it by adding the damage types you want to the `bypasses_invulnerability` tag using a [datapack](https://minecraft.fandom.com/wiki/Tutorials/Creating_a_data_pack). It's located at `minecraft\tags\damage_type\bypasses_invulnerability.json`.

**Q: How do I add blocks to the "totem_cant_break" tag?**

A: You need to [create a datapack](https://minecraft.fandom.com/wiki/Tutorials/Creating_a_data_pack) and use the following path: `data/better_totem_of_undying/tags/blocks` and create at this location the file `totem_cant_break.json`. Then add to the json file the blocks you want.

**Q: How to add the custom effects to the totem?**

A: Cloth config does not support lists inside lists in their GUI, therefore you will need to open the config file using the Notepad app.

Find CUSTOM EFFECTS list in the config file and then you will do the following: 

Within the list, you should provide another list using curly braces, with the first parameter being the damage type, the second parameter being the effect that will be granted, the third parameter the duration of the effect in ticks, and the fourth parameter the effect amplifier. If you want the effect to always be granted, use the damage type as "any". 

Example: [["minecraft:out_of_world", "minecraft:blindness", "800", "0"]]. In this example, if the player dies in the void, the totem will grant the level 1 blindness effect with a duration of 40 seconds.

Note that even the numbers are passed as strings, so you need to write "800" and not 800.

**Q: Where can I download the mod?**

A: The only official places to download the mod are at [Curseforge](https://www.curseforge.com/minecraft/mc-mods/better-totem-of-undying-fabric) and [Modrinth](https://modrinth.com/mod/better-totem-of-undying-fabric) websites.
