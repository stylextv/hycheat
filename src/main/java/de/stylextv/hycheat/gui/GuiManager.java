package de.stylextv.hycheat.gui;

import de.stylextv.hycheat.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.StringTextComponent;

public class GuiManager {

    public static ItemStack ITEM_EMPTY;
    public static ItemStack ITEM_GLASS;
    public static ItemStack ITEM_ENABLED;
    public static ItemStack ITEM_DISABLED;
    public static ItemStack ITEM_SETTING_ENABLED;
    public static ItemStack ITEM_SETTING_DISABLED;
    public static ItemStack ITEM_SETTINGS;
    public static ItemStack ITEM_BACK;
    public static ItemStack ITEM_PAGE_NEXT;
    public static ItemStack ITEM_PAGE_PREVIOUS;

    public static void setup() {
        ITEM_EMPTY=new ItemStack(Items.AIR);

        ITEM_GLASS=new ItemStack(Items.BLACK_STAINED_GLASS_PANE);
        ITEM_GLASS.setDisplayName(new StringTextComponent(""));

        ITEM_ENABLED= ItemUtil.createItemStack(
                Items.LIME_DYE,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"Status: \",\"color\":\"gray\"},{\"text\":\"enabled\",\"color\":\"green\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to disable\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"this module.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );
        ITEM_DISABLED= ItemUtil.createItemStack(
                Items.GRAY_DYE,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"Status: \",\"color\":\"gray\"},{\"text\":\"disabled\",\"color\":\"red\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to enable\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"this module.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );
        ITEM_SETTING_ENABLED= ItemUtil.createItemStack(
                Items.LIME_DYE,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"Status: \",\"color\":\"gray\"},{\"text\":\"enabled\",\"color\":\"green\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to disable\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"this setting.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );
        ITEM_SETTING_DISABLED= ItemUtil.createItemStack(
                Items.GRAY_DYE,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"Status: \",\"color\":\"gray\"},{\"text\":\"disabled\",\"color\":\"red\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to enable\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"this setting.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );

        ITEM_SETTINGS= ItemUtil.createItemStack(
                Items.COMPARATOR,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"Settings\",\"color\":\"yellow\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to open the\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"settings for this module.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );

        ITEM_BACK= ItemUtil.createItemStack(
                Items.BARRIER,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"Close\",\"color\":\"red\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to close this\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"gui.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );

        ITEM_PAGE_NEXT= ItemUtil.createItemStack(
                Items.OAK_BUTTON,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"--->\",\"color\":\"gold\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to see the\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"next page.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );
        ITEM_PAGE_PREVIOUS= ItemUtil.createItemStack(
                Items.OAK_BUTTON,
                "[{\"text\":\""+(char)187+" \",\"color\":\"dark_gray\",\"italic\":false},{\"text\":\"<---\",\"color\":\"gold\"}]",
                "{\"text\":\"\"}",
                "{\"text\":\"Click here to see the\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"previous page.\",\"color\":\"gray\",\"italic\":false}",
                "{\"text\":\"\"}"
        );
    }

    public static void openGui(Gui gui) {
        Minecraft.getInstance().player.openContainer=gui;
        Minecraft.getInstance().displayGuiScreen(new GuiScreen(gui, Minecraft.getInstance().player.inventory, gui.getTitle()));
    }

}
