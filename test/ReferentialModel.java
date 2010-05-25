import java.util.List;

import models.City;
import models.Referential;
import models.Way;

import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.modules.search.Search;
import play.test.Fixtures;
import play.test.UnitTest;


public class ReferentialModel extends UnitTest {

    @Before
    public void clean() {
        Fixtures.deleteAll();
        Fixtures.load("data.yml");

        try {
            Search.rebuildAllIndexes();
        } catch (Exception e) {
            Logger.info("%s", e.getCause());
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void refenrentialCodeIndexation() {
        try {
            List<City> cities = Search.search("referential:TEST", City.class).fetch();
            assertNotNull(cities);
            assertEquals(2, cities.size());

            cities = Search.search("referential:TEST AND name:PLESSIS", City.class).fetch();
            assertEquals(1, cities.size());
            assertEquals("94420", cities.get(0).postalCode);

            cities = Search.search("referential:HEXAVIA", City.class).fetch();
            assertEquals(1, cities.size());
            assertEquals("75001", cities.get(0).postalCode);

            cities = Search.search("referential:BIDULE", City.class).fetch();
            assertEquals(0, cities.size());

            List<Way> ways = Search.search("referential:HEXAVIA", Way.class).fetch();
            assertEquals(2, ways.size());
        }
        catch(Exception e) {
            Logger.info("%s", e.getCause());
            e.printStackTrace();
            fail();
        }
    }

}
