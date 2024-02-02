package saltsheep.arig.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandManager {

	public static void register(FMLServerStartingEvent event) {
		//event.registerServerCommand(new CommandTestRecipe());
		event.registerServerCommand(new CommandSRecipes());
		event.registerServerCommand(new CommandSRecipesGlobal());
	}

}
