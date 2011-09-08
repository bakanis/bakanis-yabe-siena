package controllers;

import models.*;
import play.mvc.Controller;

//public class Security extends Secure.Security {
public class Security extends Controller {

    static boolean authentify(String username, String password) {
        return User.connect(username, password) != null;
    }
    
    static boolean check(String profile) {
        if("admin".equals(profile)) {
            //return User.find("byEmail", connected()).<User>first().isAdmin;
            return User.all().filter("email", "bob@gmail.com").get().isAdmin;
            //TODO: taisyti
        }
        return false;
    }
    
    static void onDisconnected() {
        Application.index();
    }
    
    static void onAuthenticated() {
        Admin.index();
    }

    static boolean isConnected() {
        return true;
        //TODO: taisyti
    }
    
}

