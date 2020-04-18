package de.stylextv.hycheat.command;

import de.stylextv.hycheat.event.WorldTickEvent;
import de.stylextv.hycheat.module.Module;
import de.stylextv.hycheat.module.ModuleManager;
import de.stylextv.hycheat.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class CommandHandler {

    private static String PREFIX="§8[§9Hy§7Cheat§8] §7";

    public static void onClientCommand(String cmd, String[] args) {
        if(cmd.equalsIgnoreCase("list")) {
            sendFeedback("Available Modules: ");
            for(Module m: ModuleManager.getModules()) {
                sendModuleFeedback(m);
            }
        } else if(cmd.equalsIgnoreCase("help")) {
            sendFeedback("Available Commands: ");
            sendHelpFeedback("- §egui §8| §7Opens the gui ","#gui");
            sendHelpFeedback("- §etoggle <name> §8| §7Toggles a module ","#toggle <name>");
            sendHelpFeedback("- §elist §8| §7Lists all modules ","#list");
            sendHelpFeedback("- §ehelp §8| §7Lists all commands ","#help");
        } else if(cmd.equalsIgnoreCase("toggle")) {
            if(args.length==1) {
                String name=args[0];
                Module m=null;
                for(Module check:ModuleManager.getModules()) {
                    if(check.getName().equalsIgnoreCase(name)) {
                        m=check;
                        break;
                    }
                }
                if(m!=null) {
                    m.setEnabled(!m.isEnabled());
                    if(m.isEnabled()) sendFeedback("Module §b"+m.getDisplayName()+"§7 is now §aenabled§7.");
                    else sendFeedback("Module §b"+m.getDisplayName()+"§7 is now §cdisabled§7.");
                } else sendFeedback("Could not find a module: §c"+name);
            } else sendFeedback("Please use #§ctoggle <name>§7 to toggle a module.");
        } else if(cmd.equalsIgnoreCase("gui")) {
            WorldTickEvent.openGui=true;
        } else {
            sendFeedback("Please use #§chelp§7 to get a list of commands.");
        }
    }

    public static void sendFeedback(String s) {
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(TextUtil.convertToTextComponent(PREFIX+s));
    }
    public static void sendHelpFeedback(String s, String suggestion) {
        TextComponent base = TextUtil.convertToTextComponent(PREFIX+s);
        TextComponent click = TextUtil.convertToTextComponent("§8[§3Click here§8]");
        click.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextUtil.convertToTextComponent("§7Click here to §eenter§7 this command.")));
        click.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestion));
        base.appendSibling(click);
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(base);
    }
    public static void sendModuleFeedback(Module m) {
        String s="- §b"+m.getName().toUpperCase()+"§7 (Enabled: ";
        String s2="§7)";
        String sClick="§a"+(char)(10004);
        if(!m.isEnabled()) sClick="§c"+(char)(10060);

        TextComponent base = TextUtil.convertToTextComponent(PREFIX+s);
        TextComponent click = TextUtil.convertToTextComponent(sClick);
        click.getStyle().setBold(true);
        click.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextUtil.convertToTextComponent("§7Click here to §etoggle§7 this module.")));
        click.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "#toggle "+m.getName()));
        base.appendSibling(click);
        base.appendSibling(TextUtil.convertToTextComponent(s2));
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(base);
    }

}
