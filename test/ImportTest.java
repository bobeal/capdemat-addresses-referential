import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import models.Import;
import models.Referential;

import play.Logger;
import play.Play;
import play.modules.search.Search;
import play.mvc.Before;
import play.test.Fixtures;
import play.test.UnitTest;

public class ImportTest extends UnitTest {

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
    public void fileExists() {
        File csvFile1 = Play.getFile("/test/csvfile1.csv");
        File csvFile1_copy = Play.getFile("/test/csvfile1_copy.csv");
        File csvFile2 = Play.getFile("/test/csvfile2.csv");
        assertNotNull(csvFile1);
        assertNotNull(csvFile1_copy);
        assertNotNull(csvFile2);

        Referential referential = Referential.find("code = ?", "HEXAVIA").first();
        assertNotNull(referential);

        try {
            Import import1 = Import.queue(referential, csvFile1, Import.Type.CITY);
            assertNotNull(import1);
            assertTrue(Import.fileExists(csvFile1_copy));
            assertFalse(Import.fileExists(csvFile2));
        } catch (Exception e) {
            Logger.debug("%s", e.getCause());
            fail();
        }
    }

}
