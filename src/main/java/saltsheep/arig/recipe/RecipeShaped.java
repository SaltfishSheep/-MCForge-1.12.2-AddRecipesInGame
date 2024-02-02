package saltsheep.arig.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeShaped extends ShapedOreRecipe implements IRecipeSheep{

	//*"need"长度必须为9，否则列表越界报错！
	public RecipeShaped(String group, ItemStack result, NonNullList<Ingredient> need) {
		super(new ResourceLocation(group), result,
				"abc",
				"def",
				"ghi",
				'a',need.get(0),
				'b',need.get(1),
				'c',need.get(2),
				'd',need.get(3),
				'e',need.get(4),
				'f',need.get(5),
				'g',need.get(6),
				'h',need.get(7),
				'i',need.get(8));
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		NonNullList<Ingredient> ingredientsList = this.input; 
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
		return IRecipeSheep.SHAPED;
	}

}
