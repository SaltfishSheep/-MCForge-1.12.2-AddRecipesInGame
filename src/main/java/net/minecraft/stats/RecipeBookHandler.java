package net.minecraft.stats;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class RecipeBookHandler {

	public static void checkAndRemove(RecipeBook book) {
		for(int eachRecipeUnlock=0;eachRecipeUnlock<book.recipes.length();eachRecipeUnlock++) {
			IRecipe vanilla = CraftingManager.REGISTRY.getObjectById(eachRecipeUnlock);
	        IRecipe byMod = ((net.minecraftforge.registries.ForgeRegistry<IRecipe>)net.minecraftforge.fml.common.registry.ForgeRegistries.RECIPES).getValue(eachRecipeUnlock);
	        if(vanilla==null&&byMod==null) {
	        	book.recipes.clear(eachRecipeUnlock);
	            book.newRecipes.clear(eachRecipeUnlock);
	        }
		}
	}
	
}
