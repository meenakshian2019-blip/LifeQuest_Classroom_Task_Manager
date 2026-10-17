
package lifequest.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Style {

    public static boolean darkMode = true;

    public static Color BACKGROUND;
    public static Color CARD;
    public static Color SIDEBAR;
    public static Color TEXT;
    public static Color MUTED;
    public static Color ACCENT;
    public static Color ACCENT_BLUE;
    public static Color SUCCESS;
    public static Color BORDER;

    static {
        setDarkMode(true);
    }

    public static void setDarkMode(boolean dark) {
        darkMode = dark;

        if (dark) {
            BACKGROUND = new Color(8, 12, 29);
            CARD = new Color(16, 24, 49);
            SIDEBAR = new Color(6, 10, 24);
            TEXT = new Color(239, 240, 255);
            MUTED = new Color(157, 169, 207);
            ACCENT = new Color(133, 83, 255);
            ACCENT_BLUE = new Color(65, 139, 255);
            SUCCESS = new Color(66, 222, 190);
            BORDER = new Color(49, 65, 116);
        } else {
            BACKGROUND = new Color(242, 244, 252);
            CARD = Color.WHITE;
            SIDEBAR = new Color(229, 233, 246);
            TEXT = new Color(30, 35, 60);
            MUTED = new Color(100, 110, 140);
            ACCENT = new Color(110, 65, 220);
            ACCENT_BLUE = new Color(45, 110, 220);
            SUCCESS = new Color(0, 150, 120);
            BORDER = new Color(210, 216, 235);
        }
    }

    public static JLabel title(String s) {
        JLabel l = new JLabel(s);
        l.setForeground(TEXT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 25));
        return l;
    }

    public static JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setForeground(TEXT);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return l;
    }

    public static JButton button(String s) {
        JButton b = new JButton(s);
        b.setFocusPainted(false);
        b.setForeground(Color.BLACK);
        b.setBackground(ACCENT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setOpaque(true);
b.setContentAreaFilled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(
            new LineBorder(ACCENT, 1, true),
            new EmptyBorder(9, 15, 9, 15)
        ));
        return b;
    }

    public static JButton secondaryButton(String s) {
        JButton b = new JButton(s);
        b.setFocusPainted(false);
        b.setForeground(Color.BLACK);
        b.setBackground(CARD);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(9, 15, 9, 15)
        ));
        return b;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    public static void text(JTextField f) {
        f.setBackground(CARD);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(
            new LineBorder(BORDER, 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
    }

    /*public static void combo(JComboBox<?> c) {
        c.setBackground(CARD);
        c.setForeground(TEXT);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }*/


    
public static void combo(JComboBox<?> c) {
    c.setBackground(Color.WHITE);
    c.setForeground(new Color(25, 30, 50));
    c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    c.setOpaque(true);

    c.setRenderer(new DefaultListCellRenderer() {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

            setForeground(new Color(25, 30, 50));
            setBackground(Color.WHITE);

            if (isSelected) {
                setBackground(new Color(225, 215, 255));
                setForeground(new Color(25, 30, 50));
            }

            setOpaque(true);
            return this;
        }
    });
}


    public static JScrollPane scroll(Component c) {
        JScrollPane s = new JScrollPane(c);
        s.setBorder(null);
        s.getViewport().setBackground(BACKGROUND);
        s.getVerticalScrollBar().setUnitIncrement(16);
        return s;
    }
}
