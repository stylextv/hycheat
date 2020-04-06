package de.stylextv.hycheat.config;

import de.stylextv.hycheat.main.HyCheat;
import de.stylextv.hycheat.module.Module;
import de.stylextv.hycheat.module.ModuleManager;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = HyCheat.MOD_ID, bus = Bus.MOD)
public class VarConfig {

    public static class Common {

        private ConfigSection[] sections;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("modules");

            sections=new ConfigSection[ModuleManager.getModules().length];
            for(int i=0; i<ModuleManager.getModules().length; i++) {
                Module m=ModuleManager.getModules()[i];
                sections[i]=new ConfigSection(m,builder);
            }

            builder.pop();
        }

        public void loadAll() {
            for(ConfigSection section:sections) section.load();
        }
        public void saveAll() {
            for(ConfigSection section:sections) section.save();
        }

    }

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    static {
        final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading event) {

    }

    @SubscribeEvent
    public static void onFileChange(final ModConfig.Reloading event) {

    }

}