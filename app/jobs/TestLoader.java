package jobs;

import org.joda.time.DateTime;

import models.AccessControl;
import models.City;
import models.Referential;
import models.Way;
import models.AccessControl.Level;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class TestLoader extends Job {

    @Override
    public void doJob() {
        if(Play.id.equals("test")) {
            Referential testHexavia = Referential.findByCode("TESTHEX");
            Referential testRivoli = Referential.findByCode("TESTRIVOLI");
            if(testHexavia == null) {
                testHexavia = new Referential("TESTHEX", "Test Hexavia");
                testHexavia.save();
            }
            if(testRivoli == null) {
                testRivoli = new Referential("TESTRIVOLI", "Test Rivoli");
                testRivoli.save();
            }
            AccessControl acHexavia = AccessControl.find("referential = ?", testHexavia).first();
            AccessControl acRivoli = AccessControl.find("referential = ?", testRivoli).first();
            if(acHexavia == null) {
                acHexavia = new AccessControl();
                acHexavia.domainName = "test.hexavia.fr";
                acHexavia.token = "0123456789ABCDEF";
                acHexavia.IP = "0:0:0:0:0:0:0:1%0";
                acHexavia.expirationDate = new DateTime().withDayOfMonth(30).withMonthOfYear(10).toDate();
                acHexavia.referential = testHexavia;
                acHexavia.level = Level.WRITE;
                acHexavia.save();
            }
            if(acRivoli == null) {
                acRivoli = new AccessControl();
                acRivoli.domainName = "test.rivoli.fr";
                acRivoli.token = "ABCDEF0123456789";
                acRivoli.IP = "0:0:0:0:0:0:0:1%0";
                acRivoli.expirationDate = new DateTime().withDayOfMonth(30).withMonthOfYear(10).toDate();
                acRivoli.referential = testRivoli;
                acRivoli.level = Level.WRITE;
                acRivoli.save();
            }
            City city = City.find("referential = ? AND inseeCode = ?", testRivoli, "94059").first();
            Way way = Way.find("referential = ? AND rivoliCode = ?", testRivoli, "1234567890").first();
            if(city == null) {
                city = new City();
                city.name = "LE PLESSIS TREVISE";
                city.inseeCode = "94059";
                city.postalCode = "94420";
                city.referential = testRivoli;
                city.save();
            }
            if(way == null) {
                way = new Way();
                way.cityInseeCode = "94059";
                way.rivoliCode = "1234567890";
                way.name = "AVENUE ARDOUIN RIVOLI";
                way.referential = testRivoli;
                way.save();
            }
        }
    }

}
