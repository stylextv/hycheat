package de.stylextv.hycheat.module;

import de.stylextv.hycheat.util.TextUtil;
import net.minecraft.item.Item;

public class ModuleSetting {

    private String name;
    private String displayName;
    private Item item;
    private String[] description;

    private boolean enabled=true;
    private boolean available;
    private boolean experimental;

    public ModuleSetting(String name, String displayName, Item item, String description) {
        this(name,displayName,item,description,true,false);
    }
    public ModuleSetting(String name, String displayName, Item item, String description, boolean available, boolean experimental) {
        this.name=name;
        this.displayName=displayName;
        this.item=item;

        this.description= TextUtil.splitDescription(description);

        this.available=available;
        this.experimental=experimental;
        if(!available||experimental) enabled=false;
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
        if(available) this.enabled=enabled;
    }
    public boolean getDefaultEnabled() {
        return !(!available||experimental);
    }
    public boolean isAvailable() {
        return available;
    }
    public boolean isExperimental() {
        return experimental;
    }

}
