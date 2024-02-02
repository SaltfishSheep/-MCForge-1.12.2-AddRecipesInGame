package saltsheep.arig.recipe;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class RecipeHelper {

	@Nullable
	public static IRecipeSheep fromNBT(NBTTagCompound nbt) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(nbt.getInteger("mode")==IRecipeSheep.SHAPED) {
			NonNullList<Ingredient> list = NonNullList.create();
			NBTTagList nbtList = nbt.getTagList("ingredients", 10);
			for(int i=0;i<nbtList.tagCount();i++)
				list.add(i, IngredientCustomFactory.fromNBTCommon((NBTTagCompound) nbtList.get(i)));
			ItemStack result = new ItemStack(nbt.getCompoundTag("result"));
			return new RecipeShaped("saltsheep", result, list);
		}else if(nbt.getInteger("mode")==IRecipeSheep.SHAPELESS) {
			NonNullList<Ingredient> list = NonNullList.create();
			NBTTagList nbtList = nbt.getTagList("ingredients", 10);
			int emptyCount = 0;
			for(int i=0;i<nbtList.tagCount();i++) {
				IngredientCustom ingredient = IngredientCustomFactory.fromNBTCommon((NBTTagCompound) nbtList.get(i));
				if(ingredient instanceof IngredientEmpty)
					emptyCount++;
				else
					list.add(i-emptyCount, ingredient);
			}
			ItemStack result = new ItemStack(nbt.getCompoundTag("result"));
			return new RecipeShapeless("saltsheep", result, list);
		}
		return null;
	}
	
	@Nullable
	public static IRecipeSheep create(int recipeMode,NonNullList<Ingredient> list,ItemStack result) {
		if(recipeMode==IRecipeSheep.SHAPED) {
			return new RecipeShaped("saltsheep", result, list);
		}else if(recipeMode==IRecipeSheep.SHAPELESS) {
			return new RecipeShapeless("saltsheep", result, list);
		}
		return null;
	}
	
}
