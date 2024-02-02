package saltsheep.arig.recipe;

public class IngredientLenientFactory extends IngredientCustomFactory {

	@Override
	public String modeName() {
		return "lenient";
	}

	@Override
	protected int mode() {
		return IngredientCustomFactory.LENIENT;
	}

	@Override
	protected Class<? extends IngredientCustom> getProductType() {
		return IngredientLenient.class;
	}

}
