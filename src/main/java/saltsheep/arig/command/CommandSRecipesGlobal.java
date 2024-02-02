package saltsheep.arig.command;

import java.lang.reflect.InvocationTargetException;
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
import saltsheep.arig.recipe.IngredientCustomFactory;
import saltsheep.arig.recipe.RecipesRegistryHandler;

public class CommandSRecipesGlobal extends CommandBase {

	@Override
	public String getName() {
		return "srecipesGlobal";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "§c/srecipesGlobal add <RecipeMode> <RecipeName> (IngredientMode) (isIgnoreDamage)添加新配方\n§c/srecipesGlobal del <RecipeName>删除某个配方\n§c/srecipesGlobal view <RecipeName> (PlayerName)预览某个配方\n§c/srecipesGlobal list列出所有已注册配方\n§c/srecipesGlobal remove add <RemoveName> (IngredientMode) (isIgnoreDamage)禁止手上物品的正常合成，并命名该次删除操作\n§c/srecipesGlobal remove del <RemoveName>撤销删除操作\n§c/srecipesGlobal remove list列出所有删除操作\n§c/srecipesGlobal removeAll <true/false>是否禁止所有正常合成（默认false）\n§c/srecipesGlobal canEdit <true/false> <key>关闭或开启配方添加功能\n§7Tips:当canEdit传入false，且原状态为true时，将会更改秘钥为传入<key>\n§7全局化操作在不重启游戏的情况下不一定实时变更配方，修改后建议重启游戏";
	}
	
	public int getRequiredPermissionLevel(){
        return 4;
    }

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if((args.length==1||args.length==3)&&args[0].equals("canEdit")) {
			if(args.length==1) {
				if(RecipesGlobalData.getCanEdit())
					sender.sendMessage(new TextComponentString("The recipes is already editable now."));
				else
					sender.sendMessage(new TextComponentString("The recipes is already not editable now."));
				return;
			}
			if(args[1].equals("false")) {
				sender.sendMessage(new TextComponentString("The recipes is not editable now."));
				if(RecipesGlobalData.getCanEdit()) {
					RecipesGlobalData.setCanEdit(false);
					RecipesGlobalData.setEditKey(args[2]);
					sender.sendMessage(new TextComponentString("Succesful set the key to - "+args[2]));
				}else{
					sender.sendMessage(new TextComponentString("Because you are already lock it,you can't change the key!\n§7Tips:This feature is to prevent tampering with key after locking."));
				}
				return;
			}else if(args[1].equals("true")) {
				if(!RecipesGlobalData.getCanEdit()) {
					if(args[2].equals(RecipesGlobalData.getEditKey())) {
						RecipesGlobalData.setCanEdit(true);
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
		if(RecipesGlobalData.getCanEdit()&&RecipesSaveData.getData().getCanEdit()) {
			if(args.length>=3&&args.length<=5&&args[0].equals("add")) {
				if(!(sender instanceof EntityPlayerMP)) {
					sender.sendMessage(new TextComponentString("Warning!You can't edit recipes without player,because it has no gui."));
					return;
				}
				if(RecipesGlobalData.getRecipe(args[2])!=null) {
					sender.sendMessage(new TextComponentString("Warning!Already has this recipe."));
					return;
				}
				int recipeMode = 0;
				switch(args[1]) {
				case "shaped":
					recipeMode = GuiHandler.RECIPE_SHAPED_GLOBAL;
					break;
				case "shapeless":
					recipeMode = GuiHandler.RECIPE_SHAPELESS_GLOBAL;
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
				if(RecipesGlobalData.list().contains(args[1])) {
					if(RecipesRegistryHandler.deleteRecipeInGlobalByName(args[1]))
						sender.sendMessage(new TextComponentString("Delete successful."));
					else
						sender.sendMessage(new TextComponentString("Warning!Failed to delete the recipe,how can that be!"));
				}else
					sender.sendMessage(new TextComponentString("Warning!Null has such recipe."));
			}else if(args.length>=2&&args.length<=3&&(args[0].equals("view"))){
				if(RecipesGlobalData.list().contains(args[1])) {
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
					GuiHandler.openGui(player, RecipesGlobalData.getRecipe(args[1]).mode()+4, args[1], 0, RecipesGlobalData.isRecipeIgnoreDamage(args[1]));
					((ContainerRecipes)player.openContainer).setItemStacks(RecipesGlobalData.getRecipeItemStacks(args[1]));
					((ContainerRecipes)player.openContainer).isView = true;
				}else
					sender.sendMessage(new TextComponentString("Warning!Null has such recipe."));
			}else if(args.length==1&&args[0].equals("list")) {
				StringBuilder builder = new StringBuilder();
				for(String recipe : RecipesGlobalData.list()) {
					builder.append(recipe);
					builder.append(',');
				}
				sender.sendMessage(new TextComponentString(builder.toString()));
			}else if(args.length>=1&&args.length<=2&&args[0].equals("removeAll")){
				if(args.length==1) {
					if(RecipesGlobalData.isRemoveAll())
						sender.sendMessage(new TextComponentString("The default recipes is already removed now."));
					else
						sender.sendMessage(new TextComponentString("The default recipes isn't all removed now."));
					return;
				}
				if(args.length==2&&args[1].equals("true"))
					RecipesGlobalData.setRemoveAll(true);
				else if(args.length==2&&args[1].equals("false"))
					RecipesGlobalData.setRemoveAll(false);
				else
					sender.sendMessage(new TextComponentString(this.getUsage(sender)));
			}else if(args.length>=2&&args.length<=5&&args[0].equals("remove")){
				if(args.length==2&&args[1].equals("list")) {
					StringBuilder builder = new StringBuilder();
					for(String remove : RecipesGlobalData.listRemove()) {
						builder.append(remove);
						builder.append(',');
					}
					sender.sendMessage(new TextComponentString(builder.toString()));
				}else if(args.length>=3&&args[1].equals("add")) {
					if(RecipesGlobalData.getRemove(args[2])!=null) {
						sender.sendMessage(new TextComponentString("Warning!Already has this remove."));
						return;
					}
					String removeName = args[2];
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
					try {
						RecipesGlobalData.addRemove(removeName, RecipesRegistryHandler.getFactory(ingredientMode).create(isIgnoreDamage, ((EntityPlayer)sender).getHeldItemMainhand()));
						sender.sendMessage(new TextComponentString("Add remove successful,it will take effect after you restart your game."));
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						AddRecipesInGame.printError(e);
					}
				}else if(args.length==3&&args[1].equals("del")) {
					if(RecipesGlobalData.listRemove().contains(args[2])) {
						if(RecipesGlobalData.delRemove(args[2]))
							sender.sendMessage(new TextComponentString("Delete remove successful,it will take effect after you restart your game."));
						else
							sender.sendMessage(new TextComponentString("Warning!Failed to delete the remove,how can that be!"));
					}else
						sender.sendMessage(new TextComponentString("Warning!Null has such remove."));
				}else
					sender.sendMessage(new TextComponentString(this.getUsage(sender)));
			}else{
				sender.sendMessage(new TextComponentString(this.getUsage(sender)));
			}
		}else
			sender.sendMessage(new TextComponentString("You can't edit any recipes,it's lock now."));
	}
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos){
		if(args.length==1)
			return Lists.newArrayList("add","del","list","canEdit","view","removeAll","remove");
		if(args.length==2) {
			if(args[0].equals("add"))
				return Lists.newArrayList("shaped","shapeless");
			else if(args[0].equals("del")||args[0].equals("view"))
				return RecipesGlobalData.list();
			else if(args[0].equals("canEdit"))
				return Lists.newArrayList("true","false");
			else if(args[0].equals("remove"))
				return Lists.newArrayList("add","del","list");
			else if(args[0].equals("removeAll"))
				return Lists.newArrayList("true","false");
		}
		if(args.length==3) {
			if(args[0].equals("view"))
				return Lists.newArrayList(AddRecipesInGame.getMCServer().getOnlinePlayerNames());
			else if(args[0].equals("remove")&&args[1].equals("del"))
				return RecipesGlobalData.listRemove();
		}
		if(args.length==4) {
			if(args[0].equals("add"))
				return RecipesRegistryHandler.getFactoriesTypeWithinEmpty();
			else if(args[0].equals("remove")&&args[1].equals("add"))
				return RecipesRegistryHandler.getFactoriesTypeWithinEmpty();
		}
		if(args.length==5) {
			if(args[0].equals("add"))
				return Lists.newArrayList("true","false");
			else if(args[0].equals("remove")&&args[1].equals("add"))
				return Lists.newArrayList("true","false");
		}
		return Lists.newArrayList("UNKNOWN");
	}

}
