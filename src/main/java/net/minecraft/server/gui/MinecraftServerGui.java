package net.minecraft.server.gui;

import com.mojang.util.QueueLogAppender;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.dedicated.DedicatedServer;

public class MinecraftServerGui extends JComponent {

    private static final Font SERVER_GUI_FONT = new Font("Monospaced", 0, 12);
    private static final Logger LOGGER = LogManager.getLogger();
    private final DedicatedServer server;

    public static void createServerGui(final DedicatedServer dedicatedserver) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
            ;
        }

        MinecraftServerGui servergui = new MinecraftServerGui(dedicatedserver);
        JFrame jframe = new JFrame("Minecraft server");

        jframe.add(servergui);
        jframe.pack();
        jframe.setLocationRelativeTo((Component) null);
        jframe.setVisible(true);
        jframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowevent) {
                dedicatedserver.initiateShutdown();

                while (!dedicatedserver.isServerStopped()) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException interruptedexception) {
                        interruptedexception.printStackTrace();
                    }
                }

                System.exit(0);
            }
        });
    }

    public MinecraftServerGui(DedicatedServer dedicatedserver) {
        this.server = dedicatedserver;
        this.setPreferredSize(new Dimension(854, 480));
        this.setLayout(new BorderLayout());

        try {
            this.add(this.getLogComponent(), "Center");
            this.add(this.getStatsComponent(), "West");
        } catch (Exception exception) {
            MinecraftServerGui.LOGGER.error("Couldn\'t build server GUI", exception);
        }

    }

    private JComponent getStatsComponent() throws Exception {
        JPanel jpanel = new JPanel(new BorderLayout());

        jpanel.add(new StatsComponent(this.server), "North");
        jpanel.add(this.getPlayerListComponent(), "Center");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Stats"));
        return jpanel;
    }

    private JComponent getPlayerListComponent() throws Exception {
        PlayerListComponent playerlistbox = new PlayerListComponent(this.server);
        JScrollPane jscrollpane = new JScrollPane(playerlistbox, 22, 30);

        jscrollpane.setBorder(new TitledBorder(new EtchedBorder(), "Players"));
        return jscrollpane;
    }

    private JComponent getLogComponent() throws Exception {
        JPanel jpanel = new JPanel(new BorderLayout());
        final JTextArea jtextarea = new JTextArea();
        final JScrollPane jscrollpane = new JScrollPane(jtextarea, 22, 30);

        jtextarea.setEditable(false);
        jtextarea.setFont(MinecraftServerGui.SERVER_GUI_FONT);
        final JTextField jtextfield = new JTextField();

        jtextfield.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionevent) {
                String s = jtextfield.getText().trim();

                if (!s.isEmpty()) {
                    MinecraftServerGui.this.server.addPendingCommand(s, MinecraftServerGui.this.server);
                }

                jtextfield.setText("");
            }
        });
        jtextarea.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent focusevent) {}
        });
        jpanel.add(jscrollpane, "Center");
        jpanel.add(jtextfield, "South");
        jpanel.setBorder(new TitledBorder(new EtchedBorder(), "Log and chat"));
        Thread thread = new Thread(new Runnable() {
            public void run() {
                String s;

                while ((s = QueueLogAppender.getNextLogEvent("ServerGuiConsole")) != null) {
                    MinecraftServerGui.this.appendLine(jtextarea, jscrollpane, s);
                }

            }
        });

        thread.setDaemon(true);
        thread.start();
        return jpanel;
    }

    public void appendLine(final JTextArea jtextarea, final JScrollPane jscrollpane, final String s) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    MinecraftServerGui.this.appendLine(jtextarea, jscrollpane, s);
                }
            });
        } else {
            Document document = jtextarea.getDocument();
            JScrollBar jscrollbar = jscrollpane.getVerticalScrollBar();
            boolean flag = false;

            if (jscrollpane.getViewport().getView() == jtextarea) {
                flag = (double) jscrollbar.getValue() + jscrollbar.getSize().getHeight() + (double) (MinecraftServerGui.SERVER_GUI_FONT.getSize() * 4) > (double) jscrollbar.getMaximum();
            }

            try {
                document.insertString(document.getLength(), s, (AttributeSet) null);
            } catch (BadLocationException badlocationexception) {
                ;
            }

            if (flag) {
                jscrollbar.setValue(Integer.MAX_VALUE);
            }

        }
    }
}
