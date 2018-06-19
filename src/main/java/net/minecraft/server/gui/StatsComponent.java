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

    private static final DecimalFormat field_120040_a = new DecimalFormat("########0.000");
    private final int[] field_120038_b = new int[256];
    private int field_120039_c;
    private final String[] field_120036_d = new String[11];
    private final MinecraftServer field_120037_e;

    public StatsComponent(MinecraftServer minecraftserver) {
        this.field_120037_e = minecraftserver;
        this.setPreferredSize(new Dimension(456, 246));
        this.setMinimumSize(new Dimension(456, 246));
        this.setMaximumSize(new Dimension(456, 246));
        (new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                StatsComponent.this.func_120034_a();
            }
        })).start();
        this.setBackground(Color.BLACK);
    }

    private void func_120034_a() {
        long i = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.gc();
        this.field_120036_d[0] = "Memory use: " + i / 1024L / 1024L + " mb (" + Runtime.getRuntime().freeMemory() * 100L / Runtime.getRuntime().maxMemory() + "% free)";
        this.field_120036_d[1] = "Avg tick: " + StatsComponent.field_120040_a.format(this.func_120035_a(this.field_120037_e.field_71311_j) * 1.0E-6D) + " ms";
        this.field_120038_b[this.field_120039_c++ & 255] = (int) (i * 100L / Runtime.getRuntime().maxMemory());
        this.repaint();
    }

    private double func_120035_a(long[] along) {
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
            int j = this.field_120038_b[i + this.field_120039_c & 255];

            graphics.setColor(new Color(j + 28 << 16));
            graphics.fillRect(i, 100 - j, 1, j);
        }

        graphics.setColor(Color.BLACK);

        for (i = 0; i < this.field_120036_d.length; ++i) {
            String s = this.field_120036_d[i];

            if (s != null) {
                graphics.drawString(s, 32, 116 + i * 16);
            }
        }

    }
}
