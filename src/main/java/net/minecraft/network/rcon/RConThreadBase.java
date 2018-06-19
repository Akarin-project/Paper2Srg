package net.minecraft.network.rcon;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RConThreadBase implements Runnable {

    private static final AtomicInteger field_164004_h = new AtomicInteger(0);
    protected boolean field_72619_a;
    protected IServer field_72617_b;
    protected final String field_164003_c;
    protected Thread field_72618_c;
    protected int field_72615_d = 5;
    protected List<DatagramSocket> field_72616_e = Lists.newArrayList();
    protected List<ServerSocket> field_72614_f = Lists.newArrayList();

    protected RConThreadBase(IServer iminecraftserver, String s) {
        this.field_72617_b = iminecraftserver;
        this.field_164003_c = s;
        if (this.field_72617_b.func_71239_B()) {
            this.func_72606_c("Debugging is enabled, performance maybe reduced!");
        }

    }

    public synchronized void func_72602_a() {
        this.field_72618_c = new Thread(this, this.field_164003_c + " #" + RConThreadBase.field_164004_h.incrementAndGet());
        this.field_72618_c.start();
        this.field_72619_a = true;
    }

    public boolean func_72613_c() {
        return this.field_72619_a;
    }

    protected void func_72607_a(String s) {
        this.field_72617_b.func_71198_k(s);
    }

    protected void func_72609_b(String s) {
        this.field_72617_b.func_71244_g(s);
    }

    protected void func_72606_c(String s) {
        this.field_72617_b.func_71236_h(s);
    }

    protected void func_72610_d(String s) {
        this.field_72617_b.func_71201_j(s);
    }

    protected int func_72603_d() {
        return this.field_72617_b.func_71233_x();
    }

    protected void func_72601_a(DatagramSocket datagramsocket) {
        this.func_72607_a("registerSocket: " + datagramsocket);
        this.field_72616_e.add(datagramsocket);
    }

    protected boolean func_72604_a(DatagramSocket datagramsocket, boolean flag) {
        this.func_72607_a("closeSocket: " + datagramsocket);
        if (null == datagramsocket) {
            return false;
        } else {
            boolean flag1 = false;

            if (!datagramsocket.isClosed()) {
                datagramsocket.close();
                flag1 = true;
            }

            if (flag) {
                this.field_72616_e.remove(datagramsocket);
            }

            return flag1;
        }
    }

    protected boolean func_72608_b(ServerSocket serversocket) {
        return this.func_72605_a(serversocket, true);
    }

    protected boolean func_72605_a(ServerSocket serversocket, boolean flag) {
        this.func_72607_a("closeSocket: " + serversocket);
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
                this.func_72606_c("IO: " + ioexception.getMessage());
            }

            if (flag) {
                this.field_72614_f.remove(serversocket);
            }

            return flag1;
        }
    }

    protected void func_72611_e() {
        this.func_72612_a(false);
    }

    protected void func_72612_a(boolean flag) {
        int i = 0;
        Iterator iterator = this.field_72616_e.iterator();

        while (iterator.hasNext()) {
            DatagramSocket datagramsocket = (DatagramSocket) iterator.next();

            if (this.func_72604_a(datagramsocket, false)) {
                ++i;
            }
        }

        this.field_72616_e.clear();
        iterator = this.field_72614_f.iterator();

        while (iterator.hasNext()) {
            ServerSocket serversocket = (ServerSocket) iterator.next();

            if (this.func_72605_a(serversocket, false)) {
                ++i;
            }
        }

        this.field_72614_f.clear();
        if (flag && 0 < i) {
            this.func_72606_c("Force closed " + i + " sockets");
        }

    }
}
