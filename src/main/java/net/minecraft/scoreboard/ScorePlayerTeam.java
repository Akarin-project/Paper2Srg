package net.minecraft.scoreboard;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

import net.minecraft.util.text.TextFormatting;

public class ScorePlayerTeam extends Team {

    private final Scoreboard scoreboard;
    private final String name;
    private final Set<String> membershipSet = Sets.newHashSet();
    private String displayName;
    private String prefix = "";
    private String suffix = "";
    private boolean allowFriendlyFire = true;
    private boolean canSeeFriendlyInvisibles = true;
    private Team.EnumVisible nameTagVisibility;
    private Team.EnumVisible deathMessageVisibility;
    private TextFormatting color;
    private Team.CollisionRule collisionRule;

    public ScorePlayerTeam(Scoreboard scoreboard, String s) {
        this.nameTagVisibility = Team.EnumVisible.ALWAYS;
        this.deathMessageVisibility = Team.EnumVisible.ALWAYS;
        this.color = TextFormatting.RESET;
        this.collisionRule = Team.CollisionRule.ALWAYS;
        this.scoreboard = scoreboard;
        this.name = s;
        this.displayName = s;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Name cannot be null");
        } else {
            this.displayName = s;
            this.scoreboard.broadcastTeamInfoUpdate(this);
        }
    }

    public Collection<String> getMembershipCollection() {
        return this.membershipSet;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        } else {
            this.prefix = s;
            this.scoreboard.broadcastTeamInfoUpdate(this);
        }
    }

    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String s) {
        this.suffix = s;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public String formatString(String s) {
        return this.getPrefix() + s + this.getSuffix();
    }

    public static String formatPlayerName(@Nullable Team scoreboardteambase, String s) {
        return scoreboardteambase == null ? s : scoreboardteambase.formatString(s);
    }

    public boolean getAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }

    public void setAllowFriendlyFire(boolean flag) {
        this.allowFriendlyFire = flag;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public boolean getSeeFriendlyInvisiblesEnabled() {
        return this.canSeeFriendlyInvisibles;
    }

    public void setSeeFriendlyInvisiblesEnabled(boolean flag) {
        this.canSeeFriendlyInvisibles = flag;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public Team.EnumVisible getNameTagVisibility() {
        return this.nameTagVisibility;
    }

    public Team.EnumVisible getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }

    public void setNameTagVisibility(Team.EnumVisible scoreboardteambase_enumnametagvisibility) {
        this.nameTagVisibility = scoreboardteambase_enumnametagvisibility;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public void setDeathMessageVisibility(Team.EnumVisible scoreboardteambase_enumnametagvisibility) {
        this.deathMessageVisibility = scoreboardteambase_enumnametagvisibility;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public Team.CollisionRule getCollisionRule() {
        return this.collisionRule;
    }

    public void setCollisionRule(Team.CollisionRule scoreboardteambase_enumteampush) {
        this.collisionRule = scoreboardteambase_enumteampush;
        this.scoreboard.broadcastTeamInfoUpdate(this);
    }

    public int getFriendlyFlags() {
        int i = 0;

        if (this.getAllowFriendlyFire()) {
            i |= 1;
        }

        if (this.getSeeFriendlyInvisiblesEnabled()) {
            i |= 2;
        }

        return i;
    }

    public void setColor(TextFormatting enumchatformat) {
        this.color = enumchatformat;
    }

    public TextFormatting getColor() {
        return this.color;
    }
}
