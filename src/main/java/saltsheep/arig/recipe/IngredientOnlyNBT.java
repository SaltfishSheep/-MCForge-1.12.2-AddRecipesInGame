package saltsheep.arig.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class IngredientOnlyNBT extends IngredientCustom {

	public IngredientOnlyNBT(IngredientCustomFactory factory,boolean isIgnoreDamege,ItemStack... items) {
		super(factory, isIgnoreDamege, items);
	}
	
	@Override
    public boolean apply(@Nullable ItemStack input) {
		if (input == null||input.isEmpty())
            return false;
		for(ItemStack item : this.getMatchingStacks()) {
			if(ItemStack.areItemStackTagsEqual(item, input))
				return true;
		}
		return false;
        
    }

    @Override
    public boolean isSimple(){
        return false;
    }
	
}
