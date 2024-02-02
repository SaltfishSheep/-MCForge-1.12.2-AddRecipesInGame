package saltsheep.arig.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class RecipeFake extends RecipeShapeless {

	public RecipeFake(ItemStack item) {
		super("saltsheep", item,NonNullList.create());
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn){
		return false;
    }
	
	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

}
