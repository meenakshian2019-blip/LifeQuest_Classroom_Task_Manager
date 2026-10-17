package lifequest.classroom;

import lifequest.database.AssignmentDAO;
import lifequest.ui.Style;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Standalone class leaderboard page for students.
 * This displays the same leaderboard data as the "Class leaderboard" tab
 * inside the Assignments page, without showing the assignments table.
 */
public class LeaderboardPanel extends JPanel {
    public LeaderboardPanel() {
        setLayout(new BorderLayout(14, 14));
        setBackground(Style.BACKGROUND);
        setBorder(new EmptyBorder(24, 26, 24, 26));

        JLabel heading = Style.title("Classroom · Leaderboard");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 25));
        add(heading, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setOpaque(false);

        JLabel subtitle = Style.label("See how students are progressing based on approved assignment points.");
        subtitle.setForeground(Style.MUTED);
        content.add(subtitle, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Rank", "Student", "Total points", "Approved assignments"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        int rank = 1;
        for (String[] row : new AssignmentDAO().leaderboard()) {
            model.addRow(new Object[]{rank++, row[0], row[1], row[2]});
        }

        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(table);
        content.add(scroll, BorderLayout.CENTER);

        if (model.getRowCount() == 0) {
            JLabel empty = Style.label("No approved assignments yet. Your class leaderboard will appear here once points are earned.");
            empty.setForeground(Style.MUTED);
            empty.setBorder(new EmptyBorder(12, 4, 12, 4));
            content.add(empty, BorderLayout.SOUTH);
        }

        add(content, BorderLayout.CENTER);
    }
}
