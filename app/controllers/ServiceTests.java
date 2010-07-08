package controllers;

import models.AccessControl;
import models.City;
import models.Referential;
import models.Way;
import models.AccessControl.Level;

import org.joda.time.DateTime;

import play.Play;
import play.Play.Mode;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import play.mvc.results.Forbidden;

@With(Secure.class)
public class ServiceTests extends Controller {

    @Before
    public void checkEnvironment() {
        if(Play.id.equals("test"))
            new Forbidden("Run the application in test mode.");
    }

    public static void wayServices() {
        render();
    }

}
