package de.stylextv.hycheat.module;

import de.stylextv.hycheat.command.CommandHandler;
import de.stylextv.hycheat.util.ScoreboardUtil;
import de.stylextv.hycheat.world.GlowManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class MurderMysteryModule extends Module {

    private static Item[] SWORD_SKINS=new Item[] {
            Items.IRON_SWORD,
            Items.STONE_SWORD,
            Items.IRON_SHOVEL,
            Items.STICK,
            Items.WOODEN_AXE,
            Items.WOODEN_SWORD,
            Items.DEAD_BUSH,
            Items.STONE_SHOVEL,
            Items.BLAZE_ROD,
            Items.DIAMOND_SHOVEL,
            Items.FEATHER,
            Items.PUMPKIN_PIE,
            Items.GOLDEN_PICKAXE,
            Items.APPLE,
            Items.NAME_TAG,
            Items.SPONGE,
            Items.CARROT_ON_A_STICK,
            Items.BONE,
            Items.CARROT,
            Items.GOLDEN_CARROT,
            Items.COOKIE,
            Items.DIAMOND_AXE,
            Items.GOLDEN_SWORD,
            Items.DIAMOND_SWORD,
            Items.DIAMOND_HOE,
            Items.SHEARS,
            Items.SALMON,
            Items.REDSTONE_TORCH,
            Items.ROSE_BUSH,
            Items.OAK_BOAT
    };

    private int foundMurderers;
    private boolean hasRole;
    private boolean isMurderer;

    private ModuleSetting showMurderer;
    private ModuleSetting showGold;
    private ModuleSetting showBow;
    private ModuleSetting showPlayers;

    public MurderMysteryModule() {
        super(
                "mm_classic", "Murder Mystery Classic/Double Up", Items.IRON_SWORD,
                "This module helps you by marking players and other objects in the classic and \\\"double up\\\" murder mystery gamemodes."
        );
        setSettings(new ModuleSetting[] {
                showMurderer=new ModuleSetting("show_mur","Show Murderer",Items.DIAMOND_AXE,"Shows the current position of the murderer(s) as innocent."),
                showGold=new ModuleSetting("show_gold","Show Gold",Items.GOLD_INGOT,"Shows the position of gold ingots as innocent."),
                showBow=new ModuleSetting("show_bow","Show Bow",Items.BOW,"Shows the current position of a bow as innocent."),
                showPlayers=new ModuleSetting("show_players","Show Players",Items.PLAYER_HEAD,"Shows the current position of the players as murderer.")
        });
    }

    @Override
    public void onEnable() {
        foundMurderers=0;
    }
    @Override
    public void onDisable() {
        GlowManager.removeAllGlow();
        isMurderer=false;
        hasRole=false;
    }
    @Override
    public boolean shouldBeActive(String sidebarTitle, String[] sidebar, String title) {
        if("MURDER MYSTERY".equals(sidebarTitle)) {
            NonNullList<ItemStack> inv=Minecraft.getInstance().player.inventory.mainInventory;
            if(inv.get(4).getItem()==Items.FILLED_MAP&&inv.get(7).getItem()==Items.ARMOR_STAND) {
                if(!hasRole&&title.length()>=2) {
                    hasRole=true;
                    isMurderer=title.charAt(1)=='c';
                    if(isMurderer) {
                        GlowManager.removeAllGlow();
                    }
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public void onTick() {
        if(isMurderer) {
            if(showPlayers.isEnabled()) {
                ArrayList<String> names= ScoreboardUtil.getTablist();

                List<AbstractClientPlayerEntity> players= Minecraft.getInstance().world.getPlayers();
                for(AbstractClientPlayerEntity playerEntity: players) {
                    if(playerEntity!=Minecraft.getInstance().player) {
                        if(playerEntity.isSleeping()||(!names.contains(playerEntity.getName().getString()))) {
                            if(GlowManager.isGlowing(playerEntity)) GlowManager.removeGlow(playerEntity);
                            else playerEntity.setGlowing(false);
                        } else {
                            Item item=playerEntity.getHeldItem(Hand.MAIN_HAND).getItem();
                            if(item == Items.BOW || item == Items.ARROW) {
                                ScorePlayerTeam color=GlowManager.getGlowColor(playerEntity);
                                if(color!=null&&color!=GlowManager.YELLOW) GlowManager.removeGlow(playerEntity);
                                GlowManager.addGlow(playerEntity,GlowManager.YELLOW);
                            } else GlowManager.addGlow(playerEntity,GlowManager.GREEN);
                        }
                    }
                }
            }
        } else {
            if(foundMurderers<2&&showMurderer.isEnabled()) {
                List<AbstractClientPlayerEntity> players= Minecraft.getInstance().world.getPlayers();
                for(AbstractClientPlayerEntity playerEntity: players) {
                    if(playerEntity!=Minecraft.getInstance().player&&!playerEntity.isSleeping()) {
                        Item item=playerEntity.getHeldItem(Hand.MAIN_HAND).getItem();
                        if(item != Items.AIR || item != Items.BOW || item != Items.ARROW) {
                            if(isSwordItem(item)&&!GlowManager.isGlowing(playerEntity)) {
                                foundMurderers++;
                                CommandHandler.sendFeedback("A murderer has been located: §4"+playerEntity.getName().getUnformattedComponentText());
                                CommandHandler.sendFeedback("They are using this as a weapon: §b"+item.getName().getString());
                                GlowManager.addGlow(playerEntity,GlowManager.RED_DARK);
                                Vec3d playerPos=Minecraft.getInstance().player.getPositionVector();
                                Vec3d targetPos=playerEntity.getPositionVector();
                                Vec3d loc=playerPos.add(targetPos.subtract(playerPos).normalize().mul(3,3,3));
                                Minecraft.getInstance().world.playSound(loc.getX(),loc.getY(),loc.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS,1,1,false);
                            }
                        }
                    }
                }
            }

            for(Entity e:Minecraft.getInstance().world.getAllEntities()) {
                if(showGold.isEnabled()&&e instanceof ItemEntity) {
                    if(((ItemEntity)e).getItem().getItem()==Items.GOLD_INGOT) {
                        GlowManager.addGlow(e,GlowManager.GOLD);
                    }
                } else if(showBow.isEnabled()&&e instanceof ArmorStandEntity) {
                    ArmorStandEntity stand=(ArmorStandEntity) e;
                    if(e.isInvisible()&&!stand.attackable()&&!stand.isSmall()&&stand.hasMarker()&&!stand.hasNoBasePlate()&&stand.getHeldItemMainhand().getItem()==Items.BOW) {
                        GlowManager.addGlow(e,GlowManager.GREEN);
                    }
                }
            }
        }
    }

    private static boolean isSwordItem(Item item) {
        for(Item check:SWORD_SKINS) {
            if(check.equals(item)) return true;
        }
        return false;
    }

}
