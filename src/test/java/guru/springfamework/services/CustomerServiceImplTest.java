package guru.springfamework.services;

import guru.springfamework.api.v1.model.CustomerDto;
import guru.springfamework.api.v1.model.mapper.CustomerMapper;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    private CustomerServiceImpl customerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, customerMapper);
    }

    @Test
    public void getAllCustomers() {
        //given
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setFirstName("Jack");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setFirstName("Johnny");
        List<Customer> customers = Arrays.asList(customer1, customer2);

        //when
        when(customerRepository.findAll()).thenReturn(customers);

        //then
        List<CustomerDto> customerDtoList = customerService.getAllCustomers();
        assertEquals(customers.size(), customerDtoList.size());
    }

    @Test
    public void getCustomerById() {
        Long id = 1L;
        //given
        Customer customer1 = new Customer();
        customer1.setId(id);
        customer1.setFirstName("Jack");

        //when
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer1));

        //then
        CustomerDto customerDto = customerService.getCustomerById(id);
        assertEquals(id, customerDto.getId());

    }
}