package net.minecraft.util.text;

import java.util.Iterator;

import net.minecraft.command.ICommandSender;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TextComponentScore extends TextComponentBase {

    private final String field_179999_b;
    private final String field_180000_c;
    private String field_179998_d = "";

    public TextComponentScore(String s, String s1) {
        this.field_179999_b = s;
        this.field_180000_c = s1;
    }

    public String func_179995_g() {
        return this.field_179999_b;
    }

    public String func_179994_h() {
        return this.field_180000_c;
    }

    public void func_179997_b(String s) {
        this.field_179998_d = s;
    }

    @Override
    public String func_150261_e() {
        return this.field_179998_d;
    }

    public void func_186876_a(ICommandSender icommandlistener) {
        MinecraftServer minecraftserver = icommandlistener.func_184102_h();

        if (minecraftserver != null && minecraftserver.func_175578_N() && StringUtils.func_151246_b(this.field_179998_d)) {
            Scoreboard scoreboard = minecraftserver.func_71218_a(0).func_96441_U();
            ScoreObjective scoreboardobjective = scoreboard.func_96518_b(this.field_180000_c);

            if (scoreboard.func_178819_b(this.field_179999_b, scoreboardobjective)) {
                Score scoreboardscore = scoreboard.func_96529_a(this.field_179999_b, scoreboardobjective);

                this.func_179997_b(String.format("%d", new Object[] { Integer.valueOf(scoreboardscore.func_96652_c())}));
            } else {
                this.field_179998_d = "";
            }
        }

    }

    @Override
    public TextComponentScore func_150259_f() {
        TextComponentScore chatcomponentscore = new TextComponentScore(this.field_179999_b, this.field_180000_c);

        chatcomponentscore.func_179997_b(this.field_179998_d);
        chatcomponentscore.func_150255_a(this.func_150256_b().func_150232_l());
        Iterator iterator = this.func_150253_a().iterator();

        while (iterator.hasNext()) {
            ITextComponent ichatbasecomponent = (ITextComponent) iterator.next();

            chatcomponentscore.func_150257_a(ichatbasecomponent.func_150259_f());
        }

        return chatcomponentscore;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TextComponentScore)) {
            return false;
        } else {
            TextComponentScore chatcomponentscore = (TextComponentScore) object;

            return this.field_179999_b.equals(chatcomponentscore.field_179999_b) && this.field_180000_c.equals(chatcomponentscore.field_180000_c) && super.equals(object);
        }
    }

    @Override
    public String toString() {
        return "ScoreComponent{name=\'" + this.field_179999_b + '\'' + "objective=\'" + this.field_180000_c + '\'' + ", siblings=" + this.field_150264_a + ", style=" + this.func_150256_b() + '}';
    }
}
