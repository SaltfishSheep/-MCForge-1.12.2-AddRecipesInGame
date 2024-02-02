package saltsheep.arig.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import saltsheep.arig.AddRecipesInGame;
import saltsheep.arig.recipe.IRecipeSheep;
import saltsheep.arig.recipe.IngredientCustom;
import saltsheep.arig.recipe.IngredientCustomFactory;
import saltsheep.arig.recipe.RecipeHelper;

public class RecipesGlobalData {

	private static File data;
	private final static HashMap<String,IRecipeSheep> saveRecipes = Maps.newHashMap();
	private final static HashMap<String,IngredientCustom> removeResultOfRecipes = Maps.newHashMap();
	//*private final static List<ResourceLocation> removesPlayerHandler = Lists.newLinkedList();
	private static boolean canEdit = true;
	private static String editKey = "key";
	private static boolean isRemoveAll = false;
	
	//*!!All remove will take effect after restart game.
	public static boolean isRemoveAll() {
		return isRemoveAll;
	}

	public static void setRemoveAll(boolean isRemoveAll) {
		if(canEdit) {
			RecipesGlobalData.isRemoveAll = isRemoveAll;
			saveData();
		}
	}
	
	public static List<IngredientCustom> getRemoves(){
		return Lists.newArrayList(removeResultOfRecipes.values());
	}

	public static boolean addRemove(String id,IngredientCustom result) {
		if(canEdit) {
			if(removeResultOfRecipes.containsKey(id))
				return false;
			removeResultOfRecipes.put(id, result);
			saveData();
			return true;
		}
		return false;
	}
	
	public static boolean delRemove(String id) {
		if(canEdit) {
			if(removeResultOfRecipes.containsKey(id)) {
				removeResultOfRecipes.remove(id);
				saveData();
				return true;
			}else
				return false;
		}
		return false;
	}
	
	@Nullable
	public static IngredientCustom getRemove(String id) {
		return removeResultOfRecipes.get(id);
	}
	
	public static List<String> listRemove(){
		return Lists.newArrayList(removeResultOfRecipes.keySet());
	}
	
	/*public static List<ResourceLocation> getAlreadyRemoves(){
		return removesPlayerHandler;
	}
	
	public static void addAlreadyRemove(ResourceLocation rl) {
		removesPlayerHandler.add(rl);
	}*/
	
	public static boolean register(String name,IRecipeSheep recipe) {
		if(canEdit) {
			if(saveRecipes.containsKey(name))
				return false;
			saveRecipes.put(name, recipe);
			saveData();
			return true;
		}
		return false;
	}
	
	public static boolean delete(String name) {
		if(canEdit) {
			if(saveRecipes.containsKey(name)) {
				saveRecipes.remove(name);
				saveData();
				return true;
			}else
				return false;
		}
		return false;
	}
	
	public static List<String> list(){
		return Lists.newArrayList(saveRecipes.keySet());
	}
	
	public static Set<Entry<String,IRecipeSheep>> getRegisteredRecipes(){
		return saveRecipes.entrySet();
	}
	
	@Nullable
	public static IRecipeSheep getRecipe(String name) {
		return saveRecipes.get(name);
	}
	
	public static boolean isRecipeIgnoreDamage(String name) {
		IRecipeSheep recipe = RecipesGlobalData.getRecipe(name);
		int courtIG = 0;
		int courtUNIG = 0;
		if(recipe!=null)
			for(Ingredient each : recipe.getIngredients()) {
				if(each instanceof IngredientCustom) {
					if(((IngredientCustom)each).isIgnoreDamage())
						courtIG++;
					else
						courtUNIG++;
				}else {
					courtUNIG++;
				}
			}
		return courtIG>=courtUNIG;
	}
	
	public static List<ItemStack> getRecipeItemStacks(String recipeName){
		List<ItemStack> recipeStacks = Lists.newArrayList();
		NonNullList<Ingredient> recipeIngredients = saveRecipes.get(recipeName).getIngredients();
		for(int i = 0;i<recipeIngredients.size();i++)
			recipeStacks.add(recipeIngredients.get(i).getMatchingStacks()[0]);
		for(int i = recipeStacks.size();i<9;i++)
			recipeStacks.add(i, ItemStack.EMPTY);
		recipeStacks.add(9, saveRecipes.get(recipeName).getRecipeOutput());
		return recipeStacks;
	}
	
	public static boolean getCanEdit() {
		return RecipesGlobalData.canEdit;
	}
	
	public static void setCanEdit(boolean canEdit) {
		RecipesGlobalData.canEdit = canEdit;
		saveData();
	}
	
	public static String getEditKey() {
		return RecipesGlobalData.editKey;
	}
	
	public static void setEditKey(String editKey) {
		RecipesGlobalData.editKey = editKey;
		saveData();
	}
	
	public static void saveData() {
		write();
	}
	
	private static void read() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		NBTTagCompound nbttagcompound = null;
        try {
        	FileInputStream fileinputstream = new FileInputStream(data);
			nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
			fileinputstream.close();
        } catch (IOException e) {
			AddRecipesInGame.printError(e);
		}
		if(nbttagcompound != null&&!nbttagcompound.isEmpty()) {
			NBTTagCompound recipes = nbttagcompound.getCompoundTag("recipesSavedBySheep");
			NBTTagCompound removeRecipes = nbttagcompound.getCompoundTag("removeResultOfRecipes");
			canEdit = nbttagcompound.getBoolean("canEdit");
			editKey = nbttagcompound.getString("editKey");
			isRemoveAll = (nbttagcompound.getBoolean("isRemoveAll"));
			if(!recipes.isEmpty()) {
				for(String name : recipes.getKeySet()) {
					IRecipeSheep recipe = RecipeHelper.fromNBT(recipes.getCompoundTag(name));
					saveRecipes.put(name, recipe);
				}
			}
			if(!removeRecipes.isEmpty()) {
				for(String removeName : removeRecipes.getKeySet()) {
					IngredientCustom removeResult = IngredientCustomFactory.fromNBTCommon(removeRecipes.getCompoundTag(removeName));
					removeResultOfRecipes.put(removeName, removeResult);
				}
			}
		}
	}
	
	private static void write() {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setBoolean("canEdit", canEdit);
		nbttagcompound.setString("editKey", editKey);
		nbttagcompound.setBoolean("isRemoveAll", isRemoveAll);
		NBTTagCompound recipes = new NBTTagCompound();
		NBTTagCompound removeRecipes = new NBTTagCompound();
		for(String name : saveRecipes.keySet())
			recipes.setTag(name, saveRecipes.get(name).toNBT());
		for(String removeName : removeResultOfRecipes.keySet())
			removeRecipes.setTag(removeName, removeResultOfRecipes.get(removeName).toNBT());
		nbttagcompound.setTag("recipesSavedBySheep", recipes);
		nbttagcompound.setTag("removeResultOfRecipes", removeRecipes);
		try {
        	FileOutputStream fileoutputstream = new FileOutputStream(data);
			CompressedStreamTools.writeCompressed(nbttagcompound, fileoutputstream);
			fileoutputstream.close();
			AddRecipesInGame.info("Save global talent successful!");
        } catch (IOException e) {
			AddRecipesInGame.printError(e);
		}
	}
	
	//*Init talent when first use.
	static {
		try {
			data = new File("."+File.separator+"AddRecipesInGame","recipeData.dat");
			if(!data.getParentFile().exists())
				data.getParentFile().mkdir();
			if(!data.exists()) 
				data.createNewFile();
			read();
			AddRecipesInGame.info("Init global talent successful!");
		} catch (Exception e) {
			AddRecipesInGame.printError(e);
		}
	}
	
}
