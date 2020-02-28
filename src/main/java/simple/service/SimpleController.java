package simple.service;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


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

    @Get("/{delay}")
    public String index(@PathVariable String delay) {

        System.out.println("Calling httpbin org api using delay of " + delay);
        String response = client.toBlocking().retrieve(HttpRequest.GET("/delay/" + delay));

        logger.info("response from httpbin delay {} ", response);
        return "Hello World";
    }

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Post
    public Single<Person> save(@Body Person person) {
        System.out.println("inserting customer " + person.getFirstName());

       // Person response = client.toBlocking().retrieve(HttpRequest.POST("/customer", person));
        return daoSvc.save(person.getFirstName(), person.getLastName(), person.getPhone());
    }


}
