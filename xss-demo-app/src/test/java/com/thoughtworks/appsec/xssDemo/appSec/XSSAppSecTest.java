package com.thoughtworks.appsec.xssDemo.appSec;

import com.thoughtworks.appsec.xssDemo.utils.GuestBookClient;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class XSSAppSecTest {

    private GuestBookClient client = new GuestBookClient();

    @Before
    public void setup() {
        client.waitForPing();
        client.clearEntries();
    }

    @Test
    public void testDoesBasicHTMLEncoding() {
        client.postEntry("<b>Hello</b>");
        assertThat(entryExistsWithText("&lt;b&gt;Hello&lt;/b&gt;"), is(true));
    }


    @Test
    public void testHTMLEncodesJavascriptTag() {
        client.postEntry("<script>alert('hello');</script>");
        assertThat(entryExistsWithText("&lt;script&gt;alert('hello');&lt;/script&gt;"), is(true));

    }

    private boolean entryExistsWithText(String text) {
        for (GuestBookClient.Entry entry : client.getEntries().getFound()) {
            if (entry.getContents().equals(text)) {
                return true;
            }
        }
        return false;
    }
}
