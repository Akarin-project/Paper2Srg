package net.minecraft.network.rcon;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import net.minecraft.server.MinecraftServer;

public class RConThreadQuery extends RConThreadBase {

    private long lastAuthCheckTime;
    private int queryPort;
    private final int serverPort;
    private final int maxPlayers;
    private final String serverMotd;
    private final String worldName;
    private DatagramSocket querySocket;
    private final byte[] buffer = new byte[1460];
    private DatagramPacket incomingPacket;
    private final Map<SocketAddress, String> idents;
    private String queryHostname;
    private String serverHostname;
    private final Map<SocketAddress, RConThreadQuery.Auth> queryClients;
    private final long time;
    private final RConOutputStream output;
    private long lastQueryResponseTime;

    public RConThreadQuery(IServer iminecraftserver) {
        super(iminecraftserver, "Query Listener");
        this.queryPort = iminecraftserver.getIntProperty("query.port", 0);
        this.serverHostname = iminecraftserver.getHostname();
        this.serverPort = iminecraftserver.getPort();
        this.serverMotd = iminecraftserver.getMotd();
        this.maxPlayers = iminecraftserver.getMaxPlayers();
        this.worldName = iminecraftserver.getFolderName();
        this.lastQueryResponseTime = 0L;
        this.queryHostname = "0.0.0.0";
        if (!this.serverHostname.isEmpty() && !this.queryHostname.equals(this.serverHostname)) {
            this.queryHostname = this.serverHostname;
        } else {
            this.serverHostname = "0.0.0.0";

            try {
                InetAddress inetaddress = InetAddress.getLocalHost();

                this.queryHostname = inetaddress.getHostAddress();
            } catch (UnknownHostException unknownhostexception) {
                this.logWarning("Unable to determine local host IP, please set server-ip in \'" + iminecraftserver.getSettingsFilename() + "\' : " + unknownhostexception.getMessage());
            }
        }

        if (0 == this.queryPort) {
            this.queryPort = this.serverPort;
            this.logInfo("Setting default query port to " + this.queryPort);
            iminecraftserver.setProperty("query.port", (Object) Integer.valueOf(this.queryPort));
            iminecraftserver.setProperty("debug", (Object) Boolean.valueOf(false));
            iminecraftserver.saveProperties();
        }

        this.idents = Maps.newHashMap();
        this.output = new RConOutputStream(1460);
        this.queryClients = Maps.newHashMap();
        this.time = (new Date()).getTime();
    }

    private void sendResponsePacket(byte[] abyte, DatagramPacket datagrampacket) throws IOException {
        this.querySocket.send(new DatagramPacket(abyte, abyte.length, datagrampacket.getSocketAddress()));
    }

    private boolean parseIncomingPacket(DatagramPacket datagrampacket) throws IOException {
        byte[] abyte = datagrampacket.getData();
        int i = datagrampacket.getLength();
        SocketAddress socketaddress = datagrampacket.getSocketAddress();

        this.logDebug("Packet len " + i + " [" + socketaddress + "]");
        if (3 <= i && -2 == abyte[0] && -3 == abyte[1]) {
            this.logDebug("Packet \'" + RConUtils.getByteAsHexString(abyte[2]) + "\' [" + socketaddress + "]");
            switch (abyte[2]) {
            case 0:
                if (!this.verifyClientAuth(datagrampacket).booleanValue()) {
                    this.logDebug("Invalid challenge [" + socketaddress + "]");
                    return false;
                } else if (15 == i) {
                    this.sendResponsePacket(this.createQueryResponse(datagrampacket), datagrampacket);
                    this.logDebug("Rules [" + socketaddress + "]");
                } else {
                    RConOutputStream remotestatusreply = new RConOutputStream(1460);

                    remotestatusreply.writeInt((int) 0);
                    remotestatusreply.writeByteArray(this.getRequestID(datagrampacket.getSocketAddress()));
                    remotestatusreply.writeString(this.serverMotd);
                    remotestatusreply.writeString("SMP");
                    remotestatusreply.writeString(this.worldName);
                    remotestatusreply.writeString(Integer.toString(this.getNumberOfPlayers()));
                    remotestatusreply.writeString(Integer.toString(this.maxPlayers));
                    remotestatusreply.writeShort((short) this.serverPort);
                    remotestatusreply.writeString(this.queryHostname);
                    this.sendResponsePacket(remotestatusreply.toByteArray(), datagrampacket);
                    this.logDebug("Status [" + socketaddress + "]");
                }

            default:
                return true;

            case 9:
                this.sendAuthChallenge(datagrampacket);
                this.logDebug("Challenge [" + socketaddress + "]");
                return true;
            }
        } else {
            this.logDebug("Invalid packet [" + socketaddress + "]");
            return false;
        }
    }

    private byte[] createQueryResponse(DatagramPacket datagrampacket) throws IOException {
        long i = MinecraftServer.getCurrentTimeMillis();

        if (i < this.lastQueryResponseTime + 5000L) {
            byte[] abyte = this.output.toByteArray();
            byte[] abyte1 = this.getRequestID(datagrampacket.getSocketAddress());

            abyte[1] = abyte1[0];
            abyte[2] = abyte1[1];
            abyte[3] = abyte1[2];
            abyte[4] = abyte1[3];
            return abyte;
        } else {
            this.lastQueryResponseTime = i;
            this.output.reset();
            this.output.writeInt((int) 0);
            this.output.writeByteArray(this.getRequestID(datagrampacket.getSocketAddress()));
            this.output.writeString("splitnum");
            this.output.writeInt((int) 128);
            this.output.writeInt((int) 0);
            this.output.writeString("hostname");
            this.output.writeString(this.serverMotd);
            this.output.writeString("gametype");
            this.output.writeString("SMP");
            this.output.writeString("game_id");
            this.output.writeString("MINECRAFT");
            this.output.writeString("version");
            this.output.writeString(this.server.getMinecraftVersion());
            this.output.writeString("plugins");
            this.output.writeString(this.server.getPlugins());
            this.output.writeString("map");
            this.output.writeString(this.worldName);
            this.output.writeString("numplayers");
            this.output.writeString("" + this.getNumberOfPlayers());
            this.output.writeString("maxplayers");
            this.output.writeString("" + this.maxPlayers);
            this.output.writeString("hostport");
            this.output.writeString("" + this.serverPort);
            this.output.writeString("hostip");
            this.output.writeString(this.queryHostname);
            this.output.writeInt((int) 0);
            this.output.writeInt((int) 1);
            this.output.writeString("player_");
            this.output.writeInt((int) 0);
            String[] astring = this.server.getOnlinePlayerNames();
            String[] astring1 = astring;
            int j = astring.length;

            for (int k = 0; k < j; ++k) {
                String s = astring1[k];

                this.output.writeString(s);
            }

            this.output.writeInt((int) 0);
            return this.output.toByteArray();
        }
    }

    private byte[] getRequestID(SocketAddress socketaddress) {
        return ((RConThreadQuery.Auth) this.queryClients.get(socketaddress)).getRequestId();
    }

    private Boolean verifyClientAuth(DatagramPacket datagrampacket) {
        SocketAddress socketaddress = datagrampacket.getSocketAddress();

        if (!this.queryClients.containsKey(socketaddress)) {
            return Boolean.valueOf(false);
        } else {
            byte[] abyte = datagrampacket.getData();

            return ((RConThreadQuery.Auth) this.queryClients.get(socketaddress)).getRandomChallenge() != RConUtils.getBytesAsBEint(abyte, 7, datagrampacket.getLength()) ? Boolean.valueOf(false) : Boolean.valueOf(true);
        }
    }

    private void sendAuthChallenge(DatagramPacket datagrampacket) throws IOException {
        RConThreadQuery.Auth remotestatuslistener_remotestatuschallenge = new RConThreadQuery.Auth(datagrampacket);

        this.queryClients.put(datagrampacket.getSocketAddress(), remotestatuslistener_remotestatuschallenge);
        this.sendResponsePacket(remotestatuslistener_remotestatuschallenge.getChallengeValue(), datagrampacket);
    }

    private void cleanQueryClientsMap() {
        if (this.running) {
            long i = MinecraftServer.getCurrentTimeMillis();

            if (i >= this.lastAuthCheckTime + 30000L) {
                this.lastAuthCheckTime = i;
                Iterator iterator = this.queryClients.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();

                    if (((RConThreadQuery.Auth) entry.getValue()).hasExpired(i).booleanValue()) {
                        iterator.remove();
                    }
                }

            }
        }
    }

    public void run() {
        this.logInfo("Query running on " + this.serverHostname + ":" + this.queryPort);
        this.lastAuthCheckTime = MinecraftServer.getCurrentTimeMillis();
        this.incomingPacket = new DatagramPacket(this.buffer, this.buffer.length);

        try {
            while (this.running) {
                try {
                    this.querySocket.receive(this.incomingPacket);
                    this.cleanQueryClientsMap();
                    this.parseIncomingPacket(this.incomingPacket);
                } catch (SocketTimeoutException sockettimeoutexception) {
                    this.cleanQueryClientsMap();
                } catch (PortUnreachableException portunreachableexception) {
                    ;
                } catch (IOException ioexception) {
                    this.stopWithException((Exception) ioexception);
                }
            }
        } finally {
            this.closeAllSockets();
        }

    }

    public void startThread() {
        if (!this.running) {
            if (0 < this.queryPort && '\uffff' >= this.queryPort) {
                if (this.initQuerySystem()) {
                    super.startThread();
                }

            } else {
                this.logWarning("Invalid query port " + this.queryPort + " found in \'" + this.server.getSettingsFilename() + "\' (queries disabled)");
            }
        }
    }

    private void stopWithException(Exception exception) {
        if (this.running) {
            this.logWarning("Unexpected exception, buggy JRE? (" + exception + ")");
            if (!this.initQuerySystem()) {
                this.logSevere("Failed to recover from buggy JRE, shutting down!");
                this.running = false;
            }

        }
    }

    private boolean initQuerySystem() {
        try {
            this.querySocket = new DatagramSocket(this.queryPort, InetAddress.getByName(this.serverHostname));
            this.registerSocket(this.querySocket);
            this.querySocket.setSoTimeout(500);
            return true;
        } catch (SocketException socketexception) {
            this.logWarning("Unable to initialise query system on " + this.serverHostname + ":" + this.queryPort + " (Socket): " + socketexception.getMessage());
        } catch (UnknownHostException unknownhostexception) {
            this.logWarning("Unable to initialise query system on " + this.serverHostname + ":" + this.queryPort + " (Unknown Host): " + unknownhostexception.getMessage());
        } catch (Exception exception) {
            this.logWarning("Unable to initialise query system on " + this.serverHostname + ":" + this.queryPort + " (E): " + exception.getMessage());
        }

        return false;
    }

    class Auth {

        private final long timestamp = (new Date()).getTime();
        private final int randomChallenge;
        private final byte[] requestId;
        private final byte[] challengeValue;
        private final String requestIdAsString;

        public Auth(DatagramPacket datagrampacket) {
            byte[] abyte = datagrampacket.getData();

            this.requestId = new byte[4];
            this.requestId[0] = abyte[3];
            this.requestId[1] = abyte[4];
            this.requestId[2] = abyte[5];
            this.requestId[3] = abyte[6];
            this.requestIdAsString = new String(this.requestId, StandardCharsets.UTF_8);
            this.randomChallenge = (new Random()).nextInt(16777216);
            this.challengeValue = String.format("\t%s%d\u0000", new Object[] { this.requestIdAsString, Integer.valueOf(this.randomChallenge)}).getBytes(StandardCharsets.UTF_8);
        }

        public Boolean hasExpired(long i) {
            return Boolean.valueOf(this.timestamp < i);
        }

        public int getRandomChallenge() {
            return this.randomChallenge;
        }

        public byte[] getChallengeValue() {
            return this.challengeValue;
        }

        public byte[] getRequestId() {
            return this.requestId;
        }
    }
}
