package saltsheep.arig.recipe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.stats.RecipeBookHandler;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistry;
import saltsheep.arig.AddRecipesInGame;
import saltsheep.arig.data.RecipesGlobalData;
import saltsheep.arig.data.RecipesSaveData;

@EventBusSubscriber
public class RecipesRegistryHandler {

	private static ForgeRegistry<IRecipe> registry;
	private static Map<Integer,IngredientCustomFactory> factories = Maps.newHashMap();
	
	public static boolean registerFactory(IngredientCustomFactory factory) {
		if(factories.containsKey(factory.mode()))
			return false;
		for(IngredientCustomFactory alreadyRegistered:factories.values())
			if(factory.modeName().equals(alreadyRegistered.modeName()))
				return false;
		factories.put(factory.mode(), factory);
		return true;
	}
	
	@Nullable
	public static IngredientCustomFactory getFactory(int mode) {
		return factories.get(mode);
	}
	
	@Nullable
	public static IngredientCustomFactory getFactory(String modeName) {
		for(IngredientCustomFactory factory:factories.values())
			if(factory.modeName().equals(modeName))
				return factory;
		return null;
	}
	
	public static Collection<IngredientCustomFactory> getFactories(){
		return factories.values();
	}
	
	public static List<String> getFactoriesTypeWithinEmpty(){
		List<String> types = Lists.newLinkedList();
		for(IngredientCustomFactory factory:getFactories())
			if(factory.mode()!=IngredientCustomFactory.EMPTY)
				types.add(factory.modeName());
		return types;
	}
	
	@SubscribeEvent
	public static void loadRecipeInSave(WorldEvent.Load event) {
		if(!event.getWorld().isRemote) {
			registry.unfreeze();
			if(!event.getWorld().isRemote)
				RecipesSaveData.getData();
			registry.freeze();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
    public static void loadRecipeInGlobal(Register<IRecipe> event) {
		registry = (ForgeRegistry<IRecipe>) event.getRegistry();
		if(RecipesGlobalData.isRemoveAll()) {
			for(ResourceLocation RL : RegistryDetailsHandler.getRegisteredRecipesName()) {
				ItemStack fakeOut = registry.getValue(RL).getRecipeOutput();
				registry.remove(RL);
				registry.register(new RecipeFake(fakeOut).setRegistryName(RL));
			}
			return;
		}
		//*O(N*M),remove the recipes that has result removed.
		if(RecipesGlobalData.getRemoves()!=null&&!(RecipesGlobalData.getRemoves().isEmpty())) {
			List<IngredientCustom> removes = RecipesGlobalData.getRemoves();
			for(IngredientCustom remove : removes) {
				for(ResourceLocation RL : RegistryDetailsHandler.getRegisteredRecipesName()) {
					if(remove.apply(registry.getValue(RL).getRecipeOutput())) {
						ItemStack fakeOut = registry.getValue(RL).getRecipeOutput();
						registry.remove(RL);
						registry.register(new RecipeFake(fakeOut).setRegistryName(RL));
					}
				}
			}
		}
		registerRecipesAlreadySavedInGlobal();
	}
	
	@SubscribeEvent
	public static void removePlayerRecipes(EntityJoinWorldEvent event) {
		if(!event.getWorld().isRemote) {
			if(event.getEntity() instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
				RecipeBookHandler.checkAndRemove(player.getRecipeBook());
			}
		}
	}
	
	//*return is success
	public static boolean registerShapelessInGlobal(int ingredientMode,boolean isIgnoreDamage,String name,ItemStack result,ItemStack... use) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(use.length>9)
			return false;
		NonNullList<Ingredient> list = NonNullList.create();
		int emptyCount = 0;
		for(int i=0;i<use.length;i++) {
			if(use[i]==null||use[i].isEmpty())
				emptyCount++;
			else
				list.add(i-emptyCount, RecipesRegistryHandler.getFactory(ingredientMode).create(isIgnoreDamage, use[i]));
		}
		IRecipeSheep recipe =  RecipeHelper.create(IRecipeSheep.SHAPELESS, list, result);
		return registerInGlobal(name,recipe);
	}
		
	public static boolean registerShapedInGlobal(int ingredientMode,boolean isIgnoreDamage,String name,ItemStack result,ItemStack... use) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(use.length!=9)
			return false;
		NonNullList<Ingredient> list = NonNullList.create();
		for(int i=0;i<use.length;i++) {
			if(use[i]==null||use[i].isEmpty())
				list.add(i, RecipesRegistryHandler.getFactory(IngredientCustomFactory.EMPTY).create(isIgnoreDamage, use[i]));
			else
				list.add(i, RecipesRegistryHandler.getFactory(ingredientMode).create(isIgnoreDamage, use[i]));
		}
		IRecipeSheep recipe =  RecipeHelper.create(IRecipeSheep.SHAPED, list, result);
		return registerInGlobal(name,recipe);
	}
	
	public static boolean registerInGlobal(String name,IRecipeSheep recipe) {
		if(recipe == null)
			return false;
		if(RecipesGlobalData.register(name, recipe)) {
			recipe.setRegistryName(new ResourceLocation(AddRecipesInGame.MODID+":global_"+name));
			registry.unfreeze();
			registry.register(recipe);
			return true;
		}
		return false;
	}
	
	public static void registerRecipesAlreadySavedInGlobal() {
		for(String name : RecipesGlobalData.list()) {
			if(registry.containsValue(RecipesGlobalData.getRecipe(name)))
				continue;
			registry.register(RecipesGlobalData.getRecipe(name).setRegistryName(new ResourceLocation(AddRecipesInGame.MODID+":global_"+name)));
		}
	}
	
	public static boolean deleteRecipeInGlobalByName(String name) {
		boolean temp = RecipesGlobalData.delete(name);
		if(temp)
			deleteRecipeByName("global_"+name);
		return temp;
	}
	
	public static boolean deleteRecipeInSaveByName(String name) {
		boolean temp = RecipesSaveData.getData().delete(name);
		if(temp)
			deleteRecipeByName(name);
		return temp;
	}
	
	//*This is use in common,can also use for mc's recipes or other mods' recipes
	public static void deleteRecipeByName(String name) {
		if(!name.contains(":"))
			name = AddRecipesInGame.MODID+":"+name;
		for(ResourceLocation ReLo : RegistryDetailsHandler.getRegisteredRecipesName())
			if(ReLo.toString().equalsIgnoreCase(name)) {
				registry.unfreeze();
				for(EntityPlayerMP player:AddRecipesInGame.getMCServer().getPlayerList().getPlayers()) {
					player.getRecipeBook().remove(Lists.newArrayList(CraftingManager.getRecipe(ReLo)), player);
					player.getRecipeBook().remove(Lists.newArrayList(registry.getValue(ReLo)), player);
				}
				registry.remove(ReLo);
			}
	}
	
	//*return is success
	public static boolean registerShapelessInSave(int ingredientMode,boolean isIgnoreDamage,String name,ItemStack result,ItemStack... use) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(use.length>9)
			return false;
		NonNullList<Ingredient> list = NonNullList.create();
		int emptyCount = 0;
		for(int i=0;i<use.length;i++) {
			if(use[i]==null||use[i].isEmpty())
				emptyCount++;
			else
				list.add(i-emptyCount, RecipesRegistryHandler.getFactory(ingredientMode).create(isIgnoreDamage, use[i]));
		}
		IRecipeSheep recipe =  RecipeHelper.create(IRecipeSheep.SHAPELESS, list, result);
		return registerInSave(name,recipe);
	}
	
	public static boolean registerShapedInSave(int ingredientMode,boolean isIgnoreDamage,String name,ItemStack result,ItemStack... use) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(use.length!=9)
			return false;
		NonNullList<Ingredient> list = NonNullList.create();
		for(int i=0;i<use.length;i++) {
			if(use[i]==null||use[i].isEmpty())
				list.add(i, RecipesRegistryHandler.getFactory(ingredientMode).create(isIgnoreDamage, use[i]));
			else
				list.add(i, RecipesRegistryHandler.getFactory(ingredientMode).create(isIgnoreDamage, use[i]));
		}
		IRecipeSheep recipe =  RecipeHelper.create(IRecipeSheep.SHAPED, list, result);
		return registerInSave(name,recipe);
	}
	
	public static boolean registerInSave(String name,IRecipeSheep recipe) {
		if(recipe == null)
			return false;
		if(RecipesSaveData.getData().register(name, recipe)) {
			recipe.setRegistryName(new ResourceLocation(AddRecipesInGame.MODID+":"+name));
			registry.unfreeze();
			registry.register(recipe);
			return true;
		}
		return false;
	}
	
	public static boolean registerInSaveByData(String name,IRecipeSheep recipe) {
		if(recipe == null)
			return false;
		recipe.setRegistryName(new ResourceLocation(AddRecipesInGame.MODID+":"+name));
		try {
			Field isFrozen = registry.getClass().getDeclaredField("isFrozen");
			isFrozen.setAccessible(true);
			isFrozen.setBoolean(registry, false);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			AddRecipesInGame.printError(e);
		}
		registry.register(recipe);
		return true;
	}
	
}
