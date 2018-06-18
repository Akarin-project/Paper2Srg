package net.minecraft.network.rcon;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RConThreadBase implements Runnable {

    private static final AtomicInteger THREAD_ID = new AtomicInteger(0);
    protected boolean running;
    protected IServer server;
    protected final String threadName;
    protected Thread rconThread;
    protected int maxStopWait = 5;
    protected List<DatagramSocket> socketList = Lists.newArrayList();
    protected List<ServerSocket> serverSocketList = Lists.newArrayList();

    protected RConThreadBase(IServer iminecraftserver, String s) {
        this.server = iminecraftserver;
        this.threadName = s;
        if (this.server.isDebuggingEnabled()) {
            this.logWarning("Debugging is enabled, performance maybe reduced!");
        }

    }

    public synchronized void startThread() {
        this.rconThread = new Thread(this, this.threadName + " #" + RConThreadBase.THREAD_ID.incrementAndGet());
        this.rconThread.start();
        this.running = true;
    }

    public boolean isRunning() {
        return this.running;
    }

    protected void logDebug(String s) {
        this.server.logDebug(s);
    }

    protected void logInfo(String s) {
        this.server.logInfo(s);
    }

    protected void logWarning(String s) {
        this.server.logWarning(s);
    }

    protected void logSevere(String s) {
        this.server.logSevere(s);
    }

    protected int getNumberOfPlayers() {
        return this.server.getCurrentPlayerCount();
    }

    protected void registerSocket(DatagramSocket datagramsocket) {
        this.logDebug("registerSocket: " + datagramsocket);
        this.socketList.add(datagramsocket);
    }

    protected boolean closeSocket(DatagramSocket datagramsocket, boolean flag) {
        this.logDebug("closeSocket: " + datagramsocket);
        if (null == datagramsocket) {
            return false;
        } else {
            boolean flag1 = false;

            if (!datagramsocket.isClosed()) {
                datagramsocket.close();
                flag1 = true;
            }

            if (flag) {
                this.socketList.remove(datagramsocket);
            }

            return flag1;
        }
    }

    protected boolean closeServerSocket(ServerSocket serversocket) {
        return this.closeServerSocket_do(serversocket, true);
    }

    protected boolean closeServerSocket_do(ServerSocket serversocket, boolean flag) {
        this.logDebug("closeSocket: " + serversocket);
        if (null == serversocket) {
            return false;
        } else {
            boolean flag1 = false;

            try {
                if (!serversocket.isClosed()) {
                    serversocket.close();
                    flag1 = true;
                }
            } catch (IOException ioexception) {
                this.logWarning("IO: " + ioexception.getMessage());
            }

            if (flag) {
                this.serverSocketList.remove(serversocket);
            }

            return flag1;
        }
    }

    protected void closeAllSockets() {
        this.closeAllSockets_do(false);
    }

    protected void closeAllSockets_do(boolean flag) {
        int i = 0;
        Iterator iterator = this.socketList.iterator();

        while (iterator.hasNext()) {
            DatagramSocket datagramsocket = (DatagramSocket) iterator.next();

            if (this.closeSocket(datagramsocket, false)) {
                ++i;
            }
        }

        this.socketList.clear();
        iterator = this.serverSocketList.iterator();

        while (iterator.hasNext()) {
            ServerSocket serversocket = (ServerSocket) iterator.next();

            if (this.closeServerSocket_do(serversocket, false)) {
                ++i;
            }
        }

        this.serverSocketList.clear();
        if (flag && 0 < i) {
            this.logWarning("Force closed " + i + " sockets");
        }

    }
}
