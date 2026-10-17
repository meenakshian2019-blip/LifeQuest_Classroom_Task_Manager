package lifequest.database;
import java.sql.*;
public class DB {
 private static final String URL=System.getenv().getOrDefault("LIFEQUEST_DB_URL","jdbc:mysql://localhost:3306/lifequest?useSSL=false&serverTimezone=UTC");
 private static final String USER=System.getenv().getOrDefault("LIFEQUEST_DB_USER","root");
 private static final String PASSWORD=System.getenv().getOrDefault("LIFEQUEST_DB_PASSWORD","m1234");
 public static Connection getConnection() throws SQLException { return DriverManager.getConnection(URL,USER,PASSWORD); }
}
