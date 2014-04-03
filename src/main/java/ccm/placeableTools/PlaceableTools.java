package ccm.placeableTools;

import ccm.nucleumOmnium.NucleumOmnium;
import ccm.placeableTools.network.PacketHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

import java.io.File;
import java.util.logging.Logger;

import static ccm.placeableTools.util.PTConstants.CHANNEL_SIGN_UPDATE;
import static ccm.placeableTools.util.PTConstants.MODID;

@Mod(modid = MODID, dependencies = "required-after:NucleumOmnium")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = {CHANNEL_SIGN_UPDATE}, packetHandler = PacketHandler.class)
public class PlaceableTools
{
    @Mod.Instance(MODID)
    public static PlaceableTools instance;

    @SidedProxy(serverSide = "ccm.placeableTools.CommonProxy", clientSide = "ccm.placeableTools.client.ClientProxy")
    public static CommonProxy proxy;

    private PTConfig config;
    private Logger   logger;

    public static PTConfig getConfig()
    {
        return instance.config;
    }

    public static Logger getLogger()
    {
        return instance.logger;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        config = new PTConfig(new File(NucleumOmnium.getCCMFolder(), MODID + ".cfg"));

        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
