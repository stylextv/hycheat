package de.stylextv.hycheat.world;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.TextFormatting;

import java.util.concurrent.ConcurrentHashMap;

public class GlowManager {

    private static ConcurrentHashMap<Entity, ScorePlayerTeam> glowingEntities=new ConcurrentHashMap<>();

    private static boolean ready = false;
    private static Scoreboard scoreboard;
    public static ScorePlayerTeam RED_DARK;
    public static ScorePlayerTeam GOLD;
    public static ScorePlayerTeam GREEN;
    public static ScorePlayerTeam YELLOW;

    public static void onEnter() {
        if(!ready) {
            ready=true;
            scoreboard = Minecraft.getInstance().world.getScoreboard();

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
    public static void clearUpGlow() {
        for(Entity entity: glowingEntities.keySet()) {
            if(!entity.isAlive()&&!entity.isLiving()) glowingEntities.remove(entity);
        }
    }

    public static void addGlow(Entity entity, ScorePlayerTeam team) {
        if(glowingEntities.get(entity)==null) {
            glowingEntities.put(entity,team);
            entity.setGlowing(true);
            scoreboard.addPlayerToTeam(entity.getScoreboardName(), team);
        }
    }
    public static void removeGlow(Entity entity) {
        ScorePlayerTeam team=glowingEntities.get(entity);
        if(team!=null) {
            if(entity.getTeam()==team) scoreboard.removePlayerFromTeam(entity.getScoreboardName(), team);
            glowingEntities.remove(entity);
            entity.setGlowing(false);
        }
    }
    public static void removeAllGlow() {
        for(Entity entity: glowingEntities.keySet()) {
            ScorePlayerTeam team=glowingEntities.get(entity);
            if(entity.getTeam()==team) scoreboard.removePlayerFromTeam(entity.getScoreboardName(), team);
            entity.setGlowing(false);
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
