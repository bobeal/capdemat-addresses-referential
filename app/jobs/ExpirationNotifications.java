package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;

import com.sun.tools.doclets.internal.toolkit.Configuration;

import controllers.Notifications;

import models.AccessControl;
import models.ExpirationNotification;

import play.Logger;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import play.jobs.OnApplicationStart;

//Every day at 5am
@On("0 0 5 * * ?")
public class ExpirationNotifications extends Job<Void> {

    public List<Integer> steps = new ArrayList<Integer>();

    public ExpirationNotifications() {
        String stepsConfig = Play.configuration.getProperty("mail.expiration.steps");
        if(stepsConfig != null) {
            for(String strSteps : stepsConfig.split(",")) {
                try {
                    steps.add(Integer.parseInt(strSteps));
                }
                catch (NumberFormatException e) { }
            }
        }
    }

    @Override
    public void doJob() {
        Map<Integer, AccessControl> expireAclByStep = new HashMap<Integer, AccessControl>();
        DateTime now = new DateTime();
        List<AccessControl> expiredAcl = AccessControl.find("expirationDate > ? and expirationDate < ?", now.toDate(), now.plusDays(30).toDate()).fetch();
        for(AccessControl expireAc : expiredAcl) {
            Double daysLeft = Math.ceil((double) (expireAc.expirationDate.getTime() - now.toDate().getTime()) / (24 * 3600 * 1000.));
            for(int i=steps.size()-1; i>=0; i--) {
                if(daysLeft <= steps.get(i)) {
                    expireAclByStep.put(steps.get(i), expireAc);
                    break;
                }
            }
        }
        for(Entry<Integer, AccessControl> aclEntry : expireAclByStep.entrySet()) {
            ExpirationNotification notification = new ExpirationNotification(aclEntry.getKey(), aclEntry.getValue());
            if(!ExpirationNotification.exists(notification)) {
                Notifications.expiration(notification);
                notification.save();
            }
        }
    }

}
