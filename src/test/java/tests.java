import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import srver.PO;
import srver.server;
import srver.vulnerability;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static srver.server.readJsonFromUrl;


/**
 * Created by Ксения on 12.12.2016.
 */

public class tests {
    public vulnerability vuln;




    @Before
    public void before() {
        server s = new server();
       /* vuln =new vulnerability("lastseen", "references", "description", "edition", "published",
               "title", "type", "objectVersion",  "bulletinFamily",
               "cvelist", "modified", "href", "id", "score");*/


    }

    @Test
    public void testreadJsonFromUrl() throws IOException, JSONException {

        assertNotNull(readJsonFromUrl("https://vulners.com/api/v3/search/lucene/?query=type:centos%20cvss.score:[8%20TO%2010]%20order:published"));

    }

    @Test
    public void getNamePO() throws IOException, JSONException {
        PO po = new PO("key", "POname", "POvershion");
        assertEquals("POname", po.getName());

    }

    @Test
    public void getVersionPO() throws IOException, JSONException {
        PO po = new PO("key", "POname", "POvershion");
        assertEquals("POvershion", po.getVersion());

    }

    @Test
    public void testgetLastseen() {

        assertEquals("lastseen", vuln.getLastseen());

    }
    @Test
    public void testgetReferences() {

        assertEquals("references", vuln.getReferences());

    }
    @Test
    public void testgetdescription() {

        assertEquals("description", vuln.getDescription());

    }

    @Test
    public void testgetedition() {

        assertEquals("edition", vuln.getEdition());

    }@Test
    public void testgetpublished() {

        assertEquals("published", vuln.getPublished());

    }@Test
    public void testgettitle() {

        assertEquals("title", vuln.getTitle());

    }
    @Test
    public void testgetobjectVersion() {

        assertEquals("objectVersion", vuln.getObjectVersion());

    }
    @Test
    public void bulletinFamily() {

        assertEquals("bulletinFamily", vuln.getBulletinFamily());

    }
    @Test
    public void testgetcvelist() {

        assertEquals("cvelist", vuln.getCvelist());

    } @Test
    public void testgettype() {

        assertEquals("type", vuln.getType());

    }

    @Test
    public void testgetmodified() {

        assertEquals("modified", vuln.getModified());

    } @Test
    public void testgethref() {

        assertEquals("href", vuln.getHref());

    } @Test
    public void testgetscore() {

        assertEquals("score", vuln.getScore());

    }


}


