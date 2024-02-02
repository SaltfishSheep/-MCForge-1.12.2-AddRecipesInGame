package saltsheep.arig;

import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import saltsheep.arig.command.CommandManager;
import saltsheep.arig.data.RecipesGlobalData;
import saltsheep.arig.gui.GuiHandler;
import saltsheep.arig.gui.NetworkForRecipe;
import saltsheep.arig.recipe.IngredientCustomFactory;

@Mod(modid = AddRecipesInGame.MODID, name = AddRecipesInGame.NAME, version = AddRecipesInGame.VERSION, acceptableRemoteVersions = "*")
public class AddRecipesInGame
{
    public static final String MODID = "addrecipesingame";
    public static final String NAME = "AddRecipesInGame";
    public static final String VERSION = "1.12";
    public static AddRecipesInGame instance;

    private static Logger logger;

    public AddRecipesInGame() {
    	instance = this;
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        NetworkForRecipe.register();
        IngredientCustomFactory.registerSheepFactories();
    }

    @EventHandler
    public void init(FMLInitializationEvent event){
    	NetworkRegistry.INSTANCE.registerGuiHandler(AddRecipesInGame.instance, new GuiHandler());
    	new RecipesGlobalData();
    }
    
    @EventHandler
    public static void onServerStarting(FMLServerStartingEvent event){
		CommandManager.register(event);
	}
    
    public static Logger getLogger() {
    	return logger;
    }
    
    public static MinecraftServer getMCServer() {
    	return FMLCommonHandler.instance().getMinecraftServerInstance();
    }
    
    public static void printError(Throwable error) {
    	String messages = "";
    	for(StackTraceElement stackTrace : error.getStackTrace()) {
    		messages = messages+stackTrace.toString()+"\n";
		}
    	AddRecipesInGame.getLogger().error("警告！在咸羊我的mod里出现了一些错误，信息如下：\n"+messages+"出现错误类型:"+error.getClass());
    }
    
    public static void info(String str) {
    	logger.info(str);
    }
    
    public static void info(Object obj) {
    	if(obj == null)
    		logger.info("null has such obj.");
    	else
    		logger.info(obj.toString());
    }
    
    
}
