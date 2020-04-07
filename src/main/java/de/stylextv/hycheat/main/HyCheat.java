package de.stylextv.hycheat.main;

import de.stylextv.hycheat.config.VarConfig;
import de.stylextv.hycheat.event.UserChatEvent;
import de.stylextv.hycheat.event.WorldTickEvent;
import de.stylextv.hycheat.gui.GuiManager;
import de.stylextv.hycheat.module.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(HyCheat.MOD_ID)
public class HyCheat {

    public static final String MOD_ID="hycheat";

    private static HyCheat hyCheat;
    public static HyCheat getInstance() {
        return hyCheat;
    }

    public HyCheat() {
        hyCheat=this;

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new UserChatEvent());
        MinecraftForge.EVENT_BUS.register(new WorldTickEvent());

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        ModuleManager.setup();
        GuiManager.setup();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, VarConfig.COMMON_SPEC, "hycheat-common.toml");
    }

    private void setup(final FMLCommonSetupEvent event) {
        VarConfig.COMMON.loadAll();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void shutdown() {
        VarConfig.COMMON.saveAll();
    }

}
