package saltsheep.arig.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;

public class RecipeShapeless extends ShapelessRecipes implements IRecipeSheep{

	public RecipeShapeless(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
		super(group, output, ingredients);
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		NonNullList<Ingredient> ingredientsList = this.recipeItems; 
		NBTTagList ingredients = new NBTTagList();
		for(int i = 0;i<ingredientsList.size();i++) {
			ingredients.appendTag(((IngredientCustom) ingredientsList.get(i)).toNBT());
		}
		nbt.setTag("ingredients", ingredients);
		nbt.setInteger("mode", this.mode());
		nbt.setTag("result", this.getRecipeOutput().serializeNBT());
		return nbt;
	}

	@Override
	public int mode() {
		// TODO 自动生成的方法存根
		return IRecipeSheep.SHAPELESS;
	}

}
