package saltsheep.arig.recipe;

public class IngredientOnlyNBTFactory extends IngredientCustomFactory {

	@Override
	public String modeName() {
		return "onlyNBT";
	}

	@Override
	protected int mode() {
		return IngredientCustomFactory.ONLY_NBT;
	}

	@Override
	protected Class<? extends IngredientCustom> getProductType() {
		return IngredientOnlyNBT.class;
	}

}
