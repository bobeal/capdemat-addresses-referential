package controllers;

import models.Customer;
import models.ExpirationNotification;
import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.mvc.Mailer;
import play.mvc.Scope.Params;
import play.templates.JavaExtensions;
import util.Tools;

public class Notifications extends Mailer {

    private static void addRecipient(Customer customer) {
        if(customer.email == null)
            throw new NullPointerException();
        addRecipient((customer.name==null ? "" : customer.name + " ") + "<"+customer.email+">");
    }

    public static void expiration(ExpirationNotification notification) {
        setFrom(Play.configuration.getProperty("mail.sender"));
        addRecipient(notification.accessControl.customer);
        setSubject(Messages.get("mail.expiration.title", Tools.daysLeftNumber(notification.expirationDate)));
        send(notification);
    }

}
