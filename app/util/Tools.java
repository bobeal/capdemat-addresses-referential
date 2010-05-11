package util;

import java.util.Date;

import play.i18n.Messages;
import play.templates.JavaExtensions;

public class Tools extends JavaExtensions {

    public static Integer daysLeftNumber(Long timeLeft) {
        Double dayLeft = (double) Math.ceil(timeLeft / (24 *  3600 * 1000.));
        return dayLeft.intValue();
    }

    public static Integer daysLeftNumber(Date endDate) {
        return daysLeftNumber(endDate.getTime() - new Date().getTime());
    }

    public static String daysLeft(Long timeLeft) {
        return daysLeftNumber(timeLeft) + " " + Messages.get("days");
    }

    public static String daysLeft(Date endDate) {
        return daysLeftNumber(endDate) + " " + Messages.get("days");
    }

}
