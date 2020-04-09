package de.stylextv.hycheat.gui;

import de.stylextv.hycheat.module.Module;
import de.stylextv.hycheat.module.ModuleManager;
import de.stylextv.hycheat.util.ItemUtil;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class ModuleListGui extends Gui {

    private int page=0;

    public ModuleListGui() {
        super(ContainerType.GENERIC_9X5, 5);
        for(int i=0; i<5; i++) {
            putStack(i*9,GuiManager.ITEM_GLASS);
            putStack(i*9+8,GuiManager.ITEM_GLASS);
        }
        fillModuleItems();
        fillContents();
    }
    private void fillModuleItems() {
        for(int i=0; i<5; i++) {
            int index=i+page*5;
            if(index<ModuleManager.getModules().length) {
                Module m= ModuleManager.getModules()[index];
                String[] lore=new String[2+m.getDescription().length];
                lore[0]="{\"text\":\"\"}";
                for(int j=0; j<m.getDescription().length; j++) {
                    String line=m.getDescription()[j];
                    lore[j+1]="{\"text\":\""+line+"\",\"color\":\"gray\",\"italic\":false}";
                }
                lore[lore.length-1]="{\"text\":\"\"}";
                ItemStack itemStack= ItemUtil.createItemStackLoreArray(m.getItem(), "[{\"text\":\"" + (char) 187 + " \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"" + m.getDisplayName() + "\",\"color\":\"aqua\"}]", lore);
                putStack(9*1+2+i,itemStack);
                putStack(9*3+2+i,GuiManager.ITEM_SETTINGS);
            } else {
                putStack(9*1+2+i,GuiManager.ITEM_EMPTY);
                putStack(9*3+2+i,GuiManager.ITEM_EMPTY);
            }
        }
    }
    private void fillContents() {
        for(int i=0; i<5; i++) {
            int index=i+page*5;
            if(index<ModuleManager.getModules().length) {
                Module m= ModuleManager.getModules()[index];
                if(m.isEnabled()) getSlot(9*2+2+i).putStack(GuiManager.ITEM_ENABLED);
                else getSlot(9*2+2+i).putStack(GuiManager.ITEM_DISABLED);
            } else {
                putStack(9*2+2+i,GuiManager.ITEM_EMPTY);
            }
        }
        if(page>0) putStack(19,GuiManager.ITEM_PAGE_PREVIOUS);
        else putStack(19,GuiManager.ITEM_EMPTY);
        if(page*5+4<ModuleManager.getModules().length-1) putStack(25,GuiManager.ITEM_PAGE_NEXT);
        else putStack(25,GuiManager.ITEM_EMPTY);
    }

    @Override
    public ITextComponent getTitle() {
        return new StringTextComponent("Module List");
    }
    @Override
    public void slotClick(int index) {
        if(index==19) {
            if(page>0) {
                page--;
                fillModuleItems();
                fillContents();
                playClickSound();
            }
        } else if(index==25) {
            if(page*5+4<ModuleManager.getModules().length-1) {
                page++;
                fillModuleItems();
                fillContents();
                playClickSound();
            }
        } else {
            int i=index-20+page*5;
            Module[] modules=ModuleManager.getModules();
            if(i>=0&&i< modules.length) {
                Module m=modules[i];
                m.setEnabled(!m.isEnabled());
                fillContents();
                playClickSound();
            } else {
                i-=9;
                if(i>=0&&i< modules.length) {
                    Module m=modules[i];
                    GuiManager.openGui(new ModuleSettingsGui(m,this));
                    playClickSound();
                }
            }
        }
    }

}
