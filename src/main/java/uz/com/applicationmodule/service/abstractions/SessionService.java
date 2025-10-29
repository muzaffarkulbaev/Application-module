package uz.com.applicationmodule.service.abstractions;

import uz.com.applicationmodule.model.entity.Session;
import uz.com.applicationmodule.model.entity.User;

public interface SessionService {
    Session creaateSession(User user,String ip,String userAgent);
    Session getSessionByToken(String token);
}
