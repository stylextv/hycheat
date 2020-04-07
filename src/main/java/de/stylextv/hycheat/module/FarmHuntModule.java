package de.stylextv.hycheat.module;

import de.stylextv.hycheat.world.GlowManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;

import java.util.List;

public class FarmHuntModule extends Module {

    private static EntityType[] ANIMAL_TYPES=new EntityType[] {
            EntityType.PIG,
            EntityType.CHICKEN,
            EntityType.HORSE,
            EntityType.COW,
            EntityType.OCELOT,
            EntityType.SHEEP,
            EntityType.DONKEY,
            EntityType.WOLF
    };

    private boolean hasRole;
    private boolean isHunter;

    private ModuleSetting showAnimals,showHunters;

    public FarmHuntModule() {
        super(
                "farm_hunt", "Farm Hunt", Items.WHEAT,
                "This module helps you by marking players in the farm hunt gamemode."
        );
        setSettings(new ModuleSetting[] {
                showAnimals=new ModuleSetting("show_ani","Show Animals",Items.WHEAT_SEEDS,"Shows the current position of animals as hunter."),
                showHunters=new ModuleSetting("show_hunt","Show Hunters",Items.BOW,"Shows the positions of hunters as animal."),
        });
    }

    @Override
    public void onEnable() {

    }
    @Override
    public void onDisable() {
        GlowManager.removeAllGlow();
        isHunter=false;
        hasRole=false;
    }
    @Override
    public boolean shouldBeActive(String sidebarTitle, String title) {
        if("FARM HUNT".equals(sidebarTitle)) {
            NonNullList<ItemStack> inv=Minecraft.getInstance().player.inventory.mainInventory;
            Item firstSlot=inv.get(0).getItem();
            if(inv.get(8).getItem()!=Items.RED_BED&&firstSlot!=Items.WHEAT) {
                if(!hasRole) {
                    hasRole=true;
                    isHunter=firstSlot!=Items.BLAZE_ROD;
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public void onTick() {
        if(isHunter) {
            if(showAnimals.isEnabled()) {

                for(Entity e:Minecraft.getInstance().world.getAllEntities()) {
                    if(isAnimal(e)) {
                        if(e.getPitchYaw().x!=0) GlowManager.addGlow(e,GlowManager.RED);
                        else if(GlowManager.isGlowing(e)) GlowManager.removeGlow(e);
                    }
                }

            }
        } else if(showHunters.isEnabled()) {

            List<AbstractClientPlayerEntity> players= Minecraft.getInstance().world.getPlayers();
            for(AbstractClientPlayerEntity playerEntity: players) {
                if(playerEntity!=Minecraft.getInstance().player&&!playerEntity.isInvisible()) {
                    GlowManager.addGlow(playerEntity,GlowManager.RED);
                }
            }

        }
    }

    private static boolean isAnimal(Entity e) {
        EntityType type=e.getType();
        for(EntityType check:ANIMAL_TYPES) {
            if(type==check) return true;
        }
        return false;
    }

}
