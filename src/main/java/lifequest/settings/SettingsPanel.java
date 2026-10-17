
package lifequest.settings;

import lifequest.Main;
import lifequest.database.UserDAO;
import lifequest.model.User;
import lifequest.ui.Style;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final Main main;
    private final User user;
    private final JCheckBox dark;
    private final JCheckBox notifications;

    public SettingsPanel(Main main, User user) {
        this.main = main;
        this.user = user;

        setBackground(Style.BACKGROUND);
        setLayout(new BorderLayout(16, 16));
        setBorder(new EmptyBorder(28, 30, 28, 30));

        JPanel head = new JPanel();
        head.setOpaque(false);
        head.setLayout(new BoxLayout(head, BoxLayout.Y_AXIS));

        head.add(Style.title("Settings"));

        JLabel sub = Style.label("Make LifeQuest feel right for you.");
        sub.setForeground(Style.MUTED);

        head.add(Box.createVerticalStrut(5));
        head.add(sub);
        add(head, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel account = section("ACCOUNT");
        account.add(settingRow("Username", user.getUsername()));
        account.add(settingRow(
            "Email",
            user.getEmail() == null ? "Not set" : user.getEmail()
        ));

        content.add(account);
        content.add(Box.createVerticalStrut(14));

        JPanel appearance = section("APPEARANCE & NOTIFICATIONS");

        dark = check("Dark mode", Style.darkMode);
        notifications = check(
            "Enable notifications",
            user.isNotifications()
        );

        appearance.add(checkRow(
            "Dark mode",
            "Use the LifeQuest midnight-purple theme",
            dark
        ));

        appearance.add(Box.createVerticalStrut(10));

        appearance.add(checkRow(
            "Notifications",
            "Show helpful quest reminders",
            notifications
        ));

        content.add(appearance);
        content.add(Box.createVerticalStrut(14));

        JPanel actions = section("ACCOUNT ACTIONS");

        JButton save = Style.button("Save settings  ✓");
        JButton reset = Style.secondaryButton("Reset progress");
        JButton logout = Style.secondaryButton("Log out");

        save.setAlignmentX(LEFT_ALIGNMENT);
        reset.setAlignmentX(LEFT_ALIGNMENT);
        logout.setAlignmentX(LEFT_ALIGNMENT);

        actions.add(save);
        actions.add(Box.createVerticalStrut(10));
        actions.add(reset);
        actions.add(Box.createVerticalStrut(10));
        actions.add(logout);

        content.add(actions);
        add(Style.scroll(content), BorderLayout.CENTER);

        // Apply the selected theme immediately.
        dark.addActionListener(e -> {
            main.applyTheme(dark.isSelected());
        });

        // Save preferences to MySQL.
        save.addActionListener(e -> {
            boolean selectedDark = dark.isSelected();
            boolean selectedNotifications = notifications.isSelected();

            if (new UserDAO().updateSettings(
                    user.getId(),
                    selectedDark,
                    selectedNotifications)) {

                user.setDarkMode(selectedDark);
                user.setNotifications(selectedNotifications);

                JOptionPane.showMessageDialog(
                    this,
                    "Settings saved successfully!"
                );

            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Could not save settings. Check the database connection."
                );
            }
        });

        // Reset progress.
        reset.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(
                this,
                "Reset your level, XP, coins and quests? This cannot be undone.",
                "Reset progress",
                JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                if (new UserDAO().resetProgress(user.getId())) {
                    user.setLevel(1);
                    user.setXp(0);
                    user.setCoins(0);
                    user.setHealth(50);
                    user.setFocus(50);
                    user.setDiscipline(50);
                    user.setStrength(50);

                    JOptionPane.showMessageDialog(
                        this,
                        "Progress reset. Return to Dashboard to see your updated stats."
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Could not reset progress."
                    );
                }
            }
        });

        logout.addActionListener(e -> main.logout());
    }

    private JPanel section(String name) {
        JPanel p = Style.card();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel title = Style.label(name);
        title.setForeground(Style.ACCENT_BLUE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 11));
        title.setAlignmentX(LEFT_ALIGNMENT);

        p.add(title);
        p.add(Box.createVerticalStrut(14));

        return p;
    }

    private JPanel settingRow(String key, String value) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        JLabel k = Style.label(key);
        k.setForeground(Style.MUTED);

        r.add(k, BorderLayout.WEST);
        r.add(Style.label(value), BorderLayout.EAST);

        return r;
    }

    private JCheckBox check(String text, boolean selected) {
        JCheckBox c = new JCheckBox(text, selected);
        c.setOpaque(false);
        c.setForeground(Style.TEXT);
        c.setFont(new Font("Segoe UI", Font.BOLD, 13));

        return c;
    }

    private JPanel checkRow(
            String title,
            String description,
            JCheckBox c) {

        JPanel r = new JPanel(new BorderLayout(10, 0));
        r.setOpaque(false);

        JPanel labels = new JPanel();
        labels.setOpaque(false);
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));

        labels.add(Style.label(title));

        JLabel d = Style.label(description);
        d.setForeground(Style.MUTED);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 11));

        labels.add(Box.createVerticalStrut(4));
        labels.add(d);

        r.add(labels, BorderLayout.CENTER);
        r.add(c, BorderLayout.EAST);

        return r;
    }
}

