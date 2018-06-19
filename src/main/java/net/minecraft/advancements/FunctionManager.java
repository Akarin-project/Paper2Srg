package net.minecraft.advancements;

import com.google.common.collect.Maps;
import com.google.common.io.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.FunctionObject;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class FunctionManager implements ITickable {

    private static final Logger LOGGER = LogManager.getLogger();
    private final File functionDir;
    private final MinecraftServer server;
    private final Map<ResourceLocation, FunctionObject> functions = Maps.newHashMap();
    private String currentGameLoopFunctionId = "-";
    private FunctionObject gameLoopFunction;
    private final ArrayDeque<FunctionManager.a> commandQueue = new ArrayDeque();
    private boolean isExecuting = false;
    // CraftBukkit start
    private final ICommandSender gameLoopFunctionSender = new CustomFunctionListener();

    public class CustomFunctionListener implements ICommandSender {

        protected org.bukkit.command.CommandSender sender = new org.bukkit.craftbukkit.command.CraftFunctionCommandSender(this);
        // CraftBukkit end

        @Override
        public String getName() {
            return FunctionManager.this.currentGameLoopFunctionId;
        }

        @Override
        public boolean canUseCommand(int i, String s) {
            return i <= 2;
        }

        @Override
        public World getEntityWorld() {
            return FunctionManager.this.server.worlds.get(0); // CraftBukkit
        }

        @Override
        public MinecraftServer getServer() {
            return FunctionManager.this.server;
        }
    };

    public FunctionManager(@Nullable File file, MinecraftServer minecraftserver) {
        this.functionDir = file;
        this.server = minecraftserver;
        this.reload();
    }

    @Nullable
    public FunctionObject getFunction(ResourceLocation minecraftkey) {
        return this.functions.get(minecraftkey);
    }

    public ICommandManager getCommandManager() {
        return this.server.getCommandManager();
    }

    public int getMaxCommandChainLength() {
        return this.server.worlds.get(0).getGameRules().getInt("maxCommandChainLength"); // CraftBukkit
    }

    public Map<ResourceLocation, FunctionObject> getFunctions() {
        return this.functions;
    }

    @Override
    public void update() {
        String s = this.server.worlds.get(0).getGameRules().getString("gameLoopFunction"); // CraftBukkit

        if (!s.equals(this.currentGameLoopFunctionId)) {
            this.currentGameLoopFunctionId = s;
            this.gameLoopFunction = this.getFunction(new ResourceLocation(s));
        }

        if (this.gameLoopFunction != null) {
            this.execute(this.gameLoopFunction, this.gameLoopFunctionSender);
        }

    }

    public int execute(FunctionObject customfunction, ICommandSender icommandlistener) {
        int i = this.getMaxCommandChainLength();

        if (this.isExecuting) {
            if (this.commandQueue.size() < i) {
                this.commandQueue.addFirst(new FunctionManager.a(this, icommandlistener, new FunctionObject.d(customfunction)));
            }

            return 0;
        } else {
            int j;

            try {
                this.isExecuting = true;
                int k = 0;
                FunctionObject.c[] acustomfunction_c = customfunction.a();

                for (j = acustomfunction_c.length - 1; j >= 0; --j) {
                    this.commandQueue.push(new FunctionManager.a(this, icommandlistener, acustomfunction_c[j]));
                }

                do {
                    if (this.commandQueue.isEmpty()) {
                        j = k;
                        return j;
                    }

                    this.commandQueue.removeFirst().a(this.commandQueue, i);
                    ++k;
                } while (k < i);

                j = k;
            } finally {
                this.commandQueue.clear();
                this.isExecuting = false;
            }

            return j;
        }
    }

    public void reload() {
        this.functions.clear();
        this.gameLoopFunction = null;
        this.currentGameLoopFunctionId = "-";
        this.loadFunctions();
    }

    private void loadFunctions() {
        if (this.functionDir != null) {
            this.functionDir.mkdirs();
            Iterator iterator = FileUtils.listFiles(this.functionDir, new String[] { "mcfunction"}, true).iterator();

            while (iterator.hasNext()) {
                File file = (File) iterator.next();
                String s = FilenameUtils.removeExtension(this.functionDir.toURI().relativize(file.toURI()).toString());
                String[] astring = s.split("/", 2);

                if (astring.length == 2) {
                    ResourceLocation minecraftkey = new ResourceLocation(astring[0], astring[1]);

                    try {
                        this.functions.put(minecraftkey, FunctionObject.create(this, Files.readLines(file, StandardCharsets.UTF_8)));
                    } catch (Throwable throwable) {
                        FunctionManager.LOGGER.error("Couldn\'t read custom function " + minecraftkey + " from " + file, throwable);
                    }
                }
            }

            if (!this.functions.isEmpty()) {
                FunctionManager.LOGGER.info("Loaded " + this.functions.size() + " custom command functions");
            }

        }
    }

    public static class a {

        private final FunctionManager a;
        private final ICommandSender b;
        private final FunctionObject.c c;

        public a(FunctionManager customfunctiondata, ICommandSender icommandlistener, FunctionObject.c customfunction_c) {
            this.a = customfunctiondata;
            this.b = icommandlistener;
            this.c = customfunction_c;
        }

        public void a(ArrayDeque<FunctionManager.a> arraydeque, int i) {
            this.c.a(this.a, this.b, arraydeque, i);
        }

        @Override
        public String toString() {
            return this.c.toString();
        }
    }
}
