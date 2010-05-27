package controllers.crud;

import controllers.CRUD;
import controllers.Secure;
import controllers.CRUD.For;
import play.mvc.With;
import models.Customer;

@CRUD.For(Customer.class)
@With(Secure.class)
public class Customers extends CRUD {

}
