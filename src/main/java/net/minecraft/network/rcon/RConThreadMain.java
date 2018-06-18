package net.minecraft.network.rcon;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RConThreadMain extends RConThreadBase {

    private int rconPort;
    private final int serverPort;
    private String hostname;
    private ServerSocket serverSocket;
    private final String rconPassword;
    private Map<SocketAddress, RConThreadClient> clientThreads;

    public RConThreadMain(IServer iminecraftserver) {
        super(iminecraftserver, "RCON Listener");
        this.rconPort = iminecraftserver.getIntProperty("rcon.port", 0);
        this.rconPassword = iminecraftserver.getStringProperty("rcon.password", "");
        this.hostname = iminecraftserver.getStringProperty("rcon.ip", iminecraftserver.getHostname()); // Paper
        this.serverPort = iminecraftserver.getPort();
        if (0 == this.rconPort) {
            this.rconPort = this.serverPort + 10;
            this.logInfo("Setting default rcon port to " + this.rconPort);
            iminecraftserver.setProperty("rcon.port", (Object) Integer.valueOf(this.rconPort));
            if (this.rconPassword.isEmpty()) {
                iminecraftserver.setProperty("rcon.password", (Object) "");
            }

            iminecraftserver.saveProperties();
        }

        if (this.hostname.isEmpty()) {
            this.hostname = "0.0.0.0";
        }

        this.initClientThreadList();
        this.serverSocket = null;
    }

    private void initClientThreadList() {
        this.clientThreads = Maps.newHashMap();
    }

    private void cleanClientThreadsMap() {
        Iterator iterator = this.clientThreads.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();

            if (!((RConThreadClient) entry.getValue()).isRunning()) {
                iterator.remove();
            }
        }

    }

    public void run() {
        this.logInfo("RCON running on " + this.hostname + ":" + this.rconPort);

        try {
            while (this.running) {
                try {
                    Socket socket = this.serverSocket.accept();

                    socket.setSoTimeout(500);
                    RConThreadClient remotecontrolsession = new RConThreadClient(this.server, socket);

                    remotecontrolsession.startThread();
                    this.clientThreads.put(socket.getRemoteSocketAddress(), remotecontrolsession);
                    this.cleanClientThreadsMap();
                } catch (SocketTimeoutException sockettimeoutexception) {
                    this.cleanClientThreadsMap();
                } catch (IOException ioexception) {
                    if (this.running) {
                        this.logInfo("IO: " + ioexception.getMessage());
                    }
                }
            }
        } finally {
            this.closeServerSocket(this.serverSocket);
        }

    }

    public void startThread() {
        if (this.rconPassword.isEmpty()) {
            this.logWarning("No rcon password set in \'" + this.server.getSettingsFilename() + "\', rcon disabled!");
        } else if (0 < this.rconPort && '\uffff' >= this.rconPort) {
            if (!this.running) {
                try {
                    this.serverSocket = new ServerSocket(this.rconPort, 0, InetAddress.getByName(this.hostname));
                    this.serverSocket.setSoTimeout(500);
                    super.startThread();
                } catch (IOException ioexception) {
                    this.logWarning("Unable to initialise rcon on " + this.hostname + ":" + this.rconPort + " : " + ioexception.getMessage());
                }

            }
        } else {
            this.logWarning("Invalid rcon port " + this.rconPort + " found in \'" + this.server.getSettingsFilename() + "\', rcon disabled!");
        }
    }
}
