package saltsheep.arig.gui;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import saltsheep.arig.AddRecipesInGame;

public class GuiContainerRecipes extends GuiContainer {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft:textures/gui/container/crafting_table.png");
	
	public final int mode;
	public final boolean isIgnoreDamage;

	public GuiContainerRecipes(Container inventorySlotsIn,int mode,boolean isIgnoreDamage) {
		super(inventorySlotsIn);
		this.isIgnoreDamage = isIgnoreDamage;
		this.mode=mode;
		this.width = 176;
		this.height = 166;
	}
	
	@Override
    public void initGui() {
        super.initGui();
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(0, offsetX + 107, offsetY + 5, 64, 20, I18n.format("saltsheep.gui.recipe.confirm")));
    }

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		this.drawDefaultBackground();
		GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;
		this.renderHoveredToolTip(mouseX-offsetX, mouseY-offsetY);
		String mode = I18n.format("saltsheep.gui.recipe.mode."+this.mode+"."+String.valueOf(this.isIgnoreDamage));
		this.mc.fontRenderer.drawString(mode, 6, 6, 0x000000);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
			GuiButton choose = this.selectedButton;
			if(choose!=null) {
				NetworkForRecipe.confirmByClient(Minecraft.getMinecraft().player);
			}
		} catch (IOException e) {
			AddRecipesInGame.printError(e);
		}
	}

}
