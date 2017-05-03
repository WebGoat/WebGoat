import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.apache.commons.lang3.RandomStringUtils

import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpConf = http
    .baseURL("http://localhost:8080/WebGoat/") // Here is the root for all relative URLs
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("Register and automatic login").
    exec(session =>
      session.setAll(("username", RandomStringUtils.randomAlphabetic(10)))
    )
    .exec(
      http("Test")
        .post("register.mvc")
        .formParam("username", "${username}")
        .formParam("password", "${username}")
        .formParam("matchingPassword", "${username}")
        .formParam("agree", "agree")
    )

  setUp(scn.inject(atOnceUsers(100)).protocols(httpConf))
}