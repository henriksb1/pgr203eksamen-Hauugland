package no.kristiania.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryStringTest {
    @Test
    void shouldRetrieveQueryParameter(){
        QueryString queryString = new QueryString("status=200");
        assertEquals("200", queryString.getParameter("status"));

    }

    @Test
    void ShouldRetrieveOtherQueryParameter() {
        QueryString queryString = new QueryString("Status=404");
        assertEquals("404", queryString.getParameter("status"));
    }
}
