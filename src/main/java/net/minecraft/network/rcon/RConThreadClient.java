package net.minecraft.network.rcon;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RConThreadClient extends RConThreadBase {

    private static final Logger field_164005_h = LogManager.getLogger();
    private boolean field_72657_g;
    private Socket field_72659_h;
    private final byte[] field_72660_i = new byte[1460];
    private final String field_72658_j;

    RConThreadClient(IServer iminecraftserver, Socket socket) {
        super(iminecraftserver, "RCON Client");
        this.field_72659_h = socket;

        try {
            this.field_72659_h.setSoTimeout(0);
        } catch (Exception exception) {
            this.field_72619_a = false;
        }

        this.field_72658_j = iminecraftserver.func_71330_a("rcon.password", "");
        this.func_72609_b("Rcon connection from: " + socket.getInetAddress());
    }

    public void run() {
        while (true) {
            try {
                if (!this.field_72619_a) {
                    return;
                }

                BufferedInputStream bufferedinputstream = new BufferedInputStream(this.field_72659_h.getInputStream());
                int i = bufferedinputstream.read(this.field_72660_i, 0, 1460);

                if (10 > i) {
                    return;
                }

                byte b0 = 0;
                int j = RConUtils.func_72665_b(this.field_72660_i, 0, i);

                if (j == i - 4) {
                    int k = b0 + 4;
                    int l = RConUtils.func_72665_b(this.field_72660_i, k, i);

                    k += 4;
                    int i1 = RConUtils.func_72662_b(this.field_72660_i, k);

                    k += 4;
                    switch (i1) {
                    case 2:
                        if (this.field_72657_g) {
                            String s = RConUtils.func_72661_a(this.field_72660_i, k, i);

                            try {
                                this.func_72655_a(l, this.field_72617_b.func_71252_i(s));
                            } catch (Exception exception) {
                                this.func_72655_a(l, "Error executing: " + s + " (" + exception.getMessage() + ")");
                            }
                            continue;
                        }

                        this.func_72656_f();
                        continue;

                    case 3:
                        String s1 = RConUtils.func_72661_a(this.field_72660_i, k, i);
                        int j1 = k + s1.length();

                        if (!s1.isEmpty() && s1.equals(this.field_72658_j)) {
                            this.field_72657_g = true;
                            this.func_72654_a(l, 2, "");
                            continue;
                        }

                        this.field_72657_g = false;
                        this.func_72656_f();
                        continue;

                    default:
                        this.func_72655_a(l, String.format("Unknown request %s", new Object[] { Integer.toHexString(i1)}));
                        continue;
                    }
                }
            } catch (SocketTimeoutException sockettimeoutexception) {
                return;
            } catch (IOException ioexception) {
                return;
            } catch (Exception exception1) {
                RConThreadClient.field_164005_h.error("Exception whilst parsing RCON input", exception1);
                return;
            } finally {
                this.func_72653_g();
            }

            return;
        }
    }

    private void func_72654_a(int i, int j, String s) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1248);
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
        byte[] abyte = s.getBytes("UTF-8");

        dataoutputstream.writeInt(Integer.reverseBytes(abyte.length + 10));
        dataoutputstream.writeInt(Integer.reverseBytes(i));
        dataoutputstream.writeInt(Integer.reverseBytes(j));
        dataoutputstream.write(abyte);
        dataoutputstream.write(0);
        dataoutputstream.write(0);
        this.field_72659_h.getOutputStream().write(bytearrayoutputstream.toByteArray());
    }

    private void func_72656_f() throws IOException {
        this.func_72654_a(-1, 2, "");
    }

    private void func_72655_a(int i, String s) throws IOException {
        int j = s.length();

        do {
            int k = 4096 <= j ? 4096 : j;

            this.func_72654_a(i, 0, s.substring(0, k));
            s = s.substring(k);
            j = s.length();
        } while (0 != j);

    }

    private void func_72653_g() {
        if (null != this.field_72659_h) {
            try {
                this.field_72659_h.close();
            } catch (IOException ioexception) {
                this.func_72606_c("IO: " + ioexception.getMessage());
            }

            this.field_72659_h = null;
        }
    }
}
