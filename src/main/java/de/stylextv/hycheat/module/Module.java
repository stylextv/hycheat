package de.stylextv.hycheat.module;

import net.minecraft.item.Item;

import java.util.ArrayList;

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

        ArrayList<String> descriptionLines=new ArrayList<>();
        String currentLine="";
        for(String word:description.split(" ")) {
            if(currentLine.length()+word.length()+1>26) {
                descriptionLines.add(currentLine);
                currentLine="";
            } else if(currentLine.length()!=0) currentLine=currentLine+" ";
            currentLine=currentLine+word;
        }
        descriptionLines.add(currentLine);
        this.description=new String[descriptionLines.size()];
        for(int i=0; i<this.description.length; i++) {
            this.description[i]=descriptionLines.get(i);
        }
    }
    protected void setSettings(ModuleSetting[] settings) {
        this.settings=settings;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract boolean shouldBeActive(String sidebarTitle, String title);
    public abstract void onTick();

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
