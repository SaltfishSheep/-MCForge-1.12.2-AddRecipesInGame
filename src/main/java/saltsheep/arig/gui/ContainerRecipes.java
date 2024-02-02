package saltsheep.arig.gui;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import saltsheep.arig.AddRecipesInGame;
import saltsheep.arig.recipe.RecipesRegistryHandler;

public class ContainerRecipes extends Container {

	public Slot[][] craftTable = new Slot[3][3];
	public Slot result;
	
	public String recipeName;
	public int recipeMode;
	public int ingredientMode;
	public boolean isConfirm = false;
	public boolean isView = false;
	public boolean isIgnoreDamage = false;
	protected EntityPlayer playerIn;
	protected IInventory craftTableItems;
	
	public ContainerRecipes(EntityPlayer playerIn) {
		super();
		this.craftTableItems = new InventoryBasic("Recipe Edit", false, 10);
		this.playerIn = playerIn;
		this.addSlotToContainer(result = new Slot(craftTableItems, 0, 124, 35));
        for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                this.addSlotToContainer(craftTable[i][j] = new Slot(craftTableItems, 1+j+i*3, 30 + j * 18, 17 + i * 18) {
                    @Override
                    public int getItemStackLimit(ItemStack stack){
                        return 1;
                    }
        		});
            }
        }
        for (int k = 0; k < 3; ++k){
            for (int i1 = 0; i1 < 9; ++i1){
                this.addSlotToContainer(new Slot(playerIn.inventory, i1 + k * 9 + 9, 8 + i1 * 18, 84 + k * 18));
            }
        }
        for (int l = 0; l < 9; ++l){
            this.addSlotToContainer(new Slot(playerIn.inventory, l, 8 + l * 18, 142));
        }
	}
	
	public void setItemStacks(List<ItemStack> list) {
		if(list.size()!=10)
			return;
		int point = 0;
		for (int i = 0; i < 3; ++i){
            for (int j = 0; j < 3; ++j){
                this.craftTable[i][j].putStack(list.get(point));
                point++;
            }
        }
		this.result.putStack(list.get(9));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	@Override
    public void onContainerClosed(EntityPlayer playerIn){
        super.onContainerClosed(playerIn);
        if(playerIn.isServerWorld()){
        	if(this.isView)
        		return;
        	if(this.isConfirm) {
        		try {
        			if(recipeMode==GuiHandler.RECIPE_SHAPED) {
        				if(RecipesRegistryHandler.registerShapedInSave(this.ingredientMode, this.isIgnoreDamage, this.recipeName, result.getStack(), 
        						craftTable[0][0].getStack(),craftTable[0][1].getStack(),craftTable[0][2].getStack(),
        						craftTable[1][0].getStack(),craftTable[1][1].getStack(),craftTable[1][2].getStack(),
        						craftTable[2][0].getStack(),craftTable[2][1].getStack(),craftTable[2][2].getStack()))
        					playerIn.sendMessage(new TextComponentString("Register successful."));
        				else
        					playerIn.sendMessage(new TextComponentString("Warning!Failed to register,maybe the recipe is already register!"));
        			}else if(recipeMode==GuiHandler.RECIPE_SHAPELESS) {
        				if(RecipesRegistryHandler.registerShapelessInSave(this.ingredientMode, this.isIgnoreDamage, this.recipeName, result.getStack(), 
        						craftTable[0][0].getStack(),craftTable[0][1].getStack(),craftTable[0][2].getStack(),
        						craftTable[1][0].getStack(),craftTable[1][1].getStack(),craftTable[1][2].getStack(),
        						craftTable[2][0].getStack(),craftTable[2][1].getStack(),craftTable[2][2].getStack()))
        					playerIn.sendMessage(new TextComponentString("Register successful."));
        				else
        					playerIn.sendMessage(new TextComponentString("Warning!Failed to register,maybe the recipe is already register!"));
        			}else if(recipeMode==GuiHandler.RECIPE_SHAPED_GLOBAL) {
        				if(RecipesRegistryHandler.registerShapedInGlobal(this.ingredientMode, this.isIgnoreDamage, this.recipeName, result.getStack(), 
        						craftTable[0][0].getStack(),craftTable[0][1].getStack(),craftTable[0][2].getStack(),
        						craftTable[1][0].getStack(),craftTable[1][1].getStack(),craftTable[1][2].getStack(),
        						craftTable[2][0].getStack(),craftTable[2][1].getStack(),craftTable[2][2].getStack()))
        					playerIn.sendMessage(new TextComponentString("Register successful."));
        				else
        					playerIn.sendMessage(new TextComponentString("Warning!Failed to register,maybe the recipe is already register!"));
        			}else if(recipeMode==GuiHandler.RECIPE_SHAPELESS_GLOBAL){
        				if(RecipesRegistryHandler.registerShapelessInGlobal(this.ingredientMode, this.isIgnoreDamage, this.recipeName, result.getStack(), 
        						craftTable[0][0].getStack(),craftTable[0][1].getStack(),craftTable[0][2].getStack(),
        						craftTable[1][0].getStack(),craftTable[1][1].getStack(),craftTable[1][2].getStack(),
        						craftTable[2][0].getStack(),craftTable[2][1].getStack(),craftTable[2][2].getStack()))
        					playerIn.sendMessage(new TextComponentString("Register successful."));
        				else
        					playerIn.sendMessage(new TextComponentString("Warning!Failed to register,maybe the recipe is already register!"));
        			}else
        				AddRecipesInGame.getLogger().warn("Warning!You are tried to register an unknown type of recipe.");
        		}catch(Exception error) {
        			AddRecipesInGame.printError(error);
        		}
        	}else {
        		ItemStack paperStack = this.result.getStack();
        		if(paperStack != ItemStack.EMPTY){
        			playerIn.dropItem(paperStack, false);
            	}
            	for(Slot[] i : this.craftTable) {
            		for(Slot craftTableSlot : i) {
            			ItemStack tableItem = craftTableSlot.getStack();
                		if(tableItem != ItemStack.EMPTY){
                    		playerIn.dropItem(tableItem, false);
                		}
            		}
            	}
        	}
        }
    }
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
	    return ItemStack.EMPTY;
	}

}
