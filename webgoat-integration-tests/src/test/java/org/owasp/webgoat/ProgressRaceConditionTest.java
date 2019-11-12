package org.owasp.webgoat;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProgressRaceConditionTest extends IntegrationTest {

    @Test
    public void runTests() throws InterruptedException {
        startLesson("Challenge1");

        Callable<Response> call = () ->
                RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .formParams(Map.of("flag", "test"))
                        .post(url("/challenge/flag/"));
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        List<? extends Callable<Response>> flagCalls = IntStream.range(0, 20).mapToObj(i -> call).collect(Collectors.toList());
        var responses = executorService.invokeAll(flagCalls);

        //A certain amount of parallel calls should fail as optimistic locking in DB is applied
        Assertions.assertThat(responses.stream().filter(r -> {
            try {
                return r.get().getStatusCode() == 500;
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }).count()).isGreaterThan(8);
    }
}
