package lifequest.quests;

import lifequest.database.QuestDAO;
import lifequest.model.User;
import lifequest.ui.Style;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateQuestDialog extends JDialog {
    private boolean saved=false;
    private final JTextField name=new JTextField();
    private final JComboBox<String> category=new JComboBox<>(new String[]{"Study","Fitness","Personal","Work","Other"});
    private final JComboBox<String> priority=new JComboBox<>(new String[]{"Low","Medium","High"});
    private final JComboBox<String> difficulty=new JComboBox<>(new String[]{"Easy","Medium","Hard","Epic"});
    private final JComboBox<String> repeat=new JComboBox<>(new String[]{"None","Daily","Weekly"});
    public CreateQuestDialog(Window owner,User user){super(owner,"Create a Quest",ModalityType.APPLICATION_MODAL);setSize(520,520);setLocationRelativeTo(owner);getContentPane().setBackground(Style.BACKGROUND);
        JPanel outer=new JPanel(new BorderLayout(10,18));outer.setBackground(Style.BACKGROUND);outer.setBorder(new EmptyBorder(22,24,22,24));
        JLabel title=Style.title("Create a new quest");JLabel subtitle=Style.label("Choose your challenge and earn XP.");subtitle.setForeground(Style.MUTED);JPanel head=new JPanel();head.setOpaque(false);head.setLayout(new BoxLayout(head,BoxLayout.Y_AXIS));head.add(title);head.add(Box.createVerticalStrut(5));head.add(subtitle);outer.add(head,BorderLayout.NORTH);
        JPanel fields=Style.card();fields.setLayout(new GridLayout(5,2,12,13));addRow(fields,"Quest name",name);addRow(fields,"Category",category);addRow(fields,"Priority",priority);addRow(fields,"Difficulty",difficulty);addRow(fields,"Repeat",repeat);outer.add(fields,BorderLayout.CENTER);
        JPanel buttons=new JPanel(new GridLayout(1,2,10,0));buttons.setOpaque(false);JButton save=Style.button("Create Quest  ✦"),cancel=Style.secondaryButton("Cancel");buttons.add(save);buttons.add(cancel);outer.add(buttons,BorderLayout.SOUTH);save.addActionListener(e->save(user));cancel.addActionListener(e->dispose());setContentPane(outer);}
    private void addRow(JPanel p,String label,JComponent field){p.add(Style.label(label));if(field instanceof JTextField)Style.text((JTextField)field);else Style.combo((JComboBox<?>)field);p.add(field);}
    private void save(User u){String n=name.getText().trim();if(n.isEmpty()){JOptionPane.showMessageDialog(this,"Enter a quest name.");return;}String d=(String)difficulty.getSelectedItem();int xp=d.equals("Easy")?20:d.equals("Medium")?50:d.equals("Hard")?80:120;if(new QuestDAO().addQuest(u.getId(),n,(String)category.getSelectedItem(),(String)priority.getSelectedItem(),d,xp,(String)repeat.getSelectedItem())){saved=true;dispose();}else JOptionPane.showMessageDialog(this,"Could not create the quest. Check the database connection.");}
    public boolean isSaved(){return saved;}
}