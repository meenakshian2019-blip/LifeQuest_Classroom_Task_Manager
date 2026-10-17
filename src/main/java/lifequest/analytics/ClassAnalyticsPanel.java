package lifequest.analytics;

import lifequest.database.AssignmentDAO;
import lifequest.database.AssignmentDAO.Assignment;
import lifequest.database.AssignmentDAO.Submission;
import lifequest.ui.Style;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Read-only teacher analytics page, separate from Classroom Manager.
 * Uses existing assignment, submission, and leaderboard DAO methods.
 */
public class ClassAnalyticsPanel extends JPanel {
    private final AssignmentDAO dao = new AssignmentDAO();

    public ClassAnalyticsPanel() {
        setLayout(new BorderLayout(16, 16));
        setBackground(Style.BACKGROUND);
        setBorder(new EmptyBorder(26, 28, 26, 28));
        build();
    }

    private void build() {
        removeAll();

        JLabel title = Style.title("Class Analytics");
        title.setFont(new Font("Segoe UI", Font.BOLD, 27));
        JLabel subtitle = Style.label("A quick overview of assignments and student progress");
        subtitle.setForeground(Style.MUTED);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);
        add(heading, BorderLayout.NORTH);

        List<Assignment> assignments = dao.allAssignments();
        List<Submission> submissions = dao.submissions();
        List<String[]> leaderboard = dao.leaderboard();

        int pending = 0, approved = 0, rejected = 0;
        for (Submission submission : submissions) {
            if ("PENDING".equalsIgnoreCase(submission.status)) pending++;
            else if ("APPROVED".equalsIgnoreCase(submission.status)) approved++;
            else if ("REJECTED".equalsIgnoreCase(submission.status)) rejected++;
        }

        JPanel main = new JPanel(new BorderLayout(14, 14));
        main.setOpaque(false);

        JPanel cards = new JPanel(new GridLayout(1, 4, 12, 12));
        cards.setOpaque(false);
        cards.add(metricCard("ASSIGNMENTS", assignments.size(), "Published assignments", Style.ACCENT));
        cards.add(metricCard("SUBMISSIONS", submissions.size(), "Received from students", Style.ACCENT_BLUE));
        cards.add(metricCard("PENDING", pending, "Awaiting review", new Color(255, 190, 92)));
        cards.add(metricCard("APPROVED", approved, "Accepted submissions", Style.SUCCESS));
        main.add(cards, BorderLayout.NORTH);

        JPanel lower = new JPanel(new GridLayout(1, 2, 14, 14));
        lower.setOpaque(false);
        lower.add(statusPanel(submissions.size(), approved, pending, rejected));
        lower.add(leaderboardPanel(leaderboard));
        main.add(lower, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel metricCard(String label, int value, String description, Color accent) {
        JPanel card = Style.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel name = Style.label(label);
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));
        name.setForeground(Style.MUTED);
        JLabel number = Style.title(String.valueOf(value));
        number.setFont(new Font("Segoe UI", Font.BOLD, 30));
        number.setForeground(accent);
        JLabel detail = Style.label(description);
        detail.setForeground(Style.MUTED);
        card.add(name);
        card.add(Box.createVerticalStrut(10));
        card.add(number);
        card.add(Box.createVerticalStrut(4));
        card.add(detail);
        return card;
    }

    private JPanel statusPanel(int total, int approved, int pending, int rejected) {
        JPanel panel = Style.card();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel title = Style.title("Submission status");
        title.setFont(new Font("Segoe UI", Font.BOLD, 19));
        panel.add(title);
        panel.add(Box.createVerticalStrut(18));

        if (total == 0) {
            JLabel empty = Style.label("No student submissions yet.");
            empty.setForeground(Style.MUTED);
            panel.add(empty);
            panel.add(Box.createVerticalStrut(8));
            JLabel hint = Style.label("Status summaries will appear here once students submit work.");
            hint.setForeground(Style.MUTED);
            panel.add(hint);
            return panel;
        }

        addStatusBar(panel, "Approved", approved, total, Style.SUCCESS);
        addStatusBar(panel, "Pending review", pending, total, new Color(255, 190, 92));
        addStatusBar(panel, "Rejected", rejected, total, new Color(255, 105, 145));
        int reviewed = approved + rejected;
        int rate = total == 0 ? 0 : (reviewed * 100 / total);
        panel.add(Box.createVerticalStrut(18));
        JLabel completion = Style.label("Reviewed: " + rate + "%");
        completion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(completion);
        return panel;
    }

    private void addStatusBar(JPanel panel, String label, int count, int total, Color color) {
        JPanel line = new JPanel(new BorderLayout(8, 6));
        line.setOpaque(false);
        JLabel name = Style.label(label + " (" + count + ")");
        name.setForeground(Style.TEXT);
        JProgressBar bar = new JProgressBar(0, total);
        bar.setValue(count);
        bar.setStringPainted(false);
        bar.setForeground(color);
        bar.setBackground(new Color(35, 43, 72));
        bar.setBorderPainted(false);
        line.add(name, BorderLayout.NORTH);
        line.add(bar, BorderLayout.CENTER);
        panel.add(line);
        panel.add(Box.createVerticalStrut(14));
    }

    private JPanel leaderboardPanel(List<String[]> leaderboard) {
        JPanel panel = Style.card();
        panel.setLayout(new BorderLayout(8, 12));
        JLabel title = Style.title("Top students");
        title.setFont(new Font("Segoe UI", Font.BOLD, 19));
        panel.add(title, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Rank", "Student", "Points", "Approved"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        int rank = 1;
        for (String[] student : leaderboard) {
            if (rank > 8) break;
            model.addRow(new Object[]{rank++, student[0], student[1], student[2]});
        }
        JTable table = new JTable(model);
        table.setRowHeight(27);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        if (leaderboard.isEmpty()) {
            JLabel empty = Style.label("Student rankings will appear here.");
            empty.setForeground(Style.MUTED);
            panel.add(empty, BorderLayout.SOUTH);
        }
        return panel;
    }
}
