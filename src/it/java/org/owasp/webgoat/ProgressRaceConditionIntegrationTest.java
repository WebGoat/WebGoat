package org.owasp.webgoat;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProgressRaceConditionIntegrationTest extends IntegrationTest {

    @Test
    public void runTests() throws InterruptedException {
    	int NUMBER_OF_CALLS = 40;
    	int NUMBER_OF_PARALLEL_THREADS = 5;
        startLesson("Challenge1");

        Callable<Response> call = () -> {
        		//System.out.println("thread "+Thread.currentThread().getName());
                return RestAssured.given()
                        .when()
                        .relaxedHTTPSValidation()
                        .cookie("JSESSIONID", getWebGoatCookie())
                        .formParams(Map.of("flag", "test"))
                        .post(url("/challenge/flag/"));

        };
        ExecutorService executorService = Executors.newWorkStealingPool(NUMBER_OF_PARALLEL_THREADS);
        List<? extends Callable<Response>> flagCalls = IntStream.range(0, NUMBER_OF_CALLS).mapToObj(i -> call).collect(Collectors.toList());
        var responses = executorService.invokeAll(flagCalls);

        //A certain amount of parallel calls should fail as optimistic locking in DB is applied
        long countStatusCode500 = responses.stream().filter(r -> {
            try {
            	//System.err.println(r.get().getStatusCode());
                return r.get().getStatusCode() != 200;
            } catch (InterruptedException | ExecutionException e) {
            	//System.err.println(e);
                throw new IllegalStateException(e);
            }
        }).count();
        System.err.println("counted status 500: "+countStatusCode500);
        Assertions.assertThat(countStatusCode500).isLessThanOrEqualTo((NUMBER_OF_CALLS - (NUMBER_OF_CALLS/NUMBER_OF_PARALLEL_THREADS)));
    }
}
