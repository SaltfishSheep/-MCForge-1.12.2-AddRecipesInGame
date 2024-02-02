package saltsheep.arig.gui;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NetworkForRecipe {
	
	private static final String NAME = "RECIPECONFIRMDATA";
	private static final FMLEventChannel CHANNEL = NetworkRegistry.INSTANCE.newEventDrivenChannel(NAME);

	public static void register() {
		CHANNEL.register(NetworkForRecipe.class);
	}
	
	@SideOnly(Side.CLIENT)
	public static void confirmByClient(EntityPlayerSP player) {
		PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
		char[] nameToArray = player.getName().toCharArray();
		buf.writeInt(nameToArray.length);
		for(char each : nameToArray)
			buf.writeChar(each);
		//*写入玩家名称
		CHANNEL.sendToServer(new FMLProxyPacket(buf,NAME));
	}
	
	@SubscribeEvent
	public static void confirmByServer(FMLNetworkEvent.ServerCustomPacketEvent event) {
		if(event.getPacket().channel().equals(NAME)) {
			MinecraftServer MCServer = FMLCommonHandler.instance().getMinecraftServerInstance();
			ByteBuf buf = event.getPacket().payload();
			char[] nameAsArray = new char[buf.readInt()];
			for(int each = 0;each < nameAsArray.length;each++)
				nameAsArray[each] = buf.readChar();
			String playerName = new String(nameAsArray);
			EntityPlayerMP player = MCServer.getPlayerList().getPlayerByUsername(playerName);
			if(player != null&&player instanceof EntityPlayerMP) {
				MCServer.addScheduledTask(()->{
					((ContainerRecipes) player.openContainer).isConfirm = true;
					player.closeScreen();
				});
			}
		}
	}
	
}
