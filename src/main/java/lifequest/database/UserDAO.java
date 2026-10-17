package lifequest.database;
import lifequest.model.User;
import java.sql.*;
public class UserDAO {
 public User login(String u,String p){
  String sql="SELECT * FROM users WHERE username=? AND password=?";
  try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement(sql)){s.setString(1,u);s.setString(2,p);try(ResultSet r=s.executeQuery()){if(r.next())return from(r);}}
  catch(SQLException e){System.out.println("Login error: "+e.getMessage());} return null;
 }
 public boolean register(String u,String e,String p){
  try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement("INSERT INTO users(username,email,password,role) VALUES(?,?,?,'STUDENT')")){
   s.setString(1,u);s.setString(2,e);s.setString(3,p);s.executeUpdate();return true;
  }catch(SQLException x){System.out.println("Registration error: "+x.getMessage());return false;}
 }
 public boolean updateProgress(User u){
  String q="UPDATE users SET level=?,xp=?,coins=?,health=?,focus=?,discipline=?,strength=?,dark_mode=?,notifications=? WHERE id=?";
  try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement(q)){
   s.setInt(1,u.getLevel());s.setInt(2,u.getXp());s.setInt(3,u.getCoins());s.setInt(4,u.getHealth());s.setInt(5,u.getFocus());s.setInt(6,u.getDiscipline());s.setInt(7,u.getStrength());s.setBoolean(8,u.isDarkMode());s.setBoolean(9,u.isNotifications());s.setInt(10,u.getId());s.executeUpdate();return true;
  }catch(SQLException e){return false;}
 }
 public boolean updateSettings(int id,boolean dark,boolean notes){
  try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement("UPDATE users SET dark_mode=?,notifications=? WHERE id=?")){
   s.setBoolean(1,dark);s.setBoolean(2,notes);s.setInt(3,id);s.executeUpdate();return true;
  }catch(SQLException e){return false;}
 }
 public boolean resetProgress(int id){
  try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement("UPDATE users SET level=1,xp=0,coins=0,health=50,focus=50,discipline=50,strength=50 WHERE id=?")){
   s.setInt(1,id);s.executeUpdate();
   try(PreparedStatement q=c.prepareStatement("DELETE FROM quests WHERE user_id=?")){q.setInt(1,id);q.executeUpdate();}
   return true;
  }catch(SQLException e){return false;}
 }
 private User from(ResultSet r)throws SQLException{User u=new User(r.getInt("id"),r.getString("username"),r.getString("email"),r.getString("password"),r.getInt("level"),r.getInt("xp"),r.getInt("coins"),r.getInt("health"),r.getInt("focus"),r.getInt("discipline"),r.getInt("strength"),r.getBoolean("dark_mode"),r.getBoolean("notifications"));u.setRole(r.getString("role"));return u;}
}
