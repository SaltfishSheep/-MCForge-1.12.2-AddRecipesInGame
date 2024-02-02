package saltsheep.arig.recipe;

public class IngredientOnlyNameFactory extends IngredientCustomFactory {

	@Override
	public String modeName() {
		return "onlyName";
	}

	@Override
	protected int mode() {
		return IngredientCustomFactory.ONLY_NAME;
	}

	@Override
	protected Class<? extends IngredientCustom> getProductType() {
		return IngredientOnlyName.class;
	}

}
