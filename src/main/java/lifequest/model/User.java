package lifequest.model;
public class User {
 private int id, level, xp, coins, health, focus, discipline, strength;
 private String username, email, password, role = "STUDENT";
 private boolean darkMode, notifications;
 public User() {}
 public User(int id,String username,String email,String password,int level,int xp,int coins,int health,int focus,int discipline,int strength,boolean darkMode,boolean notifications){
  this.id=id;this.username=username;this.email=email;this.password=password;this.level=level;this.xp=xp;this.coins=coins;this.health=health;this.focus=focus;this.discipline=discipline;this.strength=strength;this.darkMode=darkMode;this.notifications=notifications;
 }
 public String getRole(){return role;} public void setRole(String v){role=v==null?"STUDENT":v;} public boolean isAdmin(){return "ADMIN".equalsIgnoreCase(role);}
 public int getId(){return id;} public void setId(int v){id=v;}
 public String getUsername(){return username;} public void setUsername(String v){username=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public int getLevel(){return level;} public void setLevel(int v){level=v;}
 public int getXp(){return xp;} public void setXp(int v){xp=v;}
 public int getCoins(){return coins;} public void setCoins(int v){coins=v;}
 public int getHealth(){return health;} public void setHealth(int v){health=v;}
 public int getFocus(){return focus;} public void setFocus(int v){focus=v;}
 public int getDiscipline(){return discipline;} public void setDiscipline(int v){discipline=v;}
 public int getStrength(){return strength;} public void setStrength(int v){strength=v;}
 public boolean isDarkMode(){return darkMode;} public void setDarkMode(boolean v){darkMode=v;}
 public boolean isNotifications(){return notifications;} public void setNotifications(boolean v){notifications=v;}
}
