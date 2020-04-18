package de.stylextv.hycheat.module;

import de.stylextv.hycheat.util.TextUtil;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

public abstract class Module {

    private String name;
    private String displayName;
    private Item item;
    private String[] description;
    private boolean enabled=true;

    private ModuleSetting[] settings;

    public Module(String name, String displayName, Item item, String description) {
        this.name=name;
        this.displayName=displayName;
        this.item=item;

        this.description=TextUtil.splitDescription(description);
    }
    protected void setSettings(ModuleSetting[] settings) {
        this.settings=settings;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract boolean shouldBeActive(String sidebarTitle, String[] sidebar, String title);
    public abstract void onTick();

    public void onPlaySound(PlaySoundEvent event) {
        //to be overridden
    }
    public void onUserClick(InputEvent.ClickInputEvent event) {
        //to be overridden
    }
    public void onOverlayRender() {
        //to be overridden
    }

    public String getName() {
        return name;
    }
    public String getDisplayName() {
        return displayName;
    }
    public Item getItem() {
        return item;
    }
    public String[] getDescription() {
        return description;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled=enabled;
    }

    public ModuleSetting[] getSettings() {
        return settings;
    }

}
