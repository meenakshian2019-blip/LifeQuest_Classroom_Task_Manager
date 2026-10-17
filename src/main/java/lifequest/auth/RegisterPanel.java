package lifequest.auth;

import lifequest.Main;
import lifequest.database.UserDAO;
import lifequest.ui.MascotPanel;
import lifequest.ui.Style;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private final Main main;
    private final JTextField username = new JTextField(), email = new JTextField();
    private final JPasswordField password = new JPasswordField();

    public RegisterPanel(Main main) {
        this.main = main; setBackground(Style.BACKGROUND); setLayout(new GridBagLayout());
        JPanel card = Style.card(); card.setLayout(new BorderLayout(20, 10));
        card.setBorder(new EmptyBorder(25, 30, 25, 30)); card.setPreferredSize(new Dimension(760, 540));
        JPanel intro = new JPanel(); intro.setOpaque(false); intro.setLayout(new BoxLayout(intro, BoxLayout.Y_AXIS));
        intro.add(Style.title("Begin your journey"));
        JLabel sub = Style.label("Create your adventurer account."); sub.setForeground(Style.MUTED); intro.add(sub);
        intro.add(Box.createVerticalStrut(4)); intro.add(new MascotPanel());
        JPanel form = new JPanel(); form.setOpaque(false); form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        addField(form, "Username", username); addField(form, "Email", email); addField(form, "Password", password);
        JButton create = Style.button("Create account  ✦"); create.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JButton back = Style.secondaryButton("Already have an account? Login"); back.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        form.add(Box.createVerticalStrut(8)); form.add(create); form.add(Box.createVerticalStrut(10)); form.add(back);
        create.addActionListener(e -> register()); back.addActionListener(e -> main.showLogin());
        card.add(intro, BorderLayout.WEST); card.add(form, BorderLayout.CENTER); add(card);
    }
    private void addField(JPanel p, String name, JTextField f) {
        JLabel l = Style.label(name); f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); Style.text(f);
        p.add(l); p.add(Box.createVerticalStrut(6)); p.add(f); p.add(Box.createVerticalStrut(13));
    }
    private void register() {
        String u = username.getText().trim(), e = email.getText().trim(), p = new String(password.getPassword());
        if (u.isEmpty() || e.isEmpty() || p.isEmpty()) { JOptionPane.showMessageDialog(this, "Please fill in every field."); return; }
        if (!e.contains("@") || p.length() < 4) { JOptionPane.showMessageDialog(this, "Enter a valid email and a password of at least 4 characters."); return; }
        if (new UserDAO().register(u, e, p)) { JOptionPane.showMessageDialog(this, "Account created! You can now log in."); main.showLogin(); }
        else JOptionPane.showMessageDialog(this, "Registration failed. Username or email may already exist.");
    }
}
