package de.stylextv.hycheat.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.TextFormatting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GlowManager {

    private static ConcurrentHashMap<Entity, ScorePlayerTeam> glowingEntities=new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Entity, ScorePlayerTeam> oldTeams=new ConcurrentHashMap<>();
    private static CopyOnWriteArrayList<Entity> removeGlowList=new CopyOnWriteArrayList<>();

    private static Scoreboard scoreboard;
    public static ScorePlayerTeam RED;
    public static ScorePlayerTeam RED_DARK;
    public static ScorePlayerTeam GOLD;
    public static ScorePlayerTeam GREEN;
    public static ScorePlayerTeam YELLOW;

    public static void onEnter() {
        scoreboard = Minecraft.getInstance().world.getScoreboard();
        if(scoreboard.getTeam("dark red")==null) {
            RED = scoreboard.createTeam("red");
            RED.setColor(TextFormatting.RED);
            RED_DARK = scoreboard.createTeam("dark red");
            RED_DARK.setColor(TextFormatting.DARK_RED);
            GOLD = scoreboard.createTeam("gold");
            GOLD.setColor(TextFormatting.GOLD);
            GREEN = scoreboard.createTeam("green");
            GREEN.setColor(TextFormatting.GREEN);
            YELLOW = scoreboard.createTeam("yellow");
            YELLOW.setColor(TextFormatting.YELLOW);
        }

        glowingEntities.clear();
    }

    public static void refreshGlow(Entity e) {
        if(isGlowing(e)) {
            e.setGlowing(true);
        }
    }
    public static void refreshGlowStates() {
        if(removeGlowList.size()!=0) {
            for(Entity e: removeGlowList) {
                e.setGlowing(false);
            }
            removeGlowList.clear();
        }
    }
    public static void clearUpGlow() {
        for(Entity entity: glowingEntities.keySet()) {
            if(!entity.isAlive()&&!entity.isLiving()) glowingEntities.remove(entity);
        }
    }

    public static void addGlow(Entity entity, ScorePlayerTeam team) {
        if(glowingEntities.get(entity)==null) {
            glowingEntities.put(entity,team);
            entity.setGlowing(true);
            Team old=entity.getTeam();
            if(old!=null&&old instanceof ScorePlayerTeam) oldTeams.put(entity, (ScorePlayerTeam) old);
            scoreboard.addPlayerToTeam(entity.getScoreboardName(), team);
        }
    }
    public static void removeGlow(Entity entity) {
        ScorePlayerTeam team=glowingEntities.get(entity);
        if(team!=null) {
            if(entity.getTeam()==team) scoreboard.removePlayerFromTeam(entity.getScoreboardName(), team);
            glowingEntities.remove(entity);
            entity.setGlowing(false);
            ScorePlayerTeam oldTeam=oldTeams.get(entity);
            if(oldTeam!=null) scoreboard.addPlayerToTeam(entity.getScoreboardName(),oldTeam);
            removeGlowList.add(entity);
        }
    }
    public static void removeAllGlow() {
        for(Entity entity: glowingEntities.keySet()) {
            ScorePlayerTeam team=glowingEntities.get(entity);
            if(entity.getTeam()==team) scoreboard.removePlayerFromTeam(entity.getScoreboardName(), team);
            entity.setGlowing(false);
            ScorePlayerTeam oldTeam=oldTeams.get(entity);
            if(oldTeam!=null) scoreboard.addPlayerToTeam(entity.getScoreboardName(),oldTeam);
            removeGlowList.add(entity);
        }
        glowingEntities.clear();
    }
    public static boolean isGlowing(Entity entity) {
        return glowingEntities.get(entity)!=null;
    }
    public static ScorePlayerTeam getGlowColor(Entity entity) {
        return glowingEntities.get(entity);
    }

}
