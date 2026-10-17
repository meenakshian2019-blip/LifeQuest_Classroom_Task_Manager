package lifequest.database;
import java.sql.*;import java.util.*;
public class QuestDAO {
 public static class Quest {
  public int id,xp; public String name,category,priority,difficulty,repeatType; public boolean completed; public Timestamp completedAt,createdAt;
  public Quest(int id,String name,String category,String priority,String difficulty,int xp,String repeatType,boolean completed,Timestamp completedAt,Timestamp createdAt){
   this.id=id;this.name=name;this.category=category;this.priority=priority;this.difficulty=difficulty;this.xp=xp;this.repeatType=repeatType;this.completed=completed;this.completedAt=completedAt;this.createdAt=createdAt;
  }
 }
 public List<Quest> getActive(int uid){return getList(uid,"active");}
 public List<Quest> getHistory(int uid,String f){
  String q=f.equals("Today")?"SELECT * FROM quests WHERE user_id=? AND completed=true AND DATE(completed_at)=CURDATE() ORDER BY completed_at DESC":
   f.equals("This Week")?"SELECT * FROM quests WHERE user_id=? AND completed=true AND YEARWEEK(completed_at,1)=YEARWEEK(CURDATE(),1) ORDER BY completed_at DESC":
   "SELECT * FROM quests WHERE user_id=? AND completed=true ORDER BY completed_at DESC";
  return run(q,uid);
 }
 private List<Quest> getList(int uid,String x){return run("SELECT * FROM quests WHERE user_id=? AND completed=false ORDER BY created_at DESC",uid);}
 private List<Quest> run(String q,int uid){List<Quest> a=new ArrayList<>();try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement(q)){s.setInt(1,uid);try(ResultSet r=s.executeQuery()){while(r.next())a.add(from(r));}}catch(SQLException e){System.out.println(e.getMessage());}return a;}
 public boolean addQuest(int uid,String n,String cat,String pri,String diff,int xp,String rep){
  try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement("INSERT INTO quests(user_id,name,category,priority,difficulty,xp,repeat_type) VALUES(?,?,?,?,?,?,?)")){
   s.setInt(1,uid);s.setString(2,n);s.setString(3,cat);s.setString(4,pri);s.setString(5,diff);s.setInt(6,xp);s.setString(7,rep);s.executeUpdate();return true;
  }catch(SQLException e){return false;}
 }
 public boolean completeQuest(int id){
  try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement("UPDATE quests SET completed=true,completed_at=NOW() WHERE id=? AND completed=false")){s.setInt(1,id);return s.executeUpdate()>0;}catch(SQLException e){return false;}
 }
 public int completedCount(int uid){return one("SELECT COUNT(*) FROM quests WHERE user_id=? AND completed=true",uid);}
 public int totalXp(int uid){return one("SELECT COALESCE(SUM(xp),0) FROM quests WHERE user_id=? AND completed=true",uid);}
 private int one(String q,int uid){try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement(q)){s.setInt(1,uid);try(ResultSet r=s.executeQuery()){if(r.next())return r.getInt(1);}}catch(SQLException e){}return 0;}
 private Quest from(ResultSet r)throws SQLException{return new Quest(r.getInt("id"),r.getString("name"),r.getString("category"),r.getString("priority"),r.getString("difficulty"),r.getInt("xp"),r.getString("repeat_type"),r.getBoolean("completed"),r.getTimestamp("completed_at"),r.getTimestamp("created_at"));}
}
