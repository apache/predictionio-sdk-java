package io.prediction;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsArray.array;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Client}.
 *
 * @author support@prediction.io (The PredictionIO Team)
 */
@RunWith(JUnit4.class)
public class ClientTest {

    String appkey = "validkey";
    int apiPort = 5784;
    String apiURL = "http://localhost:" + apiPort;
    Client client;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(apiPort);

    @Before
    public void setUp() {
        client = new Client(appkey, apiURL);
    }

    @After
    public void after() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    public void getStatus() {
        stubFor(get(urlEqualTo("/"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/plain")
                .withBody("online")));

        try {
            assertThat(client.getStatus(), is("online"));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createUser() {
        String url = "/users.json";
        stubFor(post(urlEqualTo(url))
              .willReturn(aResponse().withStatus(201)));

        try {
            client.createUser("foo");
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_uid\":\"foo\"")));

        try {
            client.createUser(client.getCreateUserRequestBuilder("bar"));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_uid\":\"bar\"")));
    }

    @Test
    public void getUser() {
        try {
            User fooUser = client.getUser("baz");
            assertThat(fooUser.getUid(), is("baz"));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
    @Test
    public void deleteUser() {
        stubFor(delete(urlEqualTo("/users/beef.json"))
                .willReturn(aResponse().withStatus(200)));

        try {
            client.deleteUser("beef");
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            System.err.println("yo");
            System.err.println(e);
            fail(e.getMessage());
        }
    }
    */

    @Test
    public void createItem() {
        String url = "/items.json";
        stubFor(post(urlEqualTo(url))
                .willReturn(aResponse().withStatus(201)));

        try {
            client.createItem("foo", new String[]{"bar", "baz"});
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_iid\":\"foo\""))
               .withRequestBody(containing("\"pio_itypes\":\"bar,baz\"")));

        try {
            client.createItem(client.getCreateItemRequestBuilder("bar", new String[]{"dead", "beef"}));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_iid\":\"bar\""))
               .withRequestBody(containing("\"pio_itypes\":\"dead,beef\"")));
    }

    @Test
    public void getItem() {
        try {
            Item item = client.getItem("beef");
            assertThat(item.getIid(), is("beef"));
            assertThat(item.getItypes(), is(array(equalTo("foo"), equalTo("bar"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getItemRecTopNWithoutIdentity() {
        try {
            client.getItemRecTopN("greatengine", 5);
            fail("Should have thrown UnidentifiedUserException");
        } catch (UnidentifiedUserException e) {
            // Expected
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getItemRecTopN() {
        try {
            client.identify("foo");
            String[] items = client.getItemRecTopN("greatengine", 5);
            assertThat(items.length, is(5));
            assertThat(items, is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
        } catch (UnidentifiedUserException e) {
            fail(e.getMessage());
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            String[] items = client.getItemRecTopN("greatengine", "foo", 5);
            assertThat(items.length, is(5));
            assertThat(items, is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            client.identify("foo");
            String[] items = client.getItemRecTopN(client.getItemRecGetTopNRequestBuilder("greatengine", 5));
            assertThat(items.length, is(5));
            assertThat(items, is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
        } catch (UnidentifiedUserException e) {
            fail(e.getMessage());
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            String[] items = client.getItemRecTopN(client.getItemRecGetTopNRequestBuilder("greatengine", "foo", 5));
            assertThat(items.length, is(5));
            assertThat(items, is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getItemRecTopNWithAttributes() {
        try {
            client.identify("foo");
            Map<String, String[]> items = client.getItemRecTopNWithAttributes("greatengineattr", 5, new String[]{"cost", "price"});
            assertThat(items.size(), is(3));
            assertThat(items.get("pio_iids").length, is(5));
            assertThat(items.get("cost").length, is(5));
            assertThat(items.get("price").length, is(5));
            assertThat(items.get("pio_iids"), is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
            assertThat(items.get("cost"), is(array(equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"), equalTo("1"))));
            assertThat(items.get("price"), is(array(equalTo("6"), equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"))));
        } catch (UnidentifiedUserException e) {
            fail(e.getMessage());
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            Map<String, String[]> items = client.getItemRecTopNWithAttributes("greatengineattr", "foo", 5, new String[]{"cost", "price"});
            assertThat(items.size(), is(3));
            assertThat(items.get("pio_iids").length, is(5));
            assertThat(items.get("cost").length, is(5));
            assertThat(items.get("price").length, is(5));
            assertThat(items.get("pio_iids"), is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
            assertThat(items.get("cost"), is(array(equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"), equalTo("1"))));
            assertThat(items.get("price"), is(array(equalTo("6"), equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            Map<String, String[]> items = client.getItemRecTopNWithAttributes(client.getItemRecGetTopNRequestBuilder("greatengineattr", "foo", 5, new String[]{"cost", "price"}));
            assertThat(items.size(), is(3));
            assertThat(items.get("pio_iids").length, is(5));
            assertThat(items.get("cost").length, is(5));
            assertThat(items.get("price").length, is(5));
            assertThat(items.get("pio_iids"), is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
            assertThat(items.get("cost"), is(array(equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"), equalTo("1"))));
            assertThat(items.get("price"), is(array(equalTo("6"), equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getItemSimTopN() {
        try {
            String[] items = client.getItemSimTopN("anothergreatengine", "foo", 5);
            assertThat(items.length, is(5));
            assertThat(items, is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            String[] items = client.getItemSimTopN(client.getItemSimGetTopNRequestBuilder("anothergreatengine", "foo", 5));
            assertThat(items.length, is(5));
            assertThat(items, is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void getItemSimTopNWithAttributes() {
        try {
            Map<String, String[]> items = client.getItemSimTopNWithAttributes("anothergreatengineattr", "foo", 5, new String[]{"cost", "price"});
            assertThat(items.size(), is(3));
            assertThat(items.get("pio_iids").length, is(5));
            assertThat(items.get("cost").length, is(5));
            assertThat(items.get("price").length, is(5));
            assertThat(items.get("pio_iids"), is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
            assertThat(items.get("cost"), is(array(equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"), equalTo("1"))));
            assertThat(items.get("price"), is(array(equalTo("6"), equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        try {
            Map<String, String[]> items = client.getItemSimTopNWithAttributes(client.getItemSimGetTopNRequestBuilder("anothergreatengineattr", "foo", 5, new String[]{"cost", "price"}));
            assertThat(items.size(), is(3));
            assertThat(items.get("pio_iids").length, is(5));
            assertThat(items.get("cost").length, is(5));
            assertThat(items.get("price").length, is(5));
            assertThat(items.get("pio_iids"), is(array(equalTo("baz"), equalTo("bar"), equalTo("foo"), equalTo("beef"), equalTo("dead"))));
            assertThat(items.get("cost"), is(array(equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"), equalTo("1"))));
            assertThat(items.get("price"), is(array(equalTo("6"), equalTo("5"), equalTo("4"), equalTo("3"), equalTo("2"))));
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void userActionItem() {
        String url = "/actions/u2i.json";
        stubFor(post(urlEqualTo(url))
                .willReturn(aResponse().withStatus(201)));

        try {
            client.identify("foo");
            client.userActionItem("view", "bar");
        } catch (UnidentifiedUserException e) {
            fail(e.getMessage());
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_uid\":\"foo\""))
               .withRequestBody(containing("\"pio_iid\":\"bar\""))
               .withRequestBody(containing("\"pio_action\":\"view\"")));

        try {
            client.userActionItem("foo", "view", "bar");
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_uid\":\"foo\""))
               .withRequestBody(containing("\"pio_iid\":\"bar\""))
               .withRequestBody(containing("\"pio_action\":\"view\"")));

        try {
            client.identify("foo");
            FutureAPIResponse r = client.userActionItemAsFuture("like", "bar");
            client.userActionItem(r);
        } catch (UnidentifiedUserException e) {
            fail(e.getMessage());
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_uid\":\"foo\""))
               .withRequestBody(containing("\"pio_iid\":\"bar\""))
               .withRequestBody(containing("\"pio_action\":\"like\"")));

        try {
            FutureAPIResponse r = client.userActionItemAsFuture("foo", "like", "bar");
            client.userActionItem(r);
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_uid\":\"foo\""))
               .withRequestBody(containing("\"pio_iid\":\"bar\""))
               .withRequestBody(containing("\"pio_action\":\"like\"")));

        try {
            UserActionItemRequestBuilder builder = client.getUserActionItemRequestBuilder("foo", "rate", "bar");
            builder.rate(4);
            client.userActionItem(builder);
        } catch (ExecutionException e) {
            fail(e.getMessage());
        } catch (InterruptedException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        }

        verify(postRequestedFor(urlMatching(url))
               .withRequestBody(containing("\"pio_appkey\":\"validkey\""))
               .withRequestBody(containing("\"pio_uid\":\"foo\""))
               .withRequestBody(containing("\"pio_iid\":\"bar\""))
               .withRequestBody(containing("\"pio_action\":\"rate\""))
               .withRequestBody(containing("\"pio_rate\":\"4\"")));
    }

}
