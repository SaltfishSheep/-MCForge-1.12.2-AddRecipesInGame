package saltsheep.arig.recipe;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import saltsheep.arig.AddRecipesInGame;

public abstract class IngredientCustomFactory {
	
	public static final int EMPTY = -1;
	public static final int RIGID = 0;
	public static final int LENIENT = 1;
	public static final int ONLY_NBT = 2;
	public static final int ONLY_ITEM = 3;
	public static final int ONLY_NAME = 4;
	public static final int INCLUDE_LORE = 5;
	
	public abstract String modeName();
	
	protected abstract int mode();
	
	protected abstract Class<? extends IngredientCustom> getProductType();
	
	public boolean isInstance(int mode) {
		return this.mode()==mode;
	}
	
	public boolean isInstance(IngredientCustom ingredient) {
		return this.getProductType()==ingredient.getClass();
	}

	public IngredientCustom create(boolean isIgnoreDamage,ItemStack... needs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		IngredientCustom ingredient = this.getProductType().getConstructor(IngredientCustomFactory.class,boolean.class,ItemStack[].class).newInstance(this,isIgnoreDamage,needs);
		return ingredient;
	}
	
	public NBTTagCompound toNBT(IngredientCustom ingredient) {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("mode", this.mode());
		nbt.setBoolean("isIgnoreDamage", ingredient.isIgnoreDamage());
		NBTTagList items = new NBTTagList();
		for(ItemStack eachItem : ingredient.getMatchingStacks())
			items.appendTag(eachItem.serializeNBT());
		nbt.setTag("items", items);
		return nbt;
	}
	
	public IngredientCustom fromNBT(NBTTagCompound nbt) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		NBTTagList itemsNBT = nbt.getTagList("items", 10);
		ItemStack[] needs = new ItemStack[itemsNBT.tagCount()];
		for(int i = 0;i<needs.length;i++)
			needs[i] = new ItemStack(itemsNBT.getCompoundTagAt(i));
		boolean isIgnoreDamage = nbt.getBoolean("isIgnoreDamage");
		IngredientCustom ingredient = this.create(isIgnoreDamage, needs);
		return ingredient;
	}
	
	public static IngredientCustom fromNBTCommon(NBTTagCompound nbt) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return RecipesRegistryHandler.getFactory(nbt.getInteger("mode")).fromNBT(nbt);
	}
	
	public static void registerSheepFactories() {
		for(IngredientCustomFactory factory:Arrays.asList(new IngredientEmptyFactory(),new IngredientIncludeLoreFactory(),new IngredientLenientFactory(),new IngredientOnlyItemFactory(),new IngredientOnlyNameFactory(),new IngredientOnlyNBTFactory(),new IngredientRigidFactory())) {
			if(RecipesRegistryHandler.registerFactory(factory))
				AddRecipesInGame.info("Register factory-"+factory.modeName()+" successful");
			else
				AddRecipesInGame.info("Failed to register factory-"+factory.modeName());
		}
	}
	
}
