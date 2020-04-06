package de.stylextv.hycheat.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

public class ItemUtil {

    public static ItemStack createItemStack(Item item, String title, String... lore) {
        return createItemStackLoreArray(item, title, lore);
    }
    public static ItemStack createItemStackLoreArray(Item item, String title, String[] lore) {
        ItemStack itemStack=new ItemStack(item);
        CompoundNBT nbt = itemStack.getOrCreateTag();

        ListNBT loreList = new ListNBT();
        for(String s:lore) loreList.add(StringNBT.func_229705_a_(s));

        CompoundNBT display = new CompoundNBT();
        display.put("Lore", loreList);
        display.put("Name", StringNBT.func_229705_a_(title));

        nbt.put("display", display);
        itemStack.setTag(nbt);
        return itemStack;
    }

}
