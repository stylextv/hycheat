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
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.InputEvent;

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
            EntityType.WOLF,
            EntityType.CAT
    };

    private boolean hasRole;
    private boolean isHunter;

    private ModuleSetting showAnimals;
    private ModuleSetting showHunters;
    private ModuleSetting hitAssist;

    public FarmHuntModule() {
        super(
                "farm_hunt", "Farm Hunt", Items.WHEAT,
                "This module helps you by marking players in the farm hunt gamemode."
        );
        setSettings(new ModuleSetting[] {
                showAnimals=new ModuleSetting("show_ani","Show Animals",Items.WHEAT_SEEDS,"Shows the current position of animals as hunter."),
                showHunters=new ModuleSetting("show_hunt","Show Hunters",Items.BOW,"Shows the positions of hunters as animal."),
                hitAssist=new ModuleSetting("hit_assist","Hit-Assistant",Items.TOTEM_OF_UNDYING,"Helps you hit animals with your sword as a hunter in order to compensate the lag on the hypixel network.")
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
    public boolean shouldBeActive(String sidebarTitle, String[] sidebar, String title) {
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

    @Override
    public void onUserClick(InputEvent.ClickInputEvent event) {
        if(hitAssist.isEnabled()&&isHunter&&event.isAttack()) {
            Entity target=null;
            double targetLookDis=0;
            for(Entity e:Minecraft.getInstance().world.getAllEntities()) {
                if(!e.equals(Minecraft.getInstance().player)&&e.isAlive()&&e.getDistanceSq(Minecraft.getInstance().player)<9&&GlowManager.isGlowing(e)&&isAnimal(e)) {
                    Vec3d vec1=Minecraft.getInstance().player.getPositionVec().add(0,Minecraft.getInstance().player.getEyeHeight(),0);
                    Vec3d vec2=e.getPositionVec().add(0,e.getEyeHeight(),0);
                    double dX = vec1.getX() - vec2.getX();
                    double dY = vec1.getY() - vec2.getY();
                    double dZ = vec1.getZ() - vec2.getZ();
                    double yaw = Math.toDegrees(Math.atan2(dZ, dX))+90;
                    if(yaw>180) yaw-=360;
                    double pitch = -(Math.toDegrees(Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI)-270);
                    double playerYaw=Minecraft.getInstance().player.rotationYaw;
                    while(playerYaw>180) playerYaw-=360;
                    while(playerYaw<-180) playerYaw+=360;
                    double yawDisDirect=Math.abs(playerYaw-yaw);
                    double yawDisIndirect=Math.min(playerYaw,yaw)+180 + (180-Math.max(playerYaw,yaw));
                    double pitchDis=Math.abs(pitch-Minecraft.getInstance().player.rotationPitch);
                    double disTotal=pitchDis+Math.min(yawDisDirect,yawDisIndirect);
                    if(disTotal<=70&&(target==null||disTotal<targetLookDis)) {
                        target=e;
                        targetLookDis=disTotal;
                        break;
                    }
                }
            }
            if(target!=null) {
                event.setCanceled(true);
                Minecraft.getInstance().playerController.attackEntity(Minecraft.getInstance().player,target);
            }
        }
    }

    private static boolean isAnimal(Entity e) {
        EntityType type=e.getType();
        for(EntityType check:ANIMAL_TYPES) {
            if(type.equals(check)) return true;
        }
        return false;
    }

}
