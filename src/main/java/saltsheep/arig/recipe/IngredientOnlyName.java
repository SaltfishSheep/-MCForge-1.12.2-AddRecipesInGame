package saltsheep.arig.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class IngredientOnlyName extends IngredientCustom {

	public IngredientOnlyName(IngredientCustomFactory factory,boolean isIgnoreDamege,ItemStack... items) {
		super(factory, isIgnoreDamege, items);
	}
	
	@Override
    public boolean apply(@Nullable ItemStack input) {
		if (input == null||input.isEmpty())
            return false;
		for(ItemStack item : this.getMatchingStacks()) {
			if(item.getDisplayName().equals(input.getDisplayName()))
				//*如果物品间名字相同
				return true;
		}
		return false;
        
    }

    @Override
    public boolean isSimple(){
        return false;
    }
	
}
