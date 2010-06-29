package controllers;

import play.Play;
import play.Play.Mode;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.results.Forbidden;

public class ServiceTests extends Controller {

    @Before
    public void checkEnvironment() {
        if(!(Play.mode == Mode.DEV || Play.id.equals("test")))
            new Forbidden("Wrong mode or id.");
    }

    public static void wayServices() {
        render();
    }

}
