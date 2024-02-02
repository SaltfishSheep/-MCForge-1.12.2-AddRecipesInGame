package saltsheep.arig.recipe;

public class IngredientIncludeLoreFactory extends IngredientCustomFactory {

	@Override
	public String modeName() {
		return "includeLore";
	}

	@Override
	protected int mode() {
		return IngredientCustomFactory.INCLUDE_LORE;
	}

	@Override
	protected Class<? extends IngredientCustom> getProductType() {
		return IngredientIncludeLore.class;
	}

}
