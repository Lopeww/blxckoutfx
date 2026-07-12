package com.lopew.blxckoutfx;

import com.lopew.blxckoutfx.client.BlxckoutFXClientConfig;
import com.lopew.blxckoutfx.client.BlxckoutFXGUIHandler;
import com.lopew.blxckoutfx.client.BlxckoutFXKeybinds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
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
    public static final String VERSION = "1.0.0";

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
        logger.info("{} initialized", NAME);
        
        try {
            MinecraftForge.EVENT_BUS.register(new BlxckoutFXClientConfig.ConfigEventHandler());
            
            if (event.getSide().isClient()) {
                MinecraftForge.EVENT_BUS.register(new BlxckoutFXGUIHandler());
                MinecraftForge.EVENT_BUS.register(new BlxckoutFXKeybinds());
                BlxckoutFXKeybinds.init();
            }
        } catch (Exception e) {
            logger.error("Exception during BlxckoutFX initialization", e);
            throw e;
        }
    }
}
