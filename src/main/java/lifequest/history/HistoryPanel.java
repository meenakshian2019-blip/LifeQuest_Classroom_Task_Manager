package lifequest.history;

import lifequest.database.QuestDAO;
import lifequest.database.QuestDAO.Quest;
import lifequest.model.User;
import lifequest.ui.Style;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class HistoryPanel extends JPanel {
    private final User user; 
    private final JComboBox<String> filter=new JComboBox<>(new String[]{"All","Today","This Week"}); private final DefaultTableModel model;
    public HistoryPanel(User user){this.user=user;
        setBackground(Style.BACKGROUND);
        setLayout(new BorderLayout(16,16));
        setBorder(BorderFactory.createEmptyBorder(28,30,28,30));
        JPanel top=new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel headings=new JPanel();
        headings.setOpaque(false);
        headings.setLayout(new BoxLayout(headings,BoxLayout.Y_AXIS));
        headings.add(Style.title("Quest History"));
        JLabel sub=Style.label("Look back at every step you've completed.");
        sub.setForeground(Style.MUTED);
        headings.add(Box.createVerticalStrut(5));
        headings.add(sub);
        top.add(headings,BorderLayout.WEST);
        /*Style.combo(filter);
        filter.setPreferredSize(new Dimension(145,36));
        top.add(filter,BorderLayout.EAST);*/
        
Style.combo(filter);

// Fix dropdown text visibility
filter.setForeground(new Color(25, 30, 50));
filter.setBackground(Color.WHITE);
filter.setOpaque(true);

filter.setPreferredSize(new Dimension(145, 36));
top.add(filter, BorderLayout.EAST);

        add(top,BorderLayout.NORTH);
        model=new DefaultTableModel(new Object[]{"QUEST","CATEGORY","DIFFICULTY","XP EARNED","COMPLETED AT"},0){public boolean isCellEditable(int r,int c){return false;}};
        JTable table=new JTable(model);
        table.setRowHeight(44);
        table.setShowGrid(false);
        table.setBackground(Style.CARD);
        table.setForeground(Style.TEXT);
        table.setSelectionBackground(new Color(55,45,105));
        table.setSelectionForeground(Style.TEXT);
        table.setFont(new Font("Segoe UI",Font.PLAIN,13));
        table.getTableHeader().setBackground(new Color(24,31,62));
        table.getTableHeader().setForeground(Style.MUTED);table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11));
        add(Style.scroll(table),BorderLayout.CENTER);
        JPanel foot=Style.card();
        foot.setLayout(new BorderLayout());
        JLabel note=Style.label("Every completed quest counts. Keep going!");
        note.setForeground(Style.MUTED);
        foot.add(note,BorderLayout.CENTER);
        add(foot,BorderLayout.SOUTH);
        filter.addActionListener(e->refresh());
        refresh();}
    private void refresh(){model.setRowCount(0);String f=(String)filter.getSelectedItem();
        for(Quest q:new QuestDAO().getHistory(user.getId(),f))model.addRow(new Object[]{q.name,q.category,q.difficulty,"✦ "+q.xp,q.completedAt==null?"—":new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(q.completedAt)});}
}

