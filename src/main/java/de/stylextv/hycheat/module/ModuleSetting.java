package de.stylextv.hycheat.module;

import de.stylextv.hycheat.util.TextUtil;
import net.minecraft.item.Item;

public class ModuleSetting {

    private String name;
    private String displayName;
    private Item item;
    private String[] description;
    private boolean enabled=true;

    public ModuleSetting(String name, String displayName, Item item, String description) {
        this.name=name;
        this.displayName=displayName;
        this.item=item;

        this.description= TextUtil.splitDescription(description);
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

}
