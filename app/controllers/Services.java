package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.AccessControl;
import play.Logger;
import play.data.validation.Validation;
import play.i18n.Messages;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.results.Forbidden;

public class Services extends Controller {

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

    static void notFoundJSONIfNull(Object object, String message) {
        if(object==null) {
            response.status = 404;
            renderJSON("{message:\"" + Messages.get(message) + "\"}");
        }
    }

    static void validationJSON() {
        if(Validation.hasErrors()) {
            response.status = 400;
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("message", Messages.get("validErrors"));
            jsonMap.put("errors", Validation.errors());
            renderJSON(jsonMap);
        }
    }

    static void writer() {
        if(getAccessControl().level != AccessControl.Level.WRITE) {
            forbidden();
        }
    }

    public static void tokenValidity() {
        renderJSON("{message: \"This token is valid for this IP.\"}");
    }

}
