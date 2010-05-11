package controllers;

import play.mvc.With;
import models.Customer;

@CRUD.For(Customer.class)
@With(Secure.class)
public class Customers extends CRUD {

}
