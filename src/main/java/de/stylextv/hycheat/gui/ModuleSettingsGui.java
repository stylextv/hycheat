package de.stylextv.hycheat.gui;

import de.stylextv.hycheat.module.Module;
import de.stylextv.hycheat.module.ModuleSetting;
import de.stylextv.hycheat.util.ItemUtil;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ModuleSettingsGui extends Gui {

    private Module m;
    private Gui previous;

    public ModuleSettingsGui(Module m, Gui previous) {
        super(ContainerType.GENERIC_9X4, 4);
        this.m=m;
        this.previous=previous;
        for(int i=0; i<4; i++) {
            putStack(i*9,GuiManager.ITEM_GLASS);
            putStack(i*9+8,GuiManager.ITEM_GLASS);
        }
        putStack(3*9+4,GuiManager.ITEM_BACK);

        for(int i=0; i<m.getSettings().length; i++) {
            ModuleSetting setting=m.getSettings()[i];
            String[] lore=new String[2+setting.getDescription().length];
            lore[0]="{\"text\":\"\"}";
            for(int j=0; j<setting.getDescription().length; j++) {
                String line=setting.getDescription()[j];
                lore[j+1]="{\"text\":\""+line+"\",\"color\":\"gray\",\"italic\":false}";
            }
            lore[lore.length-1]="{\"text\":\"\"}";
            ItemStack itemStack= ItemUtil.createItemStackLoreArray(setting.getItem(), "[{\"text\":\"" + (char) 187 + " \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"" + setting.getDisplayName() + "\",\"color\":\"aqua\"}]", lore);
            putStack(9*1+2+i,itemStack);
        }
        fillContents();
    }
    public void fillContents() {
        int i=0;
        for(ModuleSetting setting: m.getSettings()) {
            if(setting.isEnabled()) getSlot(9*2+2+i).putStack(GuiManager.ITEM_SETTING_ENABLED);
            else getSlot(9*2+2+i).putStack(GuiManager.ITEM_SETTING_DISABLED);

            i++;
        }
    }

    @Override
    public ITextComponent getTitle() {
        return new StringTextComponent("Module Settings");
    }
    @Override
    public void slotClick(int index) {
        if(index==3*9+4) {
            GuiManager.openGui(previous);
            playClickSound();
        } else {
            int i=index-20;
            ModuleSetting[] settings=m.getSettings();
            if(i>=0&&i< settings.length) {
                ModuleSetting setting=settings[i];
                setting.setEnabled(!setting.isEnabled());
                fillContents();
                playClickSound();
            }
        }
    }

}
