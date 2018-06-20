package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.advancements.FunctionManager;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.ResourceLocation;

public class FunctionObject {

    private final FunctionObject.c[] field_193530_b;

    public FunctionObject(FunctionObject.c[] acustomfunction_c) {
        this.field_193530_b = acustomfunction_c;
    }

    public FunctionObject.c[] a() {
        return this.field_193530_b;
    }

    public static FunctionObject func_193527_a(FunctionManager customfunctiondata, List<String> list) {
        ArrayList arraylist = Lists.newArrayListWithCapacity(list.size());
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            s = s.trim();
            if (!s.startsWith("#") && !s.isEmpty()) {
                String[] astring = s.split(" ", 2);
                String s1 = astring[0];

                if (!customfunctiondata.func_193062_a().func_71555_a().containsKey(s1)) {
                    if (s1.startsWith("//")) {
                        throw new IllegalArgumentException("Unknown or invalid command \'" + s1 + "\' (if you intended to make a comment, use \'#\' not \'//\')");
                    }

                    if (s1.startsWith("/") && s1.length() > 1) {
                        throw new IllegalArgumentException("Unknown or invalid command \'" + s1 + "\' (did you mean \'" + s1.substring(1) + "\'? Do not use a preceding forwards slash.)");
                    }

                    throw new IllegalArgumentException("Unknown or invalid command \'" + s1 + "\'");
                }

                arraylist.add(new FunctionObject.b(s));
            }
        }

        return new FunctionObject((FunctionObject.c[]) arraylist.toArray(new FunctionObject.c[arraylist.size()]));
    }

    public static class a {

        public static final FunctionObject.a a = new FunctionObject.a((ResourceLocation) null);
        @Nullable
        private final ResourceLocation b;
        private boolean c;
        private FunctionObject d;

        public a(@Nullable ResourceLocation minecraftkey) {
            this.b = minecraftkey;
        }

        public a(FunctionObject customfunction) {
            this.b = null;
            this.d = customfunction;
        }

        @Nullable
        public FunctionObject a(FunctionManager customfunctiondata) {
            if (!this.c) {
                if (this.b != null) {
                    this.d = customfunctiondata.func_193058_a(this.b);
                }

                this.c = true;
            }

            return this.d;
        }

        @Override
        public String toString() {
            return String.valueOf(this.b);
        }
    }

    public static class d implements FunctionObject.c {

        private final FunctionObject.a a;

        public d(FunctionObject customfunction) {
            this.a = new FunctionObject.a(customfunction);
        }

        @Override
        public void a(FunctionManager customfunctiondata, ICommandSender icommandlistener, ArrayDeque<FunctionManager.a> arraydeque, int i) {
            FunctionObject customfunction = this.a.a(customfunctiondata);

            if (customfunction != null) {
                FunctionObject.c[] acustomfunction_c = customfunction.a();
                int j = i - arraydeque.size();
                int k = Math.min(acustomfunction_c.length, j);

                for (int l = k - 1; l >= 0; --l) {
                    arraydeque.addFirst(new FunctionManager.a(customfunctiondata, icommandlistener, acustomfunction_c[l]));
                }
            }

        }

        @Override
        public String toString() {
            return "/function " + this.a;
        }
    }

    public static class b implements FunctionObject.c {

        private final String a;

        public b(String s) {
            this.a = s;
        }

        @Override
        public void a(FunctionManager customfunctiondata, ICommandSender icommandlistener, ArrayDeque<FunctionManager.a> arraydeque, int i) {
            // CraftBukkit start
            org.bukkit.command.CommandSender sender;
            if (icommandlistener instanceof FunctionManager.CustomFunctionListener) {
                sender = ((FunctionManager.CustomFunctionListener) icommandlistener).sender;
            } else {
                sender = CommandBlockBaseLogic.unwrapSender(icommandlistener);
            }
            CommandBlockBaseLogic.executeSafely(icommandlistener, sender, this.a);
            // CraftBukkit end
        }

        @Override
        public String toString() {
            return "/" + this.a;
        }
    }

    public interface c {

        void a(FunctionManager customfunctiondata, ICommandSender icommandlistener, ArrayDeque<FunctionManager.a> arraydeque, int i);
    }
}
