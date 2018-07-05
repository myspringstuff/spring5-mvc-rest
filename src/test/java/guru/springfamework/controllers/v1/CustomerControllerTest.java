package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDto;
import guru.springfamework.controllers.AbstractRestControllerTest;
import guru.springfamework.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest extends AbstractRestControllerTest {

    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CustomerController customerController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void findAllCustomers() throws Exception {
        //given
        CustomerDto customerDto1 = new CustomerDto();
        customerDto1.setId(1L);
        customerDto1.setFirstname("Jack");
        CustomerDto customerDto2 = new CustomerDto();
        customerDto2.setId(2L);
        customerDto2.setFirstname("Johnny");
        List<CustomerDto> customerDtoList = Arrays.asList(customerDto1, customerDto2);

        //when
        when(customerService.getAllCustomers()).thenReturn(customerDtoList);

        //then

        mockMvc.perform(
                get("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(status().isOk());

    }

    @Test
    public void findCustomerByID() throws Exception {
        //given
        Long id = 1L;
        CustomerDto dto = new CustomerDto();
        dto.setFirstname("Foo");
        dto.setLastname("bar");
        dto.setId(id);

        //when
        when(customerService.getCustomerById(anyLong())).thenReturn(dto);

        //then
        mockMvc.perform(get("/api/v1/customers/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateCustomer() throws Exception {
        //given
        String firstname = "Online Customer";
        String lastname = "Pilot";

        CustomerDto customer = new CustomerDto();
        customer.setFirstname(firstname);
        customer.setLastname(lastname);

        CustomerDto savedDto = new CustomerDto();
        savedDto.setFirstname(customer.getFirstname());
        savedDto.setLastname(customer.getLastname());

        when(customerService.createCustomer(customer)).thenReturn(savedDto);


        //when
        //then
        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(customer)))
                .andExpect(jsonPath("$.firstname", equalTo(firstname)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        //given
        Long id = 99L;
        String firstName = "John";
        String lastName = "Smith";

        CustomerDto dto = new CustomerDto();
        dto.setFirstname(firstName);
        dto.setLastname(lastName);

        CustomerDto savedDto = new CustomerDto();
        savedDto.setFirstname(firstName);
        savedDto.setLastname(lastName);

        when(customerService.updateCustomer(anyLong(), any(CustomerDto.class))).thenReturn(savedDto);

        //when & then
        mockMvc.perform(put("/api/v1/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(asJsonString(dto)))
            .andExpect(jsonPath("$.firstname", equalTo(firstName)))
            .andExpect(jsonPath("$.lastname", equalTo(lastName)))
            .andExpect(status().isOk());
    }
}