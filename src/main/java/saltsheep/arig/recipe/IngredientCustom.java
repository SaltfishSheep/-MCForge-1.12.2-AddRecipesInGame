package saltsheep.arig.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;

public abstract class IngredientCustom extends Ingredient{
	
	private final boolean isIgnoreDamage;
	public final IngredientCustomFactory factory;
	
	public IngredientCustom(IngredientCustomFactory factory,boolean isIgnoreDamage,ItemStack... items) {
		super(items);
		this.factory=factory;
		this.isIgnoreDamage=isIgnoreDamage;
	}
	
	public boolean isIgnoreDamage() {
		return this.isIgnoreDamage;
	}
	
	public NBTTagCompound toNBT() {
		return this.factory.toNBT(this);
	}
	
	/*Unused
	@Nullable
	public static IngredientCustom create(int mode,boolean isIgnoreDamage,ItemStack... items) {
		for(ItemStack each : items)
			if(each==null||each.isEmpty())
				mode = EMPTY;
		switch(mode) {
		case EMPTY:
			return new IngredientEmpty(isIgnoreDamage);
		case RIGID:
			return new IngredientRigid(isIgnoreDamage, items);
		case LENIENT:
			return new IngredientLenient(isIgnoreDamage, items);
		case ONLY_NBT:
			return new IngredientOnlyNBT(isIgnoreDamage, items);
		case ONLY_ITEM:
			return new IngredientOnlyItem(isIgnoreDamage, items);
		case ONLY_NAME:
			return new IngredientOnlyName(isIgnoreDamage, items);
		}
		return null;
	}
	
	public abstract int mode();
	
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("mode", this.mode());
		nbt.setBoolean("isIgnoreDamage", this.isIgnoreDamage());
		NBTTagList items = new NBTTagList();
		for(ItemStack eachItem : this.getMatchingStacks())
			items.appendTag(eachItem.serializeNBT());
		nbt.setTag("items", items);
		return nbt;
	}
	
	public static IngredientCustom deserializeNBT(NBTTagCompound nbt) {
		NBTTagList itemsNBT = nbt.getTagList("items", 10);
		ItemStack[] items = new ItemStack[itemsNBT.tagCount()];
		for(int i = 0;i<items.length;i++)
			items[i] = new ItemStack(itemsNBT.getCompoundTagAt(i));
		IngredientCustom result = null;
		boolean isIgnoreDamage = nbt.getBoolean("isIgnoreDamage");
		switch(nbt.getInteger("mode")) {
			case EMPTY:
				result = new IngredientEmpty(isIgnoreDamage);
				break;
			case RIGID:
				result = new IngredientRigid(isIgnoreDamage, items);
				break;
			case LENIENT:
				result = new IngredientLenient(isIgnoreDamage, items);
				break;
			case ONLY_NBT:
				result = new IngredientOnlyNBT(isIgnoreDamage, items);
				break;
			case ONLY_ITEM:
				result = new IngredientOnlyItem(isIgnoreDamage, items);
				break;
			case ONLY_NAME:
				result = new IngredientOnlyName(isIgnoreDamage, items);
				break;
		}
		return result;
	}
	*/

}
