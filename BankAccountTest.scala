import _root_.io.gatling.core.scenario.Simulation
import ch.qos.logback.classic.{Level, LoggerContext}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.slf4j.LoggerFactory

import scala.concurrent.duration._

/**
 * Performance test for the Foo entity.
 */
class BankAccountTest extends Simulation {

    val context: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    // Log all HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("TRACE"))
    // Log failed HTTP requests
    //context.getLogger("io.gatling.http").setLevel(Level.valueOf("DEBUG"))

    val baseURL = Option(System.getProperty("baseURL")) getOrElse """http://localhost:8080"""

    val httpConf = http
        .baseURL(baseURL)

    val headers_http = Map(
        "Accept" -> """application/json"""
    )

    val headers_http_authentication = Map(
        "Content-Type" -> """application/json""",
        "Accept" -> """application/json"""
    )

    val headers_http_authenticated = Map(
        "Accept" -> """application/json""",
        "Authorization" -> "${access_token}"
    )

    val scn = scenario("Test the BankAccount entity")
        .exec(http("First unauthenticated request")
        .get("/api/account")
        .headers(headers_http)
        .check(status.is(401)))
        .pause(10)
        .exec(http("Authentication")
        .post("/api/authenticate")
        .headers(headers_http_authentication)
        .body(StringBody("""{"username":"admin", "password":"admin"}""")).asJSON
        .check(header.get("Authorization").saveAs("access_token"))).exitHereIfFailed
        .doIf("${access_token.exists()}") {
            pause(5)
            .exec(http("Authenticated request")
            .get("/api/account")
            .headers(headers_http_authenticated)
            .check(status.is(200)))
            .pause(10)
            .repeat(2) {
                exec(http("Get all bankaccounts")
                .get("/accountsmicroservice/api/bankaccounts")
                .headers(headers_http_authenticated)
                .check(status.is(200)))
                .pause(5)
                .exec(http("Create new bankaccount")
                .post("/accountsmicroservice/api/bankaccounts")
                .headers(headers_http_authenticated)
                .body(StringBody("""{"id":null, "clientname":"SAMPLE_TEXT", "balance":"10"}""")).asJSON
                .check(status.is(201))
                .check(header("Location").saveAs("new_foo_url"))).exitHereIfFailed
                .pause(5)
                .doIf("${new_foo_url.exists()}") {
                    repeat(5) {
                        exec(http("Get created bankaccount")
                        .get("/accountsmicroservice${new_foo_url}")
                        .headers(headers_http_authenticated))
                        .pause(10)
                    }
                    .exec(http("Delete created bankaccount")
                    .delete("/accountsmicroservice${new_foo_url}")
                    .headers(headers_http_authenticated))
                    .pause(10)
                }
            }
        }


    val users = scenario("Users").exec(scn)

    setUp(
        users.inject(rampUsers(2000) over (2 minutes))
    ).protocols(httpConf)
}

