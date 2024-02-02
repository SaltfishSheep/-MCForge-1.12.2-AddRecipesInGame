package saltsheep.arig.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class IngredientOnlyItem extends IngredientCustom {

	public IngredientOnlyItem(IngredientCustomFactory factory,boolean isIgnoreDamege,ItemStack... items) {
		super(factory, isIgnoreDamege, items);
	}
	
	@Override
    public boolean apply(@Nullable ItemStack input) {
		if (input == null||input.isEmpty())
            return false;
		for(ItemStack item : this.getMatchingStacks()) {
			boolean itemEqual = (this.isIgnoreDamage()&&ItemStack.areItemsEqualIgnoreDurability(input, item))||ItemStack.areItemsEqual(input, item);
			if(itemEqual)
				return true;
		}
		return false;
        
    }

    @Override
    public boolean isSimple(){
        return false;
    }
    
	
}
