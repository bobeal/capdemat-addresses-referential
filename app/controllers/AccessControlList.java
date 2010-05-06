package controllers;

import play.mvc.With;
import models.AccessControl;

@CRUD.For(AccessControl.class)
@With(Secure.class)
public class AccessControlList extends CRUD {
}
