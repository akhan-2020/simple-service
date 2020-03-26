package simple.service;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Retryable;
import io.reactivex.Single;

//@Retryable(delay = "5s")
@Client("http://localhost:8181")
public interface CustomerDAOService {

    @Post("/customer")
    Single<Person> save(String firstName, String lastName, String phone);

    @Get("/customer/phone/{phone}")
    Single<Person> getByPhone(@PathVariable String phone);
}
