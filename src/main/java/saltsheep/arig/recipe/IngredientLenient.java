package saltsheep.arig.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class IngredientLenient extends IngredientCustom {

	public IngredientLenient(IngredientCustomFactory factory,boolean isIgnoreDamege,ItemStack... items) {
		super(factory, isIgnoreDamege, items);
	}
	
	@Override
    public boolean apply(@Nullable ItemStack input) {
		if (input == null||input.isEmpty())
            return false;
		for(ItemStack item : this.getMatchingStacks()) {
			boolean itemEqual = (this.isIgnoreDamage()&&ItemStack.areItemsEqualIgnoreDurability(input, item))||ItemStack.areItemsEqual(input, item);
			if(itemEqual) {
				//*是否物品类型相同
				if(item.getTagCompound()==null)
					//*如果不需要任何NBT
					return true;
				else if(input.getTagCompound()==null)
					//*如果需要NBT，传入物品没有NBT
					return false;
				for(String eachFirst : item.getTagCompound().getKeySet()) {
					if(!input.getTagCompound().hasKey(eachFirst))
						//*如果缺少一级NBT
						return false;
					else if(!input.getTagCompound().getTag(eachFirst).equals(item.getTagCompound().getTag(eachFirst)))
						//*如果一级NBT中存在不相等
						return false;
				}
				return true;
			}
		}
		return false;
        
    }

    @Override
    public boolean isSimple(){
        return false;
    }
	
}
