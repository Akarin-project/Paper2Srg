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

    private static final Logger LOGGER = LogManager.getLogger();
    private boolean loggedIn;
    private Socket clientSocket;
    private final byte[] buffer = new byte[1460];
    private final String rconPassword;

    RConThreadClient(IServer iminecraftserver, Socket socket) {
        super(iminecraftserver, "RCON Client");
        this.clientSocket = socket;

        try {
            this.clientSocket.setSoTimeout(0);
        } catch (Exception exception) {
            this.running = false;
        }

        this.rconPassword = iminecraftserver.getStringProperty("rcon.password", "");
        this.logInfo("Rcon connection from: " + socket.getInetAddress());
    }

    public void run() {
        while (true) {
            try {
                if (!this.running) {
                    return;
                }

                BufferedInputStream bufferedinputstream = new BufferedInputStream(this.clientSocket.getInputStream());
                int i = bufferedinputstream.read(this.buffer, 0, 1460);

                if (10 > i) {
                    return;
                }

                byte b0 = 0;
                int j = RConUtils.getBytesAsLEInt(this.buffer, 0, i);

                if (j == i - 4) {
                    int k = b0 + 4;
                    int l = RConUtils.getBytesAsLEInt(this.buffer, k, i);

                    k += 4;
                    int i1 = RConUtils.getRemainingBytesAsLEInt(this.buffer, k);

                    k += 4;
                    switch (i1) {
                    case 2:
                        if (this.loggedIn) {
                            String s = RConUtils.getBytesAsString(this.buffer, k, i);

                            try {
                                this.sendMultipacketResponse(l, this.server.handleRConCommand(s));
                            } catch (Exception exception) {
                                this.sendMultipacketResponse(l, "Error executing: " + s + " (" + exception.getMessage() + ")");
                            }
                            continue;
                        }

                        this.sendLoginFailedResponse();
                        continue;

                    case 3:
                        String s1 = RConUtils.getBytesAsString(this.buffer, k, i);
                        int j1 = k + s1.length();

                        if (!s1.isEmpty() && s1.equals(this.rconPassword)) {
                            this.loggedIn = true;
                            this.sendResponse(l, 2, "");
                            continue;
                        }

                        this.loggedIn = false;
                        this.sendLoginFailedResponse();
                        continue;

                    default:
                        this.sendMultipacketResponse(l, String.format("Unknown request %s", new Object[] { Integer.toHexString(i1)}));
                        continue;
                    }
                }
            } catch (SocketTimeoutException sockettimeoutexception) {
                return;
            } catch (IOException ioexception) {
                return;
            } catch (Exception exception1) {
                RConThreadClient.LOGGER.error("Exception whilst parsing RCON input", exception1);
                return;
            } finally {
                this.closeSocket();
            }

            return;
        }
    }

    private void sendResponse(int i, int j, String s) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(1248);
        DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
        byte[] abyte = s.getBytes("UTF-8");

        dataoutputstream.writeInt(Integer.reverseBytes(abyte.length + 10));
        dataoutputstream.writeInt(Integer.reverseBytes(i));
        dataoutputstream.writeInt(Integer.reverseBytes(j));
        dataoutputstream.write(abyte);
        dataoutputstream.write(0);
        dataoutputstream.write(0);
        this.clientSocket.getOutputStream().write(bytearrayoutputstream.toByteArray());
    }

    private void sendLoginFailedResponse() throws IOException {
        this.sendResponse(-1, 2, "");
    }

    private void sendMultipacketResponse(int i, String s) throws IOException {
        int j = s.length();

        do {
            int k = 4096 <= j ? 4096 : j;

            this.sendResponse(i, 0, s.substring(0, k));
            s = s.substring(k);
            j = s.length();
        } while (0 != j);

    }

    private void closeSocket() {
        if (null != this.clientSocket) {
            try {
                this.clientSocket.close();
            } catch (IOException ioexception) {
                this.logWarning("IO: " + ioexception.getMessage());
            }

            this.clientSocket = null;
        }
    }
}
