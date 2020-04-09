package de.stylextv.hycheat.event;

import de.stylextv.hycheat.module.ModuleManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlaySoundEvent {

    @SubscribeEvent
    public void onPlaySound(net.minecraftforge.client.event.sound.PlaySoundEvent event) {
        ModuleManager.onPlaySound(event);
    }

}
