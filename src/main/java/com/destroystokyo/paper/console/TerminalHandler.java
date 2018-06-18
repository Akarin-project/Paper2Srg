package com.destroystokyo.paper.console;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecrell.terminalconsole.TerminalConsoleAppender;

import org.bukkit.craftbukkit.command.ConsoleCommandCompleter;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;

public class TerminalHandler {

    private TerminalHandler() {
    }

    public static boolean handleCommands(DedicatedServer server) {
        final Terminal terminal = TerminalConsoleAppender.getTerminal();
        if (terminal == null) {
            return false;
        }

        LineReader reader = LineReaderBuilder.builder()
                .appName("Paper")
                .terminal(terminal)
                .completer(new ConsoleCommandCompleter(server))
                .build();
        reader.unsetOpt(LineReader.Option.INSERT_TAB);

        TerminalConsoleAppender.setReader(reader);

        try {
            String line;
            while (!server.isServerStopped() && server.isServerRunning()) {
                try {
                    line = reader.readLine("> ");
                } catch (EndOfFileException ignored) {
                    // Continue reading after EOT
                    continue;
                }

                if (line == null) {
                    break;
                }

                line = line.trim();
                if (!line.isEmpty()) {
                    server.addPendingCommand(line, server);
                }
            }
        } catch (UserInterruptException e) {
            server.initiateShutdown();
        } finally {
            TerminalConsoleAppender.setReader(null);
        }

        return true;
    }

}
