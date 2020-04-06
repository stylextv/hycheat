package de.stylextv.hycheat.event;

import de.stylextv.hycheat.command.CommandHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UserChatEvent {

    @SubscribeEvent
    public void onClientChat(ClientChatEvent event) {
        String s = event.getOriginalMessage();
        if(s.startsWith("#")) {
            event.setCanceled(true);
            Minecraft.getInstance().ingameGUI.getChatGUI().addToSentMessages(s);

            String[] split = s.substring(1).split(" ");
            String cmd = split[0];
            String[] args = new String[split.length-1];
            for(int i=0; i<args.length; i++) {
                args[i]=split[i+1];
            }
            CommandHandler.onClientCommand(cmd, args);
        }
    }

}
