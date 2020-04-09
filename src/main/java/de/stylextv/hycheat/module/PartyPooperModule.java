package de.stylextv.hycheat.module;

import de.stylextv.hycheat.util.ScoreboardUtil;
import de.stylextv.hycheat.world.GlowManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PartyPooperModule extends Module {

    private static String[] MUSIC_SOUNDS=new String[] {
            "block.note_block.snare",
            "block.note_block.bass",
            "block.note_block.xylophone",
            "block.note_block.basedrum"
    };
    private static int SCORE_MAX=170;
    private static int SCORE_GLOW=150;
    private static int AFK_GLOW=20*50;

    private boolean hasRole;
    private boolean isSeeker;
    private String feedbackString;
    private int feedbackColor=0;

    private HashMap<AbstractClientPlayerEntity,Double> npcScores=new HashMap<>();
    private HashMap<AbstractClientPlayerEntity,Integer> npcAfkScores=new HashMap<>();
    private HashMap<AbstractClientPlayerEntity,Integer> glowTimers=new HashMap<>();

    private HashMap<AbstractClientPlayerEntity,String> npcSkinTypes=new HashMap<>();
    private HashMap<AbstractClientPlayerEntity,ResourceLocation> npcSkins=new HashMap<>();

    private ModuleSetting showHiders;
    private ModuleSetting showSeekers;
    private ModuleSetting showObjectives;
    private ModuleSetting disableNoteblockMusic;
    private ModuleSetting disableNpcAttack;
    private ModuleSetting fixHeartBug;

    public PartyPooperModule() {
        super(
                "party_pooper", "Party Pooper", Items.MUSIC_DISC_BLOCKS,
                "This module helps you by marking players and objectives in the party pooper gamemode."
        );
        setSettings(new ModuleSetting[] {
                showHiders=new ModuleSetting("show_hiders","Show Hiders",Items.COMPASS,"Shows the current position of hiders as seeker."),
                showSeekers=new ModuleSetting("show_seekers","Show Seekers",Items.IRON_SWORD,"Shows the positions of seekers as hider."),
                showObjectives=new ModuleSetting("show_obj","Show Objectives",Items.FIREWORK_ROCKET,"Shows the positions of objectives as hider."),
                new ModuleSetting("use_ai","Use NPC-AI",Items.REDSTONE_TORCH,"Uses an ai to control your npc as hider.", false, true),
                disableNoteblockMusic=new ModuleSetting("disable_music","Disable Noteblock-Music",Items.JUKEBOX,"Disables the custom noteblock music in this gamemode."),
                disableNpcAttack=new ModuleSetting("disable_npc_atk","Disable NPC-Attacks",Items.SKELETON_SKULL,"Prevents you from hitting non-player NPCs as seeker."),
                fixHeartBug=new ModuleSetting("fix_hrtbug","Fix Heart-Bug",Items.APPLE,"Resolves the issue that reduced hearts will not reset in 1.15 (Hypixel Bug).")
        });
    }

    @Override
    public void onEnable() {
        npcScores.clear();
        npcAfkScores.clear();
        glowTimers.clear();

        npcSkinTypes.clear();
        npcSkins.clear();

        feedbackString="";
        feedbackColor=0;
    }
    @Override
    public void onDisable() {
        if(fixHeartBug.isEnabled()) {
            Minecraft.getInstance().player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20);
        }
        GlowManager.removeAllGlow();
        isSeeker=false;
        hasRole=false;
    }
    @Override
    public boolean shouldBeActive(String sidebarTitle, String[] sidebar, String title) {
        if(sidebar.length==15&&sidebar[sidebar.length-3].endsWith(" Party Pooper")) {
            NonNullList<ItemStack> inv=Minecraft.getInstance().player.inventory.mainInventory;
            if(!inv.get(8).getItem().equals(Items.RED_BED)) {
                if(!hasRole) {
                    hasRole=true;
                    isSeeker=inv.get(0).getItem().equals(Items.IRON_SWORD);
                }
                return true;
            }
        }
        return false;
    }
    @Override
    public void onTick() {
        if(!isSeeker) {

            for(Entity e:Minecraft.getInstance().world.getAllEntities()) {
                if(!GlowManager.isGlowing(e)) {

                    if(showSeekers.isEnabled()&&e instanceof AbstractClientPlayerEntity&&!e.isInvisible()&&!e.isInvisibleToPlayer(Minecraft.getInstance().player)) {
                        if(e.getTeam()!=null&&e.getTeam().getColor()==TextFormatting.RED) GlowManager.addGlow(e,GlowManager.RED);
                        else if(GlowManager.isGlowing(e)) GlowManager.removeGlow(e);
                    } else if(showObjectives.isEnabled()&&e instanceof ArmorStandEntity) {
                        ArmorStandEntity stand=(ArmorStandEntity)e;
                        if(stand.getCustomName()!=null&&stand.getCustomName().getSiblings().size()!=0&&stand.getCustomName().getSiblings().get(0).getStyle().getColor()==TextFormatting.GREEN) {
                            GlowManager.addGlow(e,GlowManager.GREEN);
                        }
                    }

                }
            }

        } else if(showHiders.isEnabled()) {

            ArrayList<String> names=ScoreboardUtil.getTablist();
            for(AbstractClientPlayerEntity playerEntity:Minecraft.getInstance().world.getPlayers()) {
                if(playerEntity!=Minecraft.getInstance().player&&!playerEntity.isInvisible()&&!playerEntity.isInvisibleToPlayer(Minecraft.getInstance().player)&&!names.contains(playerEntity.getName().getString())) {

                    if(skinHasChanged(playerEntity)) {
                        GlowManager.removeGlow(playerEntity);
                        playerEntity.setGlowing(false);
                    } else {
                        if(updateNpcScore(playerEntity)) {
                            glowTimers.put(playerEntity, 20*40);
                            if(!GlowManager.isGlowing(playerEntity)) {
                                GlowManager.addGlow(playerEntity,GlowManager.RED);

                                Vec3d playerPos=Minecraft.getInstance().player.getPositionVector();
                                Vec3d targetPos=playerEntity.getPositionVector();
                                Vec3d loc=playerPos.add(targetPos.subtract(playerPos).normalize().mul(3,3,3));
                                Minecraft.getInstance().world.playSound(loc.getX(),loc.getY(),loc.getZ(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS,1,1,false);
                            }
                        } else {
                            Integer got=glowTimers.get(playerEntity);
                            if(got!=null) {
                                got--;
                                if(got<=0) glowTimers.remove(playerEntity);
                                else glowTimers.put(playerEntity,got);
                            } else if(GlowManager.isGlowing(playerEntity)) GlowManager.removeGlow(playerEntity);
                        }
                    }
                }
            }
            refreshFeedback();

        }
    }
    private void refreshFeedback() {
        double max=0;
        for(AbstractClientPlayerEntity playerEntity:npcScores.keySet()) {
            if(!GlowManager.isGlowing(playerEntity)) {
                double score=npcScores.get(playerEntity)/SCORE_GLOW;
                if(score<=1&&score>max) max=score;
            }
        }
        for(AbstractClientPlayerEntity playerEntity:npcAfkScores.keySet()) {
            if(!GlowManager.isGlowing(playerEntity)) {
                double score=(double)npcAfkScores.get(playerEntity)/AFK_GLOW;
                if(score<=1&&score>max) max=score;
            }
        }
        if(max>1) max=1;

        int bars=(int)(max*20);
        String bar;
        if(bars!=0) bar=""+TextFormatting.RESET+TextFormatting.BOLD;
        else bar=""+TextFormatting.DARK_GRAY+TextFormatting.BOLD;
        for(int i=0; i<20; i++) {
            bar=bar+"|";
            if(i==bars-1) bar=bar+TextFormatting.DARK_GRAY+TextFormatting.BOLD;
        }
        feedbackString=TextFormatting.GRAY+"Analyzing player movement... ["+bar+TextFormatting.GRAY+"] "+TextFormatting.RESET+(int)(max*100)+TextFormatting.GRAY+"%";
        feedbackColor=Color.HSBtoRGB((float)max/3f,1,1);
    }
    private boolean updateNpcScore(AbstractClientPlayerEntity playerEntity) {
        double score=0;
        Double got=npcScores.get(playerEntity);
        if(got!=null) score=got;

        if(playerEntity.rotationYaw!=playerEntity.prevRotationYaw||playerEntity.rotationPitch!=playerEntity.prevRotationPitch) {
            Integer afkScore=npcAfkScores.get(playerEntity);
            if(afkScore!=null) npcAfkScores.remove(playerEntity);

            if(playerEntity.rotationYaw!=playerEntity.prevRotationYaw&&playerEntity.rotationPitch!=playerEntity.prevRotationPitch) {
                score+=0.85;
            } else {
                score-=0.5;
                if(score<0) score=0;
            }
        } else {
            score-=0.14;
            if(score<0) score=0;
            Integer afkScore=npcAfkScores.get(playerEntity);
            if(afkScore==null) npcAfkScores.put(playerEntity,1);
            else {
                afkScore++;
                if(afkScore>AFK_GLOW+10) {
                    afkScore=AFK_GLOW+10;
                }
                npcAfkScores.put(playerEntity,afkScore);
                if(afkScore>AFK_GLOW) score=SCORE_MAX;
            }
        }
        if(playerEntity.getHeldItemMainhand().getItem()!=Items.AIR) score=SCORE_MAX;

        if(score>SCORE_MAX) score=SCORE_MAX;

        npcScores.put(playerEntity,score);
        return score>SCORE_GLOW;
    }
    private boolean skinHasChanged(AbstractClientPlayerEntity playerEntity) {
        String type=playerEntity.getSkinType();
        ResourceLocation skin=playerEntity.getLocationSkin();
        String typeGot=npcSkinTypes.get(playerEntity);
        ResourceLocation skinGot=npcSkins.get(playerEntity);
        npcSkinTypes.put(playerEntity,type);
        npcSkins.put(playerEntity,skin);
        if(typeGot!=null&&skinGot!=null) {
            return typeGot!=type || skinGot!=skin;
        }
        return false;
    }

    @Override
    public void onPlaySound(PlaySoundEvent event) {
        if(disableNoteblockMusic.isEnabled()&&isMusicSound(event.getName())) {
            event.setResultSound(null);
        }
    }
    @Override
    public void onUserClick(InputEvent.ClickInputEvent event) {
        if(disableNpcAttack.isEnabled()&&isSeeker&&event.isAttack()) {
            if(Minecraft.getInstance().objectMouseOver.getType()== RayTraceResult.Type.ENTITY) {
                EntityRayTraceResult result=(EntityRayTraceResult) Minecraft.getInstance().objectMouseOver;
                if(!GlowManager.isGlowing(result.getEntity())) {
                    event.setCanceled(true);
                    Minecraft.getInstance().player.playSound(SoundEvents.UI_BUTTON_CLICK,1,2);
                }
            }
        }
    }
    @Override
    public void onOverlayRender() {
        if(isSeeker&&showHiders.isEnabled()) {
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(feedbackString, 10, 10, feedbackColor);
        }
    }

    private static boolean isMusicSound(String name) {
        for(String check:MUSIC_SOUNDS) {
            if(check.equals(name)) return true;
        }
        return false;
    }

}
