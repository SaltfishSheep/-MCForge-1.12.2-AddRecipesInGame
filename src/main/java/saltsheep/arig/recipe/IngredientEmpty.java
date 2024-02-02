package saltsheep.arig.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class IngredientEmpty extends IngredientCustom {

	public IngredientEmpty(IngredientCustomFactory factory,boolean isIgnoreDamege,ItemStack... items) {
		super(factory, isIgnoreDamege);
	}
	
	/*Unused
	 * @Override
	public ItemStack[] getMatchingStacks(){
        return new ItemStack[] {ItemStack.EMPTY};
    }*/
	
	@Override
    public boolean apply(@Nullable ItemStack input) {
		if(input == null||input.isEmpty())
			return true;
		return false;
    }

    @Override
    public boolean isSimple(){
        return true;
    }
	
}
