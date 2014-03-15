package ccm.placeableTools.network;

import ccm.nucleumOmnium.helpers.MiscHelper;
import ccm.nucleumOmnium.helpers.NetworkHelper;
import ccm.placeableTools.block.ToolTE;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import static ccm.placeableTools.util.PTConstants.CHANNEL_SIGN_UPDATE;

public class PacketHandler implements IPacketHandler
{
    @Override
    public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player)
    {
        try
        {
            if (packet.channel.equals(CHANNEL_SIGN_UPDATE))
            {
                ByteArrayInputStream streambyte = new ByteArrayInputStream(packet.data);
                DataInputStream stream = new DataInputStream(streambyte);

                ((ToolTE) ((EntityPlayer) player).worldObj.getBlockTileEntity(stream.readInt(), stream.readInt(), stream.readInt())).setText(stream.readBoolean(), new String[] {stream.readUTF(), stream.readUTF(), stream.readUTF(), stream.readUTF()});

                stream.close();
                streambyte.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
