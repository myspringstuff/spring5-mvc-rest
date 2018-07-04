package guru.springfamework.bootstrap;

import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Bootstrap implements CommandLineRunner {
    private final CustomerRepository customerRepository;

    public Bootstrap(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        initCustomers();
    }

    private void initCustomers() {
        Customer customer1 = new Customer();
        customer1.setFirstName("John");
        customer1.setLastName("Smith");

        Customer customer2 = new Customer();
        customer2.setFirstName("Foo");
        customer2.setLastName("bar");

        customerRepository.saveAll(Arrays.asList(customer1, customer2));
    }
}
