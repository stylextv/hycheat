package de.stylextv.hycheat.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScoreboardUtil {

    private static int SIDEBAR_SLOT= Scoreboard.getObjectiveDisplaySlotNumber("sidebar");

    public static ArrayList<String> getTablist() {
        Collection<NetworkPlayerInfo> list= Minecraft.getInstance().player.connection.getPlayerInfoMap();
        ArrayList<String> names=new ArrayList<>();
        for(NetworkPlayerInfo p:list) {
            if(p.getGameProfile()!=null) {
                names.add(p.getGameProfile().getName());
            }
        }
        return names;
    }
    public static String[] getSidebar() {
        ScoreObjective o=Minecraft.getInstance().world.getScoreboard().getObjectiveInDisplaySlot(SIDEBAR_SLOT);
        if(o!=null) {
            Collection<Score> collection = o.getScoreboard().getSortedScores(o);
            List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>() {
                public boolean apply(@Nullable Score p_apply_1_) {
                    return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
                }
            }));

            if (list.size() > 15) {
                collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
            } else {
                collection = list;
            }
            Object[] array=collection.toArray();
            String[] lines=new String[1+array.length];
            lines[0]=o.getDisplayName().getString();
            int i=1;
            for(int j=collection.size()-1; j>=0; j--) {
                Score score= (Score) array[j];
                ITextComponent component= ScorePlayerTeam.formatMemberName(o.getScoreboard().getPlayersTeam(score.getPlayerName()), new StringTextComponent(score.getPlayerName()));
                String s="";
                boolean skip=false;
                for(char ch:component.getString().toCharArray()) {
                    if((int)ch==167) {
                        skip=true;
                    } else {
                        if(!skip) {
                            s=s+ch;
                        } else skip=false;
                    }
                }
                lines[i]=s;
                i++;
            }
            return lines;
        } else return new String[0];
    }
    public static String getSidebarTitle() {
        ScoreObjective o=Minecraft.getInstance().world.getScoreboard().getObjectiveInDisplaySlot(SIDEBAR_SLOT);
        if(o!=null) return o.getDisplayName().getString();
        return "";
    }

}
