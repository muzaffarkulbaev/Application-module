package uz.com.applicationmodule.security;

import uz.com.applicationmodule.model.entity.Session;
import uz.com.applicationmodule.model.entity.User;

public class UserAndSession {
    private UserAndSession(){
    }
    public static User currentUser;
    public static Session currentSession;
}
