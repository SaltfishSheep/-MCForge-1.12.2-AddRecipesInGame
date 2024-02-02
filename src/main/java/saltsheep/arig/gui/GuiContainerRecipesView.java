package saltsheep.arig.gui;

import net.minecraft.inventory.Container;

public class GuiContainerRecipesView extends GuiContainerRecipes {
	
	public GuiContainerRecipesView(Container inventorySlotsIn, int mode, boolean isIgnoreDamage) {
		super(inventorySlotsIn, mode, isIgnoreDamage);
	}

	@Override
    public void initGui() {
		super.initGui();
		this.buttonList.clear();
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
	}

}
