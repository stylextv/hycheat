package de.stylextv.hycheat.module;

import net.minecraft.item.Item;

import java.util.ArrayList;

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
