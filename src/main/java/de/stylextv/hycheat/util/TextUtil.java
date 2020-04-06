package de.stylextv.hycheat.util;

import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;

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

    public static String[] splitDescription(String description) {
        ArrayList<String> descriptionLines=new ArrayList<>();
        String currentLine="";
        for(String word:description.split(" ")) {
            if(currentLine.length()+word.length()+1>26) {
                descriptionLines.add(currentLine);
                currentLine="";
            } else if(currentLine.length()!=0) currentLine=currentLine+" ";
            currentLine=currentLine+word;
        }
        descriptionLines.add(currentLine);
        String[] arr=new String[descriptionLines.size()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=descriptionLines.get(i);
        }
        return arr;
    }

}
