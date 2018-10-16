package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import server.model.Item;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Deque;
import java.util.TimeZone;

public class AppTest {

    private static final String REST_URI = "http://localhost:8080/roxana_items_sap_war_exploded/items";


    @Test
    public void shouldEnsureObjectCreated() throws IOException {
        insertItemWithId(1);
    }

    @Test
    public void shouldMatchFirstItem() throws IOException {
        insertItemWithId(900);

        final HttpUriRequest request = new HttpGet(REST_URI);

        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");
        mapper.setDateFormat(dateFormat);
        mapper.setTimeZone(TimeZone.getTimeZone("PDT"));

        Deque<Item> itemList = mapper.readValue(httpResponse.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(Deque.class, Item.class));

        assertEquals(itemList.getFirst().getId(), 900);
    }

    @Test
    public void shouldMatchInsertedObjectSize() throws IOException {
        for (int i = 2; i < 52; i++) {
            insertItemWithId(i);
        }

        final HttpUriRequest request = new HttpGet(REST_URI);

        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");
        mapper.setDateFormat(dateFormat);
        mapper.setTimeZone(TimeZone.getTimeZone("PDT"));

        Deque<Item> itemList = mapper.readValue(httpResponse.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(Deque.class, Item.class));

        assertEquals(itemList.size(), 52);
    }

    @Test
    public void shouldMatchObjectsCreatedInLastTwoSeconds() throws IOException, InterruptedException {
        Thread.sleep(2000);

        for (int i = 60; i < 200; ++i) {
            insertItemWithId(i);
        }

        final HttpUriRequest request = new HttpGet(REST_URI);

        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");
        mapper.setDateFormat(dateFormat);
        mapper.setTimeZone(TimeZone.getTimeZone("PDT"));

        Deque<Item> itemList = mapper.readValue(httpResponse.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(Deque.class, Item.class));

        assertEquals(itemList.size() > 100, true);
    }

    @Test
    public void shouldExpireObjectsAfterTwoSeconds() throws IOException, InterruptedException {
        Thread.sleep(2000);

        final HttpUriRequest request = new HttpGet(REST_URI);

        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");
        mapper.setDateFormat(dateFormat);
        mapper.setTimeZone(TimeZone.getTimeZone("PDT"));

        Deque<Item> itemList = mapper.readValue(httpResponse.getEntity().getContent(), mapper.getTypeFactory().constructCollectionType(Deque.class, Item.class));

        assertEquals(itemList.size(), 100);
    }

    public void insertItemWithId(int id) throws IOException {
        Date currentDate = new Date();
        Timestamp timestamp = new Timestamp((currentDate.getTime()));
        Item item = new Item(id, timestamp);

        final HttpPost request = new HttpPost(REST_URI);

        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSXXX");
        mapper.setDateFormat(dateFormat);
        mapper.setTimeZone(TimeZone.getTimeZone("PDT"));

        String itemJson = mapper.writeValueAsString(item);
        StringEntity input = new StringEntity(itemJson);
        input.setContentType("application/json");
        request.setEntity(input);

        final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(httpResponse.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
    }
}
