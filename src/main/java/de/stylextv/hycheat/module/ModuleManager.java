package de.stylextv.hycheat.module;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class ModuleManager {

    private static Module[] modules=new Module[4];
    private static Module activeModule;

    public static void setup() {
        modules[0]=new MurderMysteryModule();
        modules[1]=new FarmHuntModule();
        modules[2]=new PartyPooperModule();
        modules[3]=new PropHuntModule();
    }

    public static void onTick() {
        if(activeModule!=null) activeModule.onTick();
    }
    public static void onPlaySound(PlaySoundEvent event) {
        if(activeModule!=null) activeModule.onPlaySound(event);
    }
    public static void onUserClick(InputEvent.ClickInputEvent event) {
        if(activeModule!=null) activeModule.onUserClick(event);
    }
    public static void onOverlayRender() {
        if(activeModule!=null) activeModule.onOverlayRender();
    }
    public static void updateActiveModule(String sidebarTitle, String[] sidebar, String title) {
        if(activeModule!=null) {
            if(!activeModule.shouldBeActive(sidebarTitle,sidebar,title)||!activeModule.isEnabled()) {
                activeModule.onDisable();
                activeModule=null;
            }
        } else {
            for(Module m:modules) {
                if(m.isEnabled()&&m.shouldBeActive(sidebarTitle,sidebar,title)) {
                    activeModule=m;
                    m.onEnable();
                    break;
                }
            }
        }
    }
    public static void disableActiveModule() {
        if(activeModule!=null) activeModule.onDisable();
        activeModule=null;
    }

    public static Module[] getModules() {
        return modules;
    }

}
