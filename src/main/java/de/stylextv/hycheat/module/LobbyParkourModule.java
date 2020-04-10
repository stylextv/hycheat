package de.stylextv.hycheat.module;

import net.minecraft.item.Items;

public class LobbyParkourModule extends Module {

    private ModuleSetting showAnimals;
    private ModuleSetting showHunters;

    public LobbyParkourModule() {
        super(
                "farm_hunt", "Farm Hunt", Items.WHEAT,
                "This module helps you by marking players in the farm hunt gamemode."
        );
        setSettings(new ModuleSetting[] {
                showAnimals=new ModuleSetting("show_ani","Show Animals",Items.WHEAT_SEEDS,"Shows the current position of animals as hunter."),
                showHunters=new ModuleSetting("show_hunt","Show Hunters",Items.BOW,"Shows the positions of hunters as animal.")
        });
    }

    @Override
    public void onEnable() {

    }
    @Override
    public void onDisable() {

    }
    @Override
    public boolean shouldBeActive(String sidebarTitle, String[] sidebar, String title) {
        return false;
    }
    @Override
    public void onTick() {

    }

}
