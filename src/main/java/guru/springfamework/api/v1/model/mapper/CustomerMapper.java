package guru.springfamework.api.v1.model.mapper;

import guru.springfamework.api.v1.model.CustomerDto;
import guru.springfamework.domain.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    @Mappings({
            @Mapping(source = "firstName", target = "firstname"),
            @Mapping(source = "lastName", target = "lastname")
    })
    CustomerDto customerToCustomerDTO(Customer customer);

    @Mappings({
            @Mapping(source = "firstname", target = "firstName"),
            @Mapping(source = "lastname",target = "lastName")
    }
    )
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
