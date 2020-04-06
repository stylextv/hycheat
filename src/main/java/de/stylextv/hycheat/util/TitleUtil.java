package de.stylextv.hycheat.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

public class TitleUtil {

    private static Field titleField;
    private static Field subTitleField;

    public static String getCurrentTitle() {
        if(titleField==null) {
            try {
                titleField=ObfuscationReflectionHelper.findField(IngameGui.class, "field_175201_x");
            } catch(Exception ex) {
                titleField=ObfuscationReflectionHelper.findField(IngameGui.class, "displayedTitle");
            }
        }
        try {
            return (String) titleField.get(Minecraft.getInstance().ingameGUI);
        } catch (IllegalAccessException ex) {}
        return "";
    }
    public static String getCurrentSubTitle() {
        if(subTitleField==null) {
            try {
                subTitleField=ObfuscationReflectionHelper.findField(IngameGui.class, "field_175200_y");
            } catch(Exception ex) {
                subTitleField=ObfuscationReflectionHelper.findField(IngameGui.class, "displayedSubTitle");
            }
        }
        try {
            return (String) subTitleField.get(Minecraft.getInstance().ingameGUI);
        } catch (IllegalAccessException ex) {}
        return "";
    }

}
