package saltsheep.arig.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;

public interface IRecipeSheep extends IRecipe{

	public static int SHAPED = 0;
	public static int SHAPELESS = 1;
	
	public NBTTagCompound toNBT();
	
	public int mode();
	
}
