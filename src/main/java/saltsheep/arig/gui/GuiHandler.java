package saltsheep.arig.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import saltsheep.arig.AddRecipesInGame;
import saltsheep.arig.recipe.IRecipeSheep;

public class GuiHandler implements IGuiHandler {

	public static final int RECIPE_SHAPED = IRecipeSheep.SHAPED;
	public static final int RECIPE_SHAPELESS = IRecipeSheep.SHAPELESS;
	public static final int RECIPE_SHAPED_GLOBAL = IRecipeSheep.SHAPED+2;
	public static final int RECIPE_SHAPELESS_GLOBAL = IRecipeSheep.SHAPELESS+2;
	public static final int RECIPE_SHAPED_VIEW = RECIPE_SHAPED+4;
	public static final int RECIPE_SHAPELESS_VIEW = RECIPE_SHAPELESS+4;
	public static final int RECIPE_SHAPED_VIEW_GLOBAL = RECIPE_SHAPED_GLOBAL+4;
	public static final int RECIPE_SHAPELESS_VIEW_GLOBAL = RECIPE_SHAPELESS_GLOBAL+4;
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID==RECIPE_SHAPED)
			return new ContainerRecipes(player);
		if(ID==RECIPE_SHAPELESS)
			return new ContainerRecipes(player);
		if(ID==RECIPE_SHAPED_GLOBAL)
			return new ContainerRecipes(player);
		if(ID==RECIPE_SHAPELESS_GLOBAL)
			return new ContainerRecipes(player);
		if(ID==RECIPE_SHAPED_VIEW)
			return new ContainerRecipes(player);
		if(ID==RECIPE_SHAPELESS_VIEW)
			return new ContainerRecipes(player);
		if(ID==RECIPE_SHAPED_VIEW_GLOBAL)
			return new ContainerRecipes(player);
		if(ID==RECIPE_SHAPELESS_VIEW_GLOBAL)
			return new ContainerRecipes(player);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID){
		case RECIPE_SHAPED:
			if(x==1)
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,true);
			else
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,false);
		case RECIPE_SHAPELESS:
			if(x==1)
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,true);
			else
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,false);
		case RECIPE_SHAPED_GLOBAL:
			if(x==1)
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,true);
			else
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,false);
		case RECIPE_SHAPELESS_GLOBAL:
			if(x==1)
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,true);
			else
				return new GuiContainerRecipes(new ContainerRecipes(player),ID,false);
		case RECIPE_SHAPED_VIEW:
			if(x==1)
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,true);
			else
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,false);
		case RECIPE_SHAPELESS_VIEW:
			if(x==1)
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,true);
			else
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,false);
		case RECIPE_SHAPED_VIEW_GLOBAL:
			if(x==1)
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,true);
			else
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,false);
		case RECIPE_SHAPELESS_VIEW_GLOBAL:
			if(x==1)
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,true);
			else
				return new GuiContainerRecipesView(new ContainerRecipes(player),ID-4,false);
		default:
			return null;
		}
	}
	
	public static void openGui(EntityPlayer player,int recipeMode,String recipeName,int ingredientMode,boolean isIgnoreDamage) {
		if(isIgnoreDamage)
			player.openGui(AddRecipesInGame.instance, recipeMode, player.world, 1, 0, 0);
		else
			player.openGui(AddRecipesInGame.instance, recipeMode, player.world, 0, 0, 0);
		if(!(player.openContainer instanceof ContainerRecipes))
			return;
		((ContainerRecipes)player.openContainer).recipeName=recipeName;
		if(recipeMode>3)
			recipeMode -= 4;
		((ContainerRecipes)player.openContainer).recipeMode=recipeMode;
		((ContainerRecipes)player.openContainer).ingredientMode=ingredientMode;
		((ContainerRecipes)player.openContainer).isIgnoreDamage=isIgnoreDamage;
	}

}
