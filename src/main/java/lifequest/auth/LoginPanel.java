package lifequest.auth;

import lifequest.Main;
import lifequest.database.UserDAO;
import lifequest.model.User;
import lifequest.ui.MascotPanel;
import lifequest.ui.Style;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final Main main;
    private final JTextField username = new JTextField();
    private final JPasswordField password = new JPasswordField();

    public LoginPanel(Main main) {
        this.main = main;
        setBackground(Style.BACKGROUND);
        setLayout(new GridBagLayout());
        JPanel layout = new JPanel(new GridLayout(1, 2, 25, 0));
        layout.setOpaque(false);
        layout.setBorder(new EmptyBorder(25, 35, 25, 35));

        JPanel welcome = Style.card();
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
        JLabel logo = Style.title("✦ LifeQuest");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        JLabel tagline = Style.label("Small steps. Bigger dreams.");
        tagline.setForeground(Style.MUTED);
        welcome.add(Box.createVerticalGlue()); welcome.add(logo); welcome.add(Box.createVerticalStrut(6));
        welcome.add(tagline); welcome.add(Box.createVerticalStrut(12));
        welcome.add(new MascotPanel());
        JLabel message = Style.label("Meet Lumi, your little quest companion.");
        message.setForeground(Style.MUTED); welcome.add(message); welcome.add(Box.createVerticalGlue());

        JPanel form = Style.card();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(30, 28, 30, 28));
        JLabel heading = Style.title("Welcome back!");
        JLabel sub = Style.label("Sign in as a teacher or student to continue."); sub.setForeground(Style.MUTED);
        form.add(heading); form.add(Box.createVerticalStrut(8)); form.add(sub); form.add(Box.createVerticalStrut(28));
        addField(form, "Username", username); addField(form, "Password", password);
        JButton login = Style.button("Login  →"); login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        JButton register = Style.secondaryButton("Create an account"); register.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        form.add(Box.createVerticalStrut(14)); form.add(login); form.add(Box.createVerticalStrut(10)); form.add(register);
        login.addActionListener(e -> login()); register.addActionListener(e -> main.showRegister());
        layout.add(welcome); layout.add(form); add(layout);
    }

    private void addField(JPanel p, String name, JComponent field) {
        JLabel l = Style.label(name); l.setAlignmentX(LEFT_ALIGNMENT); field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        if (field instanceof JTextField) Style.text((JTextField) field);
        p.add(l); p.add(Box.createVerticalStrut(7)); p.add(field); p.add(Box.createVerticalStrut(16));
    }
    private void login() {
        String u = username.getText().trim(), p = new String(password.getPassword());
        if (u.isEmpty() || p.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter username and password."); return; }
        User user = new UserDAO().login(u, p);
        if (user != null) main.showApp(user);
        else JOptionPane.showMessageDialog(this, "Login failed. Check your details and database connection.");
    }
}
