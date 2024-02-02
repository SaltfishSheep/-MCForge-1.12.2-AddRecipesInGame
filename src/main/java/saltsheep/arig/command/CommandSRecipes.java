package saltsheep.arig.command;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import saltsheep.arig.AddRecipesInGame;
import saltsheep.arig.data.RecipesGlobalData;
import saltsheep.arig.data.RecipesSaveData;
import saltsheep.arig.gui.ContainerRecipes;
import saltsheep.arig.gui.GuiHandler;
import saltsheep.arig.recipe.IRecipeSheep;
import saltsheep.arig.recipe.IngredientCustomFactory;
import saltsheep.arig.recipe.RecipesRegistryHandler;

public class CommandSRecipes extends CommandBase {

	@Override
	public String getName() {
		return "srecipes";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "§c/srecipes add <RecipeMode> <RecipeName> (IngredientMode) (isIgnoreDamage)添加新配方\n§c/srecipes del <RecipeName>删除某个配方\n§c/srecipes view <RecipeName> (PlayerName)预览某个配方\n§c/srecipes list列出所有已注册配方\n§c/srecipes canEdit <true/false> <key>关闭或开启配方添加功能\n§7Tips:当canEdit传入false，且原状态为true时，将会更改秘钥为传入<key>";
	}
	
	public int getRequiredPermissionLevel(){
        return 4;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if((args.length==1||args.length==3)&&args[0].equals("canEdit")) {
			if(args.length==1) {
				if(RecipesSaveData.getData().getCanEdit())
					sender.sendMessage(new TextComponentString("The recipes is already editable now."));
				else
					sender.sendMessage(new TextComponentString("The recipes is already not editable now."));
				return;
			}
			if(args[1].equals("false")) {
				sender.sendMessage(new TextComponentString("The recipes is not editable now."));
				if(RecipesSaveData.getData().getCanEdit()) {
					RecipesSaveData.getData().setCanEdit(false);
					RecipesSaveData.getData().setEditKey(args[2]);
					sender.sendMessage(new TextComponentString("Succesful set the key to - "+args[2]));
				}else{
					sender.sendMessage(new TextComponentString("Because you are already lock it,you can't change the key!\n§7Tips:This feature is to prevent tampering with key after locking."));
				}
				return;
			}else if(args[1].equals("true")) {
				if(!RecipesSaveData.getData().getCanEdit()) {
					if(args[2].equals(RecipesSaveData.getData().getEditKey())) {
						RecipesSaveData.getData().setCanEdit(true);
						sender.sendMessage(new TextComponentString("The recipes is editable now."));
						return;
					}else {
						sender.sendMessage(new TextComponentString("Warning!The key is not correct."));
					}
				}else {
					sender.sendMessage(new TextComponentString("You are already unlock it,if you want to change key,plz use \"false\"."));
				}
				return;
			}else {
				sender.sendMessage(new TextComponentString(this.getUsage(sender)));
				return;
			}
		}
		if(RecipesSaveData.getData().getCanEdit()&&RecipesGlobalData.getCanEdit()) {
			if(args.length>=3&&args.length<=5&&args[0].equals("add")) {
				if(!(sender instanceof EntityPlayerMP)) {
					sender.sendMessage(new TextComponentString("Warning!You can't edit recipes without player,because it has no gui."));
					return;
				}
				if(RecipesSaveData.getData().getRecipe(args[2])!=null) {
					sender.sendMessage(new TextComponentString("Warning!Already has this recipe."));
					return;
				}
				int recipeMode = 0;
				switch(args[1]) {
				case "shaped":
					recipeMode = IRecipeSheep.SHAPED;
					break;
				case "shapeless":
					recipeMode = IRecipeSheep.SHAPELESS;
					break;
				default:
					sender.sendMessage(new TextComponentString("Warning!Unknown recipe mode!."));
					return;
				}
				String recipeName = args[2];
				int ingredientMode = IngredientCustomFactory.RIGID;
				if(args.length>=4) {
					switch(args[3]) {
					case "rigid":
						ingredientMode = IngredientCustomFactory.RIGID;
						break;
					case "lenient":
						ingredientMode = IngredientCustomFactory.LENIENT;
						break;
					case "onlyNBT":
						ingredientMode = IngredientCustomFactory.ONLY_NBT;
						break;
					case "onlyItem":
						ingredientMode = IngredientCustomFactory.ONLY_ITEM;
						break;
					case "onlyName":
						ingredientMode = IngredientCustomFactory.ONLY_NAME;
						break;
					default:
						sender.sendMessage(new TextComponentString("Warning!Unknown ingredient mode!."));
						return;
					}
				}
				boolean isIgnoreDamage = false;
				if(args.length>=5) {
					if(args[4].equals("true"))
						isIgnoreDamage = true;
					else if(!args[4].equals("false")) {
						sender.sendMessage(new TextComponentString("Warning!You must use 'true' or 'false'"));
						return;
					}
				}
				GuiHandler.openGui((EntityPlayer) sender, recipeMode, recipeName, ingredientMode, isIgnoreDamage);
			}else if(args.length==2&&(args[0].equals("del"))) {
				if(RecipesSaveData.getData().list().contains(args[1])) {
					if(RecipesRegistryHandler.deleteRecipeInSaveByName(args[1]))
						sender.sendMessage(new TextComponentString("Delete successful."));
					else
						sender.sendMessage(new TextComponentString("Warning!Failed to delete the recipe,how can that be!"));
				}else
					sender.sendMessage(new TextComponentString("Warning!Null has such recipe."));
			}else if(args.length>=2&&args.length<=3&&(args[0].equals("view"))){
				if(RecipesSaveData.getData().list().contains(args[1])) {
					EntityPlayer player = null;
					if(sender instanceof EntityPlayerMP) {
						player = (EntityPlayer) sender;
					}
					if(args.length==3) {
						if(AddRecipesInGame.getMCServer().getPlayerList().getPlayerByUsername(args[2])!=null)
							player = AddRecipesInGame.getMCServer().getPlayerList().getPlayerByUsername(args[2]);
						else {
							sender.sendMessage(new TextComponentString("Warning!Unknown player."));
							return;
						}
					}
					if(player == null) {
						sender.sendMessage(new TextComponentString("Warning!You tried to open gui for noplayer."));
						return;
					}
					GuiHandler.openGui(player, RecipesSaveData.getData().getRecipe(args[1]).mode()+4, args[1], 0, RecipesSaveData.getData().isRecipeIgnoreDamage(args[1]));
					((ContainerRecipes)player.openContainer).setItemStacks(RecipesSaveData.getData().getRecipeItemStacks(args[1]));
					((ContainerRecipes)player.openContainer).isView = true;
				}else
					sender.sendMessage(new TextComponentString("Warning!Null has such recipe."));
			}else if(args.length==1&&args[0].equals("list")) {
				StringBuilder builder = new StringBuilder();
				for(String recipe : RecipesSaveData.getData().list()) {
					builder.append(recipe);
					builder.append(',');
				}
				sender.sendMessage(new TextComponentString(builder.toString()));
			}else {
				sender.sendMessage(new TextComponentString(this.getUsage(sender)));
			}
		}else
			sender.sendMessage(new TextComponentString("You can't edit any recipes,it's lock now."));
	}
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos){
		if(args.length==1)
			return Lists.newArrayList("add","del","list","canEdit","view");
		if(args.length==2) {
			if(args[0].equals("add")) {
				return Lists.newArrayList("shaped","shapeless");
			}else if(args[0].equals("del")||args[0].equals("view")) {
				return RecipesSaveData.getData().list();
			}else if(args[0].equals("canEdit"))
				return Lists.newArrayList("true","false");
		}
		if(args.length==3&&args[0].equals("view"))
			return Lists.newArrayList(AddRecipesInGame.getMCServer().getOnlinePlayerNames());
		if(args.length==4&&args[0].equals("add"))
			return RecipesRegistryHandler.getFactoriesTypeWithinEmpty();
		if(args.length==5&&args[0].equals("add"))
			return Lists.newArrayList("true","false");
		return Lists.newArrayList("UNKNOWN");
	}

}
