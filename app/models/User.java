package models;
 
import play.data.validation.*;

import siena.*;
import siena.core.batch.Batch;

@Table("users")
public class User extends Model {
    
    @Id(Generator.AUTO_INCREMENT)
    public Long id;
    
    @Required
    @Column("email")
    public String email;
    
    @Required
    @Column("password")
    public String password;
    
    @Column("fullname")
    public String fullname;
    
    public boolean isAdmin;
    
    public User(){
    }
    
    public User(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }
    
    public static Query<User> all() {
        return Model.all(User.class);
    }

    public static Batch<User> batch() {
        return Model.batch(User.class);
    }
    
    public static User connect(String email, String password){
        return Model.all(User.class).filter("email", email).filter("password", password).get();
    }    
    
    @Override
    public String toString() {
        return email;
    } 
}