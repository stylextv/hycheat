package de.stylextv.hycheat.config;

import de.stylextv.hycheat.module.Module;
import de.stylextv.hycheat.module.ModuleSetting;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigSection {

    private Module m;
    private ForgeConfigSpec.BooleanValue[] values;

    public ConfigSection(Module m, ForgeConfigSpec.Builder builder) {
        this.m=m;
        builder.push(m.getName());

        values=new ForgeConfigSpec.BooleanValue[m.getSettings().length+1];
        values[0]=builder.define("enabled", true);
        for(int i = 0; i< m.getSettings().length; i++) {
            ModuleSetting setting=m.getSettings()[i];
            values[i+1]=builder.define(setting.getName(), true);
        }

        builder.pop();
    }

    public void load() {
        m.setEnabled(values[0].get());
        for(int i = 0; i< m.getSettings().length; i++) {
            ModuleSetting setting=m.getSettings()[i];
            setting.setEnabled(values[i+1].get());
        }
    }
    public void save() {
        values[0].set(m.isEnabled());
        for(int i = 0; i< m.getSettings().length; i++) {
            ModuleSetting setting=m.getSettings()[i];
            values[i+1].set(setting.isEnabled());
        }
    }

}
