package de.stylextv.hycheat.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

public abstract class Gui extends ChestContainer {

    public Gui(ContainerType<?> type, int rows) {
        super(type, 0, Minecraft.getInstance().player.inventory, new Inventory(9*rows), rows);
    }

    public abstract ITextComponent getTitle();
    public abstract void slotClick(int index);

    public void putStack(int index, ItemStack itemStack) {
        getSlot(index).putStack(itemStack);
    }
    public void playClickSound() {
        Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.5f, 2);
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        return new ItemStack(Items.AIR);
    }

}
