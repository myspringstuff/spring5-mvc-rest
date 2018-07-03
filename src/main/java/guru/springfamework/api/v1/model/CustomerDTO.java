package guru.springfamework.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CustomerDTO {
    private String firstname;
    private String lastname;
    private String customerUrl;
}
