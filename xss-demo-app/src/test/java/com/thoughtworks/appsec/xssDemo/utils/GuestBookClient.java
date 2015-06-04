package com.thoughtworks.appsec.xssDemo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.thoughtworks.appsec.xssDemo.TestException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Component
public class GuestBookClient {

    /** TODO: refactor this ***/
    private String root;

    public GuestBookClient() {
        this("http://localhost:8080");
    }

    public GuestBookClient(String appRoot) {
        this.root = appRoot;
    }

    public GuestBookClient waitForPing() {

        waitFor(new Supplier<Boolean>(){
            @Override
            public Boolean get() {
                return ping().equals(Optional.of(200));
            }
        });
        return this;
    }

    private void waitFor(Supplier<Boolean> test) {
        waitFor(test, 5000);
    }

    private void waitFor(Supplier<Boolean> test, long maxWait) {
        long timeout = maxWait + System.currentTimeMillis();
        while (System.currentTimeMillis() < timeout) {
            if (test.get()) {
                return;
            }
        }
        throw new TestException("Timed out waiting for condition.");
    }

    private Optional<Integer> ping() {
        HttpGet get = new HttpGet(root + "/health");
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final CloseableHttpResponse response = client.execute(get);
            return Optional.of(response.getStatusLine().getStatusCode());
        } catch (IOException e) {
            return Optional.absent();
        }
    }

    public GuestBookClient postEntry(final String text) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost(root + "/service/entries");
            post.setEntity(new UrlEncodedFormEntity(ImmutableList.of(new BasicNameValuePair("content", text))));
            final CloseableHttpResponse response = client.execute(post);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException(String.format("Failed with status message: %s", response.getStatusLine().getReasonPhrase()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return this;
    }


    public EntryResult getEntries() {
        return doHttpRequest(new HttpGet(root + "/service/entries"), new Function<CloseableHttpResponse, EntryResult>(){
            @Override
            public EntryResult apply(final CloseableHttpResponse response) {
                checkResponse(response);
                try {
                    return new ObjectMapper().readValue(response.getEntity().getContent(), EntryResult.class);
                } catch (IOException e) {
                    throw new TestException("Failed to fetch entries.", e);
                }
            }
        });


    }

    public GuestBookClient clearEntries() {
        BasicCookieStore store = new BasicCookieStore();
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCookieStore(store).build()) {
            checkResponse(client.execute(createLoginPost()));
            checkResponse(client.execute(new HttpDelete(String.format("%s/service/entries/", root))));
        } catch (IOException e) {
            throw new TestException("Failed to clear entries.", e);
        }
        return this;
    }

    public void deleteAllEntriesViaPost() {
        BasicCookieStore store = new BasicCookieStore();
        try (CloseableHttpClient client = HttpClientBuilder.create().setDefaultCookieStore(store).build()) {
            checkResponse(client.execute(createLoginPost()));
            client.execute(new HttpPost(String.format("%s/service/deleteEntries/", root)));
        } catch (IOException e) {
            throw new TestException("Failed to delete entries.", e);
        }
    }

    private void checkResponse(final CloseableHttpResponse response) {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new TestException(String.format("Failed to delete: %s",
                    response.getStatusLine().getReasonPhrase()));
        }
    }

    public HttpUriRequest createLoginPost() throws UnsupportedEncodingException {
        String username = System.getProperty("app.admin.username", "testuser");
        String password = System.getProperty("app.admin.password", "testpassword");
        HttpPost post = new HttpPost(String.format("%s/service/login", root));
        post.setEntity(new UrlEncodedFormEntity(ImmutableList.of(
                new BasicNameValuePair("username", username),
                new BasicNameValuePair("password", password))));
        return post;
    }

    public <T> T doHttpRequest(HttpRequestBase request, Function<CloseableHttpResponse, T> handler) {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            final CloseableHttpResponse response = client.execute(request);

            return handler.apply(response);

        } catch (IOException e) {
            throw new TestException("Failed to clean entries.", e);
        }
    }

    public static class EntryResult {
        private List<Entry> found;
        private String filter;

        public List<Entry> getFound() {
            return found;
        }

        public void setFound(List<Entry> found) {
            this.found = found;
        }

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }
    }

    public static class Entry {
        private String contents;

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "contents='" + contents + '\'' +
                    '}';
        }
    }

}
