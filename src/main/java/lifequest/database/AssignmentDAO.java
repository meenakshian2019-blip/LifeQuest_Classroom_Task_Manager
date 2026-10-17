package lifequest.database;

import java.sql.*;
import java.util.*;

/** Database operations for teacher-created classroom assignments. */
public class AssignmentDAO {
    public static class Assignment {
        public int id, points; public String title, description, difficulty; public java.sql.Date dueDate; public String status;
        public Assignment(int id,String title,String description,String difficulty,int points,java.sql.Date dueDate,String status){this.id=id;this.title=title;this.description=description;this.difficulty=difficulty;this.points=points;this.dueDate=dueDate;this.status=status;}
    }
    public static class Submission {
        public int id, assignmentId, studentId, points; public String student, assignment, status; public Timestamp submittedAt;
        public Submission(int id,int aid,int sid,String student,String assignment,String status,int points,Timestamp submittedAt){this.id=id;assignmentId=aid;studentId=sid;this.student=student;this.assignment=assignment;this.status=status;this.points=points;this.submittedAt=submittedAt;}
    }
    public boolean create(String title,String description,String difficulty,int points,java.sql.Date due,int teacherId){
        String sql="INSERT INTO assignments(title,description,difficulty,points,due_date,created_by) VALUES(?,?,?,?,?,?)";
        try(Connection c=DB.getConnection();PreparedStatement s=c.prepareStatement(sql)){s.setString(1,title);s.setString(2,description);s.setString(3,difficulty);s.setInt(4,points);if(due==null)s.setNull(5,Types.DATE);else s.setDate(5,new java.sql.Date(due.getTime()));s.setInt(6,teacherId);return s.executeUpdate()==1;}catch(SQLException e){System.err.println("Create assignment: "+e.getMessage());return false;}
    }
    public List<Assignment> assignmentsForStudent(int studentId){
        List<Assignment> out=new ArrayList<>();String sql="SELECT a.*, COALESCE(s.status,'Not submitted') status FROM assignments a LEFT JOIN submissions s ON s.assignment_id=a.id AND s.student_id=? ORDER BY a.created_at DESC";
        try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,studentId);try(ResultSet r=p.executeQuery()){while(r.next())out.add(new Assignment(r.getInt("id"),r.getString("title"),r.getString("description"),r.getString("difficulty"),r.getInt("points"),r.getDate("due_date"),r.getString("status")));}}catch(SQLException e){System.err.println(e.getMessage());}return out;
    }
    public List<Assignment> allAssignments(){List<Assignment> out=new ArrayList<>();String sql="SELECT a.*, 'Class assignment' status FROM assignments a ORDER BY a.created_at DESC";try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next())out.add(new Assignment(r.getInt("id"),r.getString("title"),r.getString("description"),r.getString("difficulty"),r.getInt("points"),r.getDate("due_date"),r.getString("status")));}catch(SQLException e){System.err.println(e.getMessage());}return out;}
    public boolean submit(int assignmentId,int studentId,String note){String sql="INSERT INTO submissions(assignment_id,student_id,note,status) VALUES(?,?,?,'PENDING') ON DUPLICATE KEY UPDATE note=VALUES(note),status=IF(status='APPROVED','APPROVED','PENDING'),submitted_at=CURRENT_TIMESTAMP";try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,assignmentId);p.setInt(2,studentId);p.setString(3,note);return p.executeUpdate()>0;}catch(SQLException e){System.err.println(e.getMessage());return false;}}
    public List<Submission> submissions(){List<Submission> out=new ArrayList<>();String sql="SELECT s.id,s.assignment_id,s.student_id,u.username,a.title,s.status,s.points,s.submitted_at FROM submissions s JOIN users u ON u.id=s.student_id JOIN assignments a ON a.id=s.assignment_id ORDER BY s.submitted_at DESC";try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next())out.add(new Submission(r.getInt(1),r.getInt(2),r.getInt(3),r.getString(4),r.getString(5),r.getString(6),r.getInt(7),r.getTimestamp(8)));}catch(SQLException e){System.err.println(e.getMessage());}return out;}
    public boolean review(int submissionId,boolean approve){String sql="UPDATE submissions SET status=?,points=IF(?,(SELECT a.points FROM assignments a WHERE a.id=submissions.assignment_id),0),reviewed_at=CURRENT_TIMESTAMP WHERE id=? AND status<>'APPROVED'";try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,approve?"APPROVED":"REJECTED");p.setBoolean(2,approve);p.setInt(3,submissionId);return p.executeUpdate()>0;}catch(SQLException e){System.err.println(e.getMessage());return false;}}
    public List<String[]> leaderboard(){List<String[]> out=new ArrayList<>();String sql="SELECT u.username,COALESCE(SUM(s.points),0) score, SUM(CASE WHEN s.status='APPROVED' THEN 1 ELSE 0 END) completed FROM users u LEFT JOIN submissions s ON s.student_id=u.id AND s.status='APPROVED' WHERE u.role='STUDENT' GROUP BY u.id,u.username ORDER BY score DESC,u.username";try(Connection c=DB.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next())out.add(new String[]{r.getString(1),String.valueOf(r.getInt(2)),String.valueOf(r.getInt(3))});}catch(SQLException e){System.err.println(e.getMessage());}return out;}
}
