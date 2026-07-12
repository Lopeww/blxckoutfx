package com.lopew.blxckoutfx;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = BlxckoutFX.MODID,
        name = BlxckoutFX.NAME,
        version = BlxckoutFX.VERSION,
        guiFactory = "com.lopew.blxckoutfx.client.config.BlxckoutFXGuiFactory",
        useMetadata = true,
        clientSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class BlxckoutFX
{
    public static final String MODID = "blxckoutfx";
    public static final String NAME = "BlxckoutFX";
    public static final String VERSION = "1.0.1";

    @SidedProxy(
            clientSide = "com.lopew.blxckoutfx.client.ClientProxy",
            serverSide = "com.lopew.blxckoutfx.CommonProxy"
    )
    public static CommonProxy proxy;

    private static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("{} {} initializing", NAME, VERSION);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
        logger.info("{} initialized", NAME);
    }
}
