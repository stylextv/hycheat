package de.stylextv.hycheat.event;

import de.stylextv.hycheat.module.ModuleManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RenderEvent {

    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            ModuleManager.onOverlayRender();
        }
    }

}
