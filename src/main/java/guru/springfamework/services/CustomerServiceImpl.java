package guru.springfamework.services;

import guru.springfamework.api.v1.model.CustomerDto;
import guru.springfamework.api.v1.model.mapper.CustomerMapper;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        return customerMapper.customerToCustomerDTO(
                customerRepository.findById(id)
                        .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public CustomerDto createCustomer(CustomerDto customer) {
        return saveCustomerDto(customerMapper.customerDtoToCustomer(customer));
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        customer.setId(id);
        return saveCustomerDto(customer);
    }

    private CustomerDto saveCustomerDto(Customer customer) {
        Customer saved= customerRepository.save(customer);
        CustomerDto savedDto = customerMapper.customerToCustomerDTO(saved);
        savedDto.setCustomerUrl("/api/v1/customer/" + saved.getId());
        return savedDto;
    }
}
