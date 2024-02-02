package saltsheep.arig.recipe;

public class IngredientEmptyFactory extends IngredientCustomFactory {

	@Override
	public String modeName() {
		return "empty";
	}

	@Override
	protected int mode() {
		return IngredientCustomFactory.EMPTY;
	}

	@Override
	protected Class<? extends IngredientCustom> getProductType() {
		return IngredientEmpty.class;
	}

}
