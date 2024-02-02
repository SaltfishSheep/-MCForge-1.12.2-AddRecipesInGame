package saltsheep.arig.recipe;

public class IngredientRigidFactory extends IngredientCustomFactory {

	@Override
	public String modeName() {
		return "rigid";
	}

	@Override
	protected int mode() {
		return IngredientCustomFactory.RIGID;
	}

	@Override
	protected Class<? extends IngredientCustom> getProductType() {
		return IngredientRigid.class;
	}

}
