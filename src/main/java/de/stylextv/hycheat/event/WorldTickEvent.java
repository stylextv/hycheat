package de.stylextv.hycheat.event;

import de.stylextv.hycheat.gui.GuiManager;
import de.stylextv.hycheat.gui.ModuleListGui;
import de.stylextv.hycheat.module.ModuleManager;
import de.stylextv.hycheat.util.ScoreboardUtil;
import de.stylextv.hycheat.util.TitleUtil;
import de.stylextv.hycheat.world.GlowManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldTickEvent {

    public static boolean openGui;

    private int updateTimer=0;

    @SubscribeEvent
    public void onEnter(EntityJoinWorldEvent event) {
        if(event.getEntity() == Minecraft.getInstance().player) {
            GlowManager.onEnter();
            ModuleManager.disableActiveModule();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if(Minecraft.getInstance().world!=null) {
            for(Entity e:Minecraft.getInstance().world.getAllEntities()) {
                GlowManager.refreshGlow(e);
            }
            GlowManager.refreshGlowStates();

            if(event.type == TickEvent.Type.CLIENT) {
                updateTimer++;
                if(updateTimer==30) {
                    updateTimer=0;

                    String[] sidebar=ScoreboardUtil.getSidebar();
                    String title="";
                    if(sidebar.length!=0) title=sidebar[0];
                    ModuleManager.updateActiveModule(title, sidebar, TitleUtil.getCurrentTitle());
                }
                ModuleManager.onTick();

                if(openGui) {
                    openGui=false;
                    GuiManager.openGui(new ModuleListGui());
                }

                GlowManager.clearUpGlow();
            }
        }
    }

}
