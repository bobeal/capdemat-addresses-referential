package controllers;

import java.util.Date;
import models.AccessControl;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

public class Services extends Controller{

    @Before
    static void checkAccess() {
        String token = (String) params.get("token");
        String IP = request.remoteAddress;
        if(token==null) {
            response.status = 400;
            renderJSON("{message: \"The parameter 'token' is required.\"}");
        }
        Logger.debug("Token: %s, IP: %s", token, IP);
        AccessControl ac = AccessControl.find("IP=? and token=? and expirationDate > ?", IP, token, new Date()).first();
        if(ac==null) {
            response.status = 400;
            renderJSON("{message: \"The token is not valid for this IP.\"}");
        }
    }

}
