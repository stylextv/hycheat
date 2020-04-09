package de.stylextv.hycheat.module;

import de.stylextv.hycheat.world.GlowManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

import java.util.HashMap;
import java.util.List;

public class PropHuntModule extends Module {

    private boolean isSeeker;

    private HashMap<ArmorStandEntity,Integer> glowTimers=new HashMap<>();

    private ModuleSetting showHiders;
    private ModuleSetting showSeekers;

    public PropHuntModule() {
        super(
                "prop_hunt", "Prop Hunt", Items.TNT,
                "This module helps you by marking players in the prop hunt gamemode."
        );
        setSettings(new ModuleSetting[] {
                showHiders=new ModuleSetting("show_hiders","Show Hiders",Items.COMPASS,"Shows the current position of hiders as seeker."),
                showSeekers=new ModuleSetting("show_seekers","Show Seekers",Items.IRON_SWORD,"Shows the positions of seekers as hider."),
        });
    }

    @Override
    public void onEnable() {
        glowTimers.clear();
    }
    @Override
    public void onDisable() {
        GlowManager.removeAllGlow();
        isSeeker=false;
    }
    @Override
    public boolean shouldBeActive(String sidebarTitle, String[] sidebar, String title) {
        if((sidebar.length==15||sidebar.length==14)&&sidebar[sidebar.length-3].endsWith(" Prop Hunt")) {
            NonNullList<ItemStack> inv=Minecraft.getInstance().player.inventory.mainInventory;
            if(!inv.get(8).getItem().equals(Items.RED_BED)) {
                boolean b=isSeeker;
                isSeeker=sidebar.length==15;
                if(isSeeker&&!b) GlowManager.removeAllGlow();
                return true;
            }
        }
        return false;
    }
    @Override
    public void onTick() {
        if(isSeeker) {
            if(showHiders.isEnabled()) {

                for(Entity e:Minecraft.getInstance().world.getAllEntities()) {
                    if(e instanceof ArmorStandEntity) {
                        ArmorStandEntity stand=(ArmorStandEntity) e;
                        if(stand.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem()!=Items.AIR||stand.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem()!=Items.AIR) {
                            Vec3d vec=stand.getPositionVector();
                            boolean addGlow=stand.lastTickPosX!=vec.getX()||stand.lastTickPosY!=vec.getY()||stand.lastTickPosZ!=vec.getZ();
                            if(addGlow) {
                                glowTimers.put(stand,20*20);
                                GlowManager.addGlow(e,GlowManager.RED);
                            } else {
                                Integer got=glowTimers.get(stand);
                                if(got!=null) {
                                    got--;
                                    if(got<=0) glowTimers.remove(stand);
                                    else glowTimers.put(stand,got);
                                } else if(GlowManager.isGlowing(e)) GlowManager.removeGlow(e);
                            }
                        }
                    }
                }

            }
        } else if(showSeekers.isEnabled()) {

            List<AbstractClientPlayerEntity> players= Minecraft.getInstance().world.getPlayers();
            for(AbstractClientPlayerEntity playerEntity: players) {
                if(playerEntity!=Minecraft.getInstance().player&&!playerEntity.isInvisible()) {
                    GlowManager.addGlow(playerEntity,GlowManager.RED);
                }
            }

        }
    }

    @Override
    public void onPlaySound(PlaySoundEvent event) {
    }
    @Override
    public void onUserClick(InputEvent.ClickInputEvent event) {
    }
    @Override
    public void onOverlayRender() {
    }

}
