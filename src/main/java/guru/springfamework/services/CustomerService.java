package guru.springfamework.services;

import guru.springfamework.api.v1.model.CustomerDto;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> getAllCustomers();

    CustomerDto getCustomerById(Long id);

    CustomerDto createCustomer(CustomerDto customer);

    CustomerDto updateCustomer(Long id, CustomerDto customer);

}
