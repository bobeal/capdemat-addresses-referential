package controllers;

import models.*;
import play.*;

public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
        return Play.configuration.getProperty("referential.admin.user","").equals(username)
            && Play.configuration.getProperty("referential.admin.password","").equals(password);
    }

}