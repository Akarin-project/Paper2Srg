package net.minecraft.util.text;

import java.util.Iterator;

import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TextComponentScore extends TextComponentBase {

    private final String name;
    private final String objective;
    private String value = "";

    public TextComponentScore(String s, String s1) {
        this.name = s;
        this.objective = s1;
    }

    public String getName() {
        return this.name;
    }

    public String getObjective() {
        return this.objective;
    }

    public void setValue(String s) {
        this.value = s;
    }

    public String getUnformattedComponentText() {
        return this.value;
    }

    public void resolve(ICommandSender icommandlistener) {
        MinecraftServer minecraftserver = icommandlistener.getServer();

        if (minecraftserver != null && minecraftserver.isAnvilFileSet() && StringUtils.isNullOrEmpty(this.value)) {
            Scoreboard scoreboard = minecraftserver.getWorld(0).getScoreboard();
            ScoreObjective scoreboardobjective = scoreboard.getObjective(this.objective);

            if (scoreboard.entityHasObjective(this.name, scoreboardobjective)) {
                Score scoreboardscore = scoreboard.getOrCreateScore(this.name, scoreboardobjective);

                this.setValue(String.format("%d", new Object[] { Integer.valueOf(scoreboardscore.getScorePoints())}));
            } else {
                this.value = "";
            }
        }

    }

    public TextComponentScore createCopy() {
        TextComponentScore chatcomponentscore = new TextComponentScore(this.name, this.objective);

        chatcomponentscore.setValue(this.value);
        chatcomponentscore.setStyle(this.getStyle().createShallowCopy());
        Iterator iterator = this.getSiblings().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatcomponentscore.appendSibling(ichatbasecomponent.createCopy());
        }

        return chatcomponentscore;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentScore)) {
            return false;
        } else {
            TextComponentScore chatcomponentscore = (TextComponentScore) object;

            return this.name.equals(chatcomponentscore.name) && this.objective.equals(chatcomponentscore.objective) && super.equals(object);
        }
    }

    public String toString() {
        return "ScoreComponent{name=\'" + this.name + '\'' + "objective=\'" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

    public ITextComponent createCopy() {
        return this.createCopy();
    }
}
