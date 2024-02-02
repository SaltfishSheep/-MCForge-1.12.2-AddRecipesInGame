package saltsheep.arig.recipe;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistryDetailsHandler {
	
	public static IForgeRegistry<IRecipe> registry = ForgeRegistries.RECIPES;
	
	/*Include the recipes registered by mc or other mod.*/
	public static List<ResourceLocation> getRegisteredRecipesName(){
		Set<ResourceLocation> ReLos = registry.getKeys();
		return Lists.newArrayList(ReLos);
	}
	
	/*Include the recipes registered by mc or other mod.*/
	public static IRecipe getRecipeByRL(ResourceLocation RL) {
		return registry.getValue(RL);
	}
	
	/*Include the recipes registered by mc or other mod.*/
	public static List<IRecipe> getRecipes(){
		return Lists.newArrayList(registry.getValuesCollection());
	}
	
}
