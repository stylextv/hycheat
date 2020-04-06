package de.stylextv.hycheat.util;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

public class TextUtil {

    public static TextComponent convertToTextComponent(String s) {
        String[] split=s.split("§");
        TextComponent base = new StringTextComponent("");
        for(String segment : split) {
            if(!segment.isEmpty()) {
                StringTextComponent comp = new StringTextComponent(segment.substring(1));
                comp.getStyle().setColor(TextFormatting.fromFormattingCode(segment.charAt(0)));
                base.appendSibling(comp);
            }
        }
        return base;
    }

}
