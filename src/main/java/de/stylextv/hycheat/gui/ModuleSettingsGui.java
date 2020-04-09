package de.stylextv.hycheat.gui;

import de.stylextv.hycheat.module.Module;
import de.stylextv.hycheat.module.ModuleSetting;
import de.stylextv.hycheat.util.ItemUtil;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ModuleSettingsGui extends Gui {

    private int page=0;

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

        fillSettingItems();
        fillContents();
    }
    private void fillSettingItems() {
        for(int i=0; i<5; i++) {
            int index=i+page*5;
            if(index< m.getSettings().length) {
                ModuleSetting setting=m.getSettings()[index];
                int length=2+setting.getDescription().length;
                if(!setting.isAvailable()) length+=3;
                else if(setting.isExperimental()) length+=4;

                String[] lore=new String[length];
                lore[0]="{\"text\":\"\"}";
                for(int j=0; j<setting.getDescription().length; j++) {
                    String line=setting.getDescription()[j];
                    lore[j+1]="{\"text\":\""+line+"\",\"color\":\"gray\",\"italic\":false}";
                }
                lore[(2+setting.getDescription().length)-1]="{\"text\":\"\"}";
                if(!setting.isAvailable()) {
                    lore[length-3]="{\"text\":\"This feature is not\",\"color\":\"red\",\"italic\":false}";
                    lore[length-2]="{\"text\":\"available yet.\",\"color\":\"red\",\"italic\":false}";
                    lore[length-1]="{\"text\":\"\"}";
                } else if(setting.isExperimental()) {
                    lore[length-4]="{\"text\":\"Warning!\",\"color\":\"red\",\"italic\":false,\"bold\":true,\"underlined\":true}";
                    lore[length-3]="{\"text\":\"This is an experimental\",\"color\":\"red\",\"italic\":false}";
                    lore[length-2]="{\"text\":\"feature.\",\"color\":\"red\",\"italic\":false}";
                    lore[length-1]="{\"text\":\"\"}";
                }

                ItemStack itemStack= ItemUtil.createItemStackLoreArray(setting.getItem(), "[{\"text\":\"" + (char) 187 + " \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"" + setting.getDisplayName() + "\",\"color\":\"aqua\"}]", lore);
                putStack(9*1+2+i,itemStack);
            } else {
                putStack(9*1+2+i,GuiManager.ITEM_EMPTY);
            }
        }
    }
    private void fillContents() {
        for(int i=0; i<5; i++) {
            int index=i+page*5;
            if(index< m.getSettings().length) {
                ModuleSetting setting = m.getSettings()[index];
                if (setting.isAvailable()) {
                    if (setting.isEnabled()) getSlot(9 * 2 + 2 + i).putStack(GuiManager.ITEM_SETTING_ENABLED);
                    else getSlot(9 * 2 + 2 + i).putStack(GuiManager.ITEM_SETTING_DISABLED);
                } else getSlot(9 * 2 + 2 + i).putStack(GuiManager.ITEM_EMPTY);
            } else getSlot(9 * 2 + 2 + i).putStack(GuiManager.ITEM_EMPTY);
        }
        if(page>0) putStack(19,GuiManager.ITEM_PAGE_PREVIOUS);
        else putStack(19,GuiManager.ITEM_EMPTY);
        if(page*5+4<m.getSettings().length-1) putStack(25,GuiManager.ITEM_PAGE_NEXT);
        else putStack(25,GuiManager.ITEM_EMPTY);
    }

    @Override
    public ITextComponent getTitle() {
        return new StringTextComponent("Module Settings");
    }
    @Override
    public void slotClick(int index) {
        if(index==19) {
            if(page>0) {
                page--;
                fillSettingItems();
                fillContents();
                playClickSound();
            }
        } else if(index==25) {
            if(page*5+4<m.getSettings().length-1) {
                page++;
                fillSettingItems();
                fillContents();
                playClickSound();
            }
        } else if(index==3*9+4) {
            GuiManager.openGui(previous);
            playClickSound();
        } else {
            int i=index-20+page*5;
            ModuleSetting[] settings=m.getSettings();
            if(i>=0&&i< settings.length) {
                ModuleSetting setting=settings[i];
                if(setting.isAvailable()) {
                    setting.setEnabled(!setting.isEnabled());
                    fillContents();
                    playClickSound();
                }
            }
        }
    }

}
