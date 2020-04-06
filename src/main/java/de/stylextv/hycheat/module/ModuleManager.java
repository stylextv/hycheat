package de.stylextv.hycheat.module;

public class ModuleManager {

    private static Module[] modules=new Module[1];
    private static Module activeModule;

    public static void setup() {
        modules[0]=new MurderMysteryModule();
    }

    public static void onTick() {
        if(activeModule!=null) activeModule.onTick();
    }
    public static void updateActiveModule(String sidebarTitle, String title) {
        if(activeModule!=null) {
            if(!activeModule.shouldBeActive(sidebarTitle,title)||!activeModule.isEnabled()) {
                activeModule.onDisable();
                activeModule=null;
            }
        } else {
            for(Module m:modules) {
                if(m.isEnabled()&&m.shouldBeActive(sidebarTitle,title)) {
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
