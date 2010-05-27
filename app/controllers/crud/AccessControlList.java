package controllers.crud;

import controllers.CRUD;
import controllers.Secure;
import controllers.CRUD.For;
import play.mvc.With;
import models.AccessControl;

@CRUD.For(AccessControl.class)
@With(Secure.class)
public class AccessControlList extends CRUD {
}
