package de.stylextv.hycheat.event;

import de.stylextv.hycheat.module.ModuleManager;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UserAttackEvent {

    @SubscribeEvent
    public void onUserAttack(InputEvent.ClickInputEvent event) {
        ModuleManager.onUserClick(event);
    }

}
