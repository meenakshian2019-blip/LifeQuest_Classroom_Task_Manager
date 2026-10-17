package lifequest.quests;

import lifequest.Main;
import lifequest.database.QuestDAO;
import lifequest.database.UserDAO;
import lifequest.database.QuestDAO.Quest;
import lifequest.model.User;
import lifequest.ui.Style;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class QuestPanel extends JPanel {
    private final Main main; private final User user; private final DefaultTableModel model; private final JTable table;
    public QuestPanel(Main main, User user) {
        this.main=main; this.user=user; setBackground(Style.BACKGROUND); setLayout(new BorderLayout(16,16));
        setBorder(BorderFactory.createEmptyBorder(28,30,28,30));
        JPanel heading=new JPanel(new BorderLayout()); heading.setOpaque(false);
        JPanel titles=new JPanel(); titles.setOpaque(false); titles.setLayout(new BoxLayout(titles,BoxLayout.Y_AXIS));
        titles.add(Style.title("Your Quests")); JLabel hint=Style.label("Every completed quest moves you forward."); hint.setForeground(Style.MUTED); titles.add(Box.createVerticalStrut(5)); titles.add(hint);
        heading.add(titles,BorderLayout.WEST); JButton create=Style.button("＋  Create Quest"); heading.add(create,BorderLayout.EAST); create.addActionListener(e->create()); add(heading,BorderLayout.NORTH);
        model=new DefaultTableModel(new Object[]{"ID","QUEST","CATEGORY","PRIORITY","DIFFICULTY","XP REWARD","REPEAT"},0){public boolean isCellEditable(int r,int c){return false;}};
        table=new JTable(model); table.setRowHeight(48); table.setShowGrid(false); table.setIntercellSpacing(new Dimension(0,1)); table.setSelectionBackground(new Color(55,45,105)); table.setSelectionForeground(Style.TEXT); table.setBackground(Style.CARD); table.setForeground(Style.TEXT); table.setFont(new Font("Segoe UI",Font.PLAIN,13));
        table.getTableHeader().setBackground(new Color(24,31,62)); table.getTableHeader().setForeground(Style.MUTED); table.getTableHeader().setFont(new Font("Segoe UI",Font.BOLD,11)); table.getTableHeader().setPreferredSize(new Dimension(10,38));
        table.getColumnModel().getColumn(0).setMinWidth(0); table.getColumnModel().getColumn(0).setMaxWidth(0); table.getColumnModel().getColumn(0).setWidth(0);
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer(){public Component getTableCellRendererComponent(JTable t,Object v,boolean s,boolean f,int r,int c){JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,s,f,r,c);l.setForeground(new Color(92,224,210));l.setText("✦ "+v+" XP");return l;}});
        add(Style.scroll(table),BorderLayout.CENTER);
        JPanel bottom=Style.card(); bottom.setLayout(new BorderLayout(12,0)); JLabel note=Style.label("Select a quest when you're ready to complete it."); note.setForeground(Style.MUTED); bottom.add(note,BorderLayout.CENTER);
        JButton complete=Style.button("✓  Complete selected quest"); bottom.add(complete,BorderLayout.EAST); complete.addActionListener(e->complete()); add(bottom,BorderLayout.SOUTH); refresh();
    }
    private void create(){CreateQuestDialog d=new CreateQuestDialog(main,user);d.setVisible(true);if(d.isSaved())refresh();}
    private void complete(){int r=table.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Select a quest first.");return;}int id=(int)model.getValueAt(r,0),xp=(int)model.getValueAt(r,5);if(new QuestDAO().completeQuest(id)){int total=user.getXp()+xp,old=user.getLevel(),level=total/100+1;user.setXp(total);user.setLevel(level);user.setCoins(user.getCoins()+xp/10);if(level>old){user.setHealth(Math.min(100,user.getHealth()+5));user.setFocus(Math.min(100,user.getFocus()+5));user.setDiscipline(Math.min(100,user.getDiscipline()+5));user.setStrength(Math.min(100,user.getStrength()+5));JOptionPane.showMessageDialog(this,"Level up! You reached level "+level+"!");}new UserDAO().updateProgress(user);JOptionPane.showMessageDialog(this,"Quest completed! +"+xp+" XP");refresh();}else JOptionPane.showMessageDialog(this,"Could not complete this quest.");}
    private void refresh(){model.setRowCount(0);for(Quest q:new QuestDAO().getActive(user.getId()))model.addRow(new Object[]{q.id,q.name,q.category,q.priority,q.difficulty,q.xp,q.repeatType});}
}
