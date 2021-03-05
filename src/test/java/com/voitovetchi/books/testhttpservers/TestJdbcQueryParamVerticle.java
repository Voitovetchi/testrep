package com.voitovetchi.books.testhttpservers;

import com.Voitovetchi.books.httpServers.JdbcQueryParamVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import java.io.IOException;

@ExtendWith(VertxExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestJdbcQueryParamVerticle extends AbstractHttpServerTest{

  private final String isbnValue = "isbn=" + testBook.getLong("ISBN");

  @BeforeAll
  @Override
  public void setUp(VertxTestContext context) throws IOException {
    DeploymentOptions options = getDeploymentOptions();
    vertx.deployVerticle(JdbcQueryParamVerticle.class.getName(), options, context.succeeding(value -> context.checkpoint().flag()));
  }

  @Test
  @Order(5)
  @Timeout(5000)
  @Override
  public void testGetByIsbn(VertxTestContext context) {
    Future<Buffer> request = httpClient.request(HttpMethod.GET, port, "localhost", "/books/getByIsbn?" + isbnValue)
      .compose(req -> req.send().compose(HttpClientResponse::body));
    testGetByIsbnRequest(request, context);
  }

  @Test
  @Order(6)
  @Timeout(5000)
  @Override
  public void testUpdate(VertxTestContext context) {
    Future<Buffer> request = httpClient.request(HttpMethod.PUT, port, "localhost", "/books/updateByIsbn?" + isbnValue)
      .compose(req -> req.send(updatedTestBook.toString()).compose(HttpClientResponse::body));
    testUpdateRequest(request, context);
  }

  @Test
  @Order(7)
  @Timeout(5000)
  @Override
  public void testDelete(VertxTestContext context) {
    Future<Buffer> request = httpClient.request(HttpMethod.DELETE, port, "localhost", "/books/deleteByIsbn?" + isbnValue)
      .compose(req -> req.send().compose(HttpClientResponse::body));
    testDeleteRequest(request, context);
  }
}
