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

    private long field_72629_g;
    private int field_72636_h;
    private final int field_72637_i;
    private final int field_72634_j;
    private final String field_72635_k;
    private final String field_72632_l;
    private DatagramSocket field_72633_m;
    private final byte[] field_72630_n = new byte[1460];
    private DatagramPacket field_72631_o;
    private final Map<SocketAddress, String> field_72644_p;
    private String field_72643_q;
    private String field_72642_r;
    private final Map<SocketAddress, RConThreadQuery.Auth> field_72641_s;
    private final long field_72640_t;
    private final RConOutputStream field_72639_u;
    private long field_72638_v;

    public RConThreadQuery(IServer iminecraftserver) {
        super(iminecraftserver, "Query Listener");
        this.field_72636_h = iminecraftserver.func_71327_a("query.port", 0);
        this.field_72642_r = iminecraftserver.func_71277_t();
        this.field_72637_i = iminecraftserver.func_71234_u();
        this.field_72635_k = iminecraftserver.func_71274_v();
        this.field_72634_j = iminecraftserver.func_71275_y();
        this.field_72632_l = iminecraftserver.func_71270_I();
        this.field_72638_v = 0L;
        this.field_72643_q = "0.0.0.0";
        if (!this.field_72642_r.isEmpty() && !this.field_72643_q.equals(this.field_72642_r)) {
            this.field_72643_q = this.field_72642_r;
        } else {
            this.field_72642_r = "0.0.0.0";

            try {
                InetAddress inetaddress = InetAddress.getLocalHost();

                this.field_72643_q = inetaddress.getHostAddress();
            } catch (UnknownHostException unknownhostexception) {
                this.func_72606_c("Unable to determine local host IP, please set server-ip in \'" + iminecraftserver.func_71329_c() + "\' : " + unknownhostexception.getMessage());
            }
        }

        if (0 == this.field_72636_h) {
            this.field_72636_h = this.field_72637_i;
            this.func_72609_b("Setting default query port to " + this.field_72636_h);
            iminecraftserver.func_71328_a("query.port", (Object) Integer.valueOf(this.field_72636_h));
            iminecraftserver.func_71328_a("debug", (Object) Boolean.valueOf(false));
            iminecraftserver.func_71326_a();
        }

        this.field_72644_p = Maps.newHashMap();
        this.field_72639_u = new RConOutputStream(1460);
        this.field_72641_s = Maps.newHashMap();
        this.field_72640_t = (new Date()).getTime();
    }

    private void func_72620_a(byte[] abyte, DatagramPacket datagrampacket) throws IOException {
        this.field_72633_m.send(new DatagramPacket(abyte, abyte.length, datagrampacket.getSocketAddress()));
    }

    private boolean func_72621_a(DatagramPacket datagrampacket) throws IOException {
        byte[] abyte = datagrampacket.getData();
        int i = datagrampacket.getLength();
        SocketAddress socketaddress = datagrampacket.getSocketAddress();

        this.func_72607_a("Packet len " + i + " [" + socketaddress + "]");
        if (3 <= i && -2 == abyte[0] && -3 == abyte[1]) {
            this.func_72607_a("Packet \'" + RConUtils.func_72663_a(abyte[2]) + "\' [" + socketaddress + "]");
            switch (abyte[2]) {
            case 0:
                if (!this.func_72627_c(datagrampacket).booleanValue()) {
                    this.func_72607_a("Invalid challenge [" + socketaddress + "]");
                    return false;
                } else if (15 == i) {
                    this.func_72620_a(this.func_72624_b(datagrampacket), datagrampacket);
                    this.func_72607_a("Rules [" + socketaddress + "]");
                } else {
                    RConOutputStream remotestatusreply = new RConOutputStream(1460);

                    remotestatusreply.func_72667_a((int) 0);
                    remotestatusreply.func_72670_a(this.func_72625_a(datagrampacket.getSocketAddress()));
                    remotestatusreply.func_72671_a(this.field_72635_k);
                    remotestatusreply.func_72671_a("SMP");
                    remotestatusreply.func_72671_a(this.field_72632_l);
                    remotestatusreply.func_72671_a(Integer.toString(this.func_72603_d()));
                    remotestatusreply.func_72671_a(Integer.toString(this.field_72634_j));
                    remotestatusreply.func_72668_a((short) this.field_72637_i);
                    remotestatusreply.func_72671_a(this.field_72643_q);
                    this.func_72620_a(remotestatusreply.func_72672_a(), datagrampacket);
                    this.func_72607_a("Status [" + socketaddress + "]");
                }

            default:
                return true;

            case 9:
                this.func_72622_d(datagrampacket);
                this.func_72607_a("Challenge [" + socketaddress + "]");
                return true;
            }
        } else {
            this.func_72607_a("Invalid packet [" + socketaddress + "]");
            return false;
        }
    }

    private byte[] func_72624_b(DatagramPacket datagrampacket) throws IOException {
        long i = MinecraftServer.func_130071_aq();

        if (i < this.field_72638_v + 5000L) {
            byte[] abyte = this.field_72639_u.func_72672_a();
            byte[] abyte1 = this.func_72625_a(datagrampacket.getSocketAddress());

            abyte[1] = abyte1[0];
            abyte[2] = abyte1[1];
            abyte[3] = abyte1[2];
            abyte[4] = abyte1[3];
            return abyte;
        } else {
            this.field_72638_v = i;
            this.field_72639_u.func_72669_b();
            this.field_72639_u.func_72667_a((int) 0);
            this.field_72639_u.func_72670_a(this.func_72625_a(datagrampacket.getSocketAddress()));
            this.field_72639_u.func_72671_a("splitnum");
            this.field_72639_u.func_72667_a((int) 128);
            this.field_72639_u.func_72667_a((int) 0);
            this.field_72639_u.func_72671_a("hostname");
            this.field_72639_u.func_72671_a(this.field_72635_k);
            this.field_72639_u.func_72671_a("gametype");
            this.field_72639_u.func_72671_a("SMP");
            this.field_72639_u.func_72671_a("game_id");
            this.field_72639_u.func_72671_a("MINECRAFT");
            this.field_72639_u.func_72671_a("version");
            this.field_72639_u.func_72671_a(this.field_72617_b.func_71249_w());
            this.field_72639_u.func_72671_a("plugins");
            this.field_72639_u.func_72671_a(this.field_72617_b.func_71258_A());
            this.field_72639_u.func_72671_a("map");
            this.field_72639_u.func_72671_a(this.field_72632_l);
            this.field_72639_u.func_72671_a("numplayers");
            this.field_72639_u.func_72671_a("" + this.func_72603_d());
            this.field_72639_u.func_72671_a("maxplayers");
            this.field_72639_u.func_72671_a("" + this.field_72634_j);
            this.field_72639_u.func_72671_a("hostport");
            this.field_72639_u.func_72671_a("" + this.field_72637_i);
            this.field_72639_u.func_72671_a("hostip");
            this.field_72639_u.func_72671_a(this.field_72643_q);
            this.field_72639_u.func_72667_a((int) 0);
            this.field_72639_u.func_72667_a((int) 1);
            this.field_72639_u.func_72671_a("player_");
            this.field_72639_u.func_72667_a((int) 0);
            String[] astring = this.field_72617_b.func_71213_z();
            String[] astring1 = astring;
            int j = astring.length;

            for (int k = 0; k < j; ++k) {
                String s = astring1[k];

                this.field_72639_u.func_72671_a(s);
            }

            this.field_72639_u.func_72667_a((int) 0);
            return this.field_72639_u.func_72672_a();
        }
    }

    private byte[] func_72625_a(SocketAddress socketaddress) {
        return ((RConThreadQuery.Auth) this.field_72641_s.get(socketaddress)).func_72591_c();
    }

    private Boolean func_72627_c(DatagramPacket datagrampacket) {
        SocketAddress socketaddress = datagrampacket.getSocketAddress();

        if (!this.field_72641_s.containsKey(socketaddress)) {
            return Boolean.valueOf(false);
        } else {
            byte[] abyte = datagrampacket.getData();

            return ((RConThreadQuery.Auth) this.field_72641_s.get(socketaddress)).func_72592_a() != RConUtils.func_72664_c(abyte, 7, datagrampacket.getLength()) ? Boolean.valueOf(false) : Boolean.valueOf(true);
        }
    }

    private void func_72622_d(DatagramPacket datagrampacket) throws IOException {
        RConThreadQuery.Auth remotestatuslistener_remotestatuschallenge = new RConThreadQuery.Auth(datagrampacket);

        this.field_72641_s.put(datagrampacket.getSocketAddress(), remotestatuslistener_remotestatuschallenge);
        this.func_72620_a(remotestatuslistener_remotestatuschallenge.func_72594_b(), datagrampacket);
    }

    private void func_72628_f() {
        if (this.field_72619_a) {
            long i = MinecraftServer.func_130071_aq();

            if (i >= this.field_72629_g + 30000L) {
                this.field_72629_g = i;
                Iterator iterator = this.field_72641_s.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry entry = (Entry) iterator.next();

                    if (((RConThreadQuery.Auth) entry.getValue()).func_72593_a(i).booleanValue()) {
                        iterator.remove();
                    }
                }

            }
        }
    }

    public void run() {
        this.func_72609_b("Query running on " + this.field_72642_r + ":" + this.field_72636_h);
        this.field_72629_g = MinecraftServer.func_130071_aq();
        this.field_72631_o = new DatagramPacket(this.field_72630_n, this.field_72630_n.length);

        try {
            while (this.field_72619_a) {
                try {
                    this.field_72633_m.receive(this.field_72631_o);
                    this.func_72628_f();
                    this.func_72621_a(this.field_72631_o);
                } catch (SocketTimeoutException sockettimeoutexception) {
                    this.func_72628_f();
                } catch (PortUnreachableException portunreachableexception) {
                    ;
                } catch (IOException ioexception) {
                    this.func_72623_a((Exception) ioexception);
                }
            }
        } finally {
            this.func_72611_e();
        }

    }

    public void func_72602_a() {
        if (!this.field_72619_a) {
            if (0 < this.field_72636_h && '\uffff' >= this.field_72636_h) {
                if (this.func_72626_g()) {
                    super.func_72602_a();
                }

            } else {
                this.func_72606_c("Invalid query port " + this.field_72636_h + " found in \'" + this.field_72617_b.func_71329_c() + "\' (queries disabled)");
            }
        }
    }

    private void func_72623_a(Exception exception) {
        if (this.field_72619_a) {
            this.func_72606_c("Unexpected exception, buggy JRE? (" + exception + ")");
            if (!this.func_72626_g()) {
                this.func_72610_d("Failed to recover from buggy JRE, shutting down!");
                this.field_72619_a = false;
            }

        }
    }

    private boolean func_72626_g() {
        try {
            this.field_72633_m = new DatagramSocket(this.field_72636_h, InetAddress.getByName(this.field_72642_r));
            this.func_72601_a(this.field_72633_m);
            this.field_72633_m.setSoTimeout(500);
            return true;
        } catch (SocketException socketexception) {
            this.func_72606_c("Unable to initialise query system on " + this.field_72642_r + ":" + this.field_72636_h + " (Socket): " + socketexception.getMessage());
        } catch (UnknownHostException unknownhostexception) {
            this.func_72606_c("Unable to initialise query system on " + this.field_72642_r + ":" + this.field_72636_h + " (Unknown Host): " + unknownhostexception.getMessage());
        } catch (Exception exception) {
            this.func_72606_c("Unable to initialise query system on " + this.field_72642_r + ":" + this.field_72636_h + " (E): " + exception.getMessage());
        }

        return false;
    }

    class Auth {

        private final long field_72598_b = (new Date()).getTime();
        private final int field_72599_c;
        private final byte[] field_72596_d;
        private final byte[] field_72597_e;
        private final String field_72595_f;

        public Auth(DatagramPacket datagrampacket) {
            byte[] abyte = datagrampacket.getData();

            this.field_72596_d = new byte[4];
            this.field_72596_d[0] = abyte[3];
            this.field_72596_d[1] = abyte[4];
            this.field_72596_d[2] = abyte[5];
            this.field_72596_d[3] = abyte[6];
            this.field_72595_f = new String(this.field_72596_d, StandardCharsets.UTF_8);
            this.field_72599_c = (new Random()).nextInt(16777216);
            this.field_72597_e = String.format("\t%s%d\u0000", new Object[] { this.field_72595_f, Integer.valueOf(this.field_72599_c)}).getBytes(StandardCharsets.UTF_8);
        }

        public Boolean func_72593_a(long i) {
            return Boolean.valueOf(this.field_72598_b < i);
        }

        public int func_72592_a() {
            return this.field_72599_c;
        }

        public byte[] func_72594_b() {
            return this.field_72597_e;
        }

        public byte[] func_72591_c() {
            return this.field_72596_d;
        }
    }
}
