package net.minecraft.server.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JComponent;
import javax.swing.Timer;

import net.minecraft.server.MinecraftServer;

public class StatsComponent extends JComponent {

    private static final DecimalFormat FORMATTER = new DecimalFormat("########0.000");
    private final int[] values = new int[256];
    private int vp;
    private final String[] msgs = new String[11];
    private final MinecraftServer server;

    public StatsComponent(MinecraftServer minecraftserver) {
        this.server = minecraftserver;
        this.setPreferredSize(new Dimension(456, 246));
        this.setMinimumSize(new Dimension(456, 246));
        this.setMaximumSize(new Dimension(456, 246));
        (new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                StatsComponent.this.tick();
            }
        })).start();
        this.setBackground(Color.BLACK);
    }

    private void tick() {
        long i = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.gc();
        this.msgs[0] = "Memory use: " + i / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
        this.msgs[1] = "Avg tick: " + StatsComponent.FORMATTER.format(this.mean(this.server.tickTimeArray) * 1.0E-6D) + " ms";
        this.values[this.vp++ & 255] = (int) (i * 100L / Runtime.getRuntime().maxMemory());
        this.repaint();
    }

    private double mean(long[] along) {
        long i = 0L;
        long[] along1 = along;
        int j = along.length;

        for (int k = 0; k < j; ++k) {
            long l = along1[k];

            i += l;
        }

        return (double) i / (double) along.length;
    }

    public void paint(Graphics graphics) {
        graphics.setColor(new Color(16777215));
        graphics.fillRect(0, 0, 456, 246);

        int i;

        for (i = 0; i < 256; ++i) {
            int j = this.values[i + this.vp & 255];

            graphics.setColor(new Color(j + 28 << 16));
            graphics.fillRect(i, 100 - j, 1, j);
        }

        graphics.setColor(Color.BLACK);

        for (i = 0; i < this.msgs.length; ++i) {
            String s = this.msgs[i];

            if (s != null) {
                graphics.drawString(s, 32, 116 + i * 16);
            }
        }

    }
}
