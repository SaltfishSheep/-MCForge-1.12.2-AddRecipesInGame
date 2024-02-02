package saltsheep.arig.data;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import saltsheep.arig.AddRecipesInGame;
import saltsheep.arig.recipe.IRecipeSheep;
import saltsheep.arig.recipe.IngredientCustom;
import saltsheep.arig.recipe.RecipeHelper;
import saltsheep.arig.recipe.RecipesRegistryHandler;

public class RecipesSaveData extends WorldSavedData {

	private static final String NAME = "RecipesSaveData";
	private final HashMap<String,IRecipeSheep> saveRecipes = Maps.newHashMap();
	
	private boolean canEdit = true;
	private String editKey = "key";
	
	public RecipesSaveData() {
		this(NAME);
	}
	
	public RecipesSaveData(String name) {
		super(name);
	}
	
	public static RecipesSaveData getData() {
		WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
		MapStorage storage = world.getMapStorage();
		if(storage.getOrLoadData(RecipesSaveData.class, NAME) == null) {
			storage.setData(NAME, new RecipesSaveData());
		}
		return (RecipesSaveData) storage.getOrLoadData(RecipesSaveData.class, NAME);
	}
	
	public boolean getCanEdit() {
		return this.canEdit;
	}
	
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
		this.markDirty();
	}
	
	public String getEditKey() {
		return this.editKey;
	}
	
	public void setEditKey(String editKey) {
		this.editKey = editKey;
		this.markDirty();
	}
	
	public boolean register(String name,IRecipeSheep recipe) {
		if(this.canEdit&&RecipesGlobalData.getCanEdit()) {
			if(saveRecipes.containsKey(name))
				return false;
			saveRecipes.put(name, recipe);
			this.markDirty();
			return true;
		}
		return false;
	}
	
	public boolean delete(String name) {
		if(this.canEdit&&RecipesGlobalData.getCanEdit()) {
			if(saveRecipes.containsKey(name)) {
				saveRecipes.remove(name);
				this.markDirty();
				return true;
			}else
				return false;
		}
		return false;
	}
	
	public List<String> list(){
		return Lists.newArrayList(saveRecipes.keySet());
	}
	
	public IRecipeSheep getRecipe(String name) {
		return saveRecipes.get(name);
	}
	
	public boolean isRecipeIgnoreDamage(String name) {
		IRecipeSheep recipe = this.getRecipe(name);
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
	
	public List<ItemStack> getRecipeItemStacks(String recipeName){
		List<ItemStack> recipeStacks = Lists.newArrayList();
		NonNullList<Ingredient> recipeIngredients = saveRecipes.get(recipeName).getIngredients();
		for(int i = 0;i<recipeIngredients.size();i++) {
			if(recipeIngredients.get(i).getMatchingStacks().length==0||recipeIngredients.get(i).getMatchingStacks()[0].isEmpty()) {
				recipeStacks.add(i, ItemStack.EMPTY);
				continue;
			}
			recipeStacks.add(recipeIngredients.get(i).getMatchingStacks()[0]);
		}
		recipeStacks.add(9, saveRecipes.get(recipeName).getRecipeOutput());
		return recipeStacks;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagCompound recipes = nbt.getCompoundTag("recipesSavedBySheep");
		this.canEdit = recipes.getBoolean("canEdit");
		this.editKey = recipes.getString("editKey");
		if(!recipes.isEmpty()) {
			for(String name : recipes.getKeySet()) {
				if(name.equals("canEdit")||name.equals("editKey"))
					continue;
				IRecipeSheep recipe;
				try {
					recipe = RecipeHelper.fromNBT(recipes.getCompoundTag(name));
					this.saveRecipes.put(name, recipe);
					RecipesRegistryHandler.registerInSaveByData(name,recipe);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					AddRecipesInGame.printError(e);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound recipes = new NBTTagCompound();
		recipes.setBoolean("canEdit", this.canEdit);
		recipes.setString("editKey", this.editKey);
		for(String name : this.saveRecipes.keySet()) {
			recipes.setTag(name, this.saveRecipes.get(name).toNBT());
		}
		compound.setTag("recipesSavedBySheep", recipes);
		return compound;
	}

}
