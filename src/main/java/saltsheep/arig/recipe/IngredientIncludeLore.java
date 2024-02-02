package saltsheep.arig.recipe;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class IngredientIncludeLore extends IngredientCustom {

	public IngredientIncludeLore(IngredientCustomFactory factory,boolean isIgnoreDamege,ItemStack... items) {
		super(factory, isIgnoreDamege, items);
	}
	
	@Override
    public boolean apply(@Nullable ItemStack input) {
		if (input == null||input.isEmpty())
            return false;
		if(input.hasTagCompound()&&input.getTagCompound().hasKey("display")&&input.getTagCompound().getCompoundTag("display").hasKey("Lore")) {
			NBTTagList loreList = input.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
			String[] lores = new String[loreList.tagCount()];
			for(int i=0;i<loreList.tagCount();i++)
				lores[i] = loreList.getStringTagAt(i);
			for(ItemStack item : this.getMatchingStacks()) {
				NBTTagList loreListIn = item.getTagCompound().getCompoundTag("display").getTagList("Lore", 8);
				boolean[] hasLores = new boolean[loreListIn.tagCount()];
				//*检查是否input是否拥有所有lore
				EachLore:for(int i=0;i<loreListIn.tagCount();i++) {
					for(int k=0;i<lores.length;k++) {
						//*检查input中是否有需求的lore
						if(lores[k].equals(loreListIn.getStringTagAt(i))) {
							hasLores[i] = true;
							continue EachLore;
						}
					}
				}
				boolean hasAll = true;
				for(boolean eachHas:hasLores)
					hasAll = hasAll&&eachHas;
				if(hasAll)
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
