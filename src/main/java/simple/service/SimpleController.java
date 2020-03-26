package simple.service;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;


//@Controller("/customer/{delay}")
@Controller("/customer")
public class SimpleController {


    @Inject
    @Client("http://httpbin.org")
    HttpClient client;

    private final CustomerDAOService daoSvc;

    public SimpleController(CustomerDAOService custSvc) {
        this.daoSvc = custSvc;
    }

    private static final Logger logger
            = LoggerFactory.getLogger(SimpleController.class);


    @Get("/delay/{delay}")
    public String delay(@PathVariable String delay) {
        logger.info("Calling httpbin org api using delay of " + delay);
        String response = client.toBlocking().retrieve(HttpRequest.GET("/delay/" + delay));
        logger.info("response from httpbin delay {} ", response);
        return "Hello World from httpbin " + delay;
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Post
    public Single<Person> save(@Body Person person) {
        logger.info("writing customer to MongoDB " + person.getPhone());
        System.out.println("inserting customer " + person.getFirstName());
        return daoSvc.save(person.getFirstName(), person.getLastName(), person.getPhone());
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Get("/phone/{phone}")
   // public Single<Person>index(@PathVariable @NotBlank String phone) {
    public HttpResponse index(@PathVariable @NotBlank String phone) {

        try {
            logger.info("Searching customer by phone " + phone);
            Single<Person> p = daoSvc.getByPhone(phone);
            logger.info("Finsihed searching customer by phone " + phone);
            return HttpResponse.ok(p.blockingGet());
        } catch (Exception e) {
            logger.error("Error searching customer by phone " + e.getMessage());
            Map<String, Object> m = new HashMap<>();
            m.put("status", 400);
            m.put("error", "Invalid request data");
            return HttpResponse.status(HttpStatus.BAD_REQUEST).body(m);
        }
    }



}
