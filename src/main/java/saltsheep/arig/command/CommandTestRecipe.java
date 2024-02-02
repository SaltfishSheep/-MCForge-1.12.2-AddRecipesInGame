package saltsheep.arig.command;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import saltsheep.arig.data.RecipesGlobalData;

public class CommandTestRecipe extends CommandBase {

	@Override
	public String getName() {
		return "testRecipe";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "§c/testRecipe";
	}
	
	public int getRequiredPermissionLevel()
    {
        return 4;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length==0) {
			sender.sendMessage(new TextComponentString(String.valueOf(RecipesGlobalData.isRemoveAll())));
			return;
		}
		if(args[0].equals("true"))
			RecipesGlobalData.setRemoveAll(true);
		else
			RecipesGlobalData.setRemoveAll(false);
		RecipesGlobalData.saveData();
	}
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos){
		return null;
	}

}
