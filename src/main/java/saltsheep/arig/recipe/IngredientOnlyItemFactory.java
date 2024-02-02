package saltsheep.arig.recipe;

public class IngredientOnlyItemFactory extends IngredientCustomFactory {

	@Override
	public String modeName() {
		return "onlyItem";
	}

	@Override
	protected int mode() {
		return IngredientCustomFactory.ONLY_ITEM;
	}

	@Override
	protected Class<? extends IngredientCustom> getProductType() {
		return IngredientOnlyItem.class;
	}

}
