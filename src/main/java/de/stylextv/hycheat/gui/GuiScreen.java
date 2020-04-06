package de.stylextv.hycheat.gui;

import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;

public class GuiScreen extends ChestScreen {

    private Gui gui;

    public GuiScreen(Gui p_i51095_1_, PlayerInventory p_i51095_2_, ITextComponent p_i51095_3_) {
        super(p_i51095_1_, p_i51095_2_, p_i51095_3_);
        this.gui=p_i51095_1_;
    }

    @Override
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        if(slotIn!=null) {
            gui.slotClick(slotId);
        }
    }

}
