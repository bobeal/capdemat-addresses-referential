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
        AccessControl accessControl = AccessControl.find("IP=? and token=? and expirationDate > ?", IP, token, new Date()).first();
        if(accessControl == null) {
            response.status = 400;
            renderJSON("{message: \"The token is not valid for this IP.\"}");
        }
        renderArgs.put("accessControl", accessControl);
    }

    static AccessControl getAccessControl() {
        return (AccessControl) renderArgs.get("accessControl");
    }

    public static void tokenValidity() {
        renderJSON("{message: \"This token is valid for this IP.\"}");
    }

}
