package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static guru.springfamework.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {VendorController.class})
public class VendorControllerTest {

    @MockBean
    private VendorService vendorService;

    @Autowired
    private MockMvc mockMvc;

    private VendorDTO vendorDTO1 = new VendorDTO();
    private VendorDTO vendorDTO2 = new VendorDTO();

    @Before
    public void setUp() throws Exception {
        vendorDTO1.setName("BMW");
        vendorDTO1.setVendorUrl(VendorController.BASE_URL + "/1");

        vendorDTO2.setName("GM");
        vendorDTO2.setVendorUrl(VendorController.BASE_URL + "/2");

    }

    @Test
    public void getAllVendors() throws Exception {
        VendorListDTO vendorListDTO = new VendorListDTO(Arrays.asList(vendorDTO1, vendorDTO2));

        given(vendorService.getAllVendors()).willReturn(vendorListDTO);

        //when / then
        mockMvc.perform(get(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void getVendorById() throws Exception {
        //given
        given(vendorService.getById(anyLong())).willReturn(vendorDTO1);

        //when
        //then
        mockMvc.perform(get(vendorDTO1.getVendorUrl())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO1.getName())));
    }

    @Test
    public void createVendor() throws Exception {
        //given
        given(vendorService.createVendor(vendorDTO1)).willReturn(vendorDTO1);

        //when/then
        mockMvc.perform(post(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO1.getName())));
    }

    @Test
    public void updateVendor() throws Exception {
        Long id = 1L;
        String url = VendorController.BASE_URL + "/" + id;
        VendorDTO existing = new VendorDTO();
        existing.setName("Ford");
        existing.setVendorUrl(url);

        VendorDTO request = new VendorDTO();
        request.setName("Incoming");

        VendorDTO updated = new VendorDTO();
        updated.setName(request.getName());

        given(vendorService.updateVendor(anyLong(), any(VendorDTO.class))).willReturn(updated);

        //when/then
        mockMvc.perform(put(url).contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(updated.getName())));

    }

    @Test
    public void patchVendor() throws Exception {
        Long id = 1L;
        String url = VendorController.BASE_URL + "/" + id;

        VendorDTO request = new VendorDTO();
        request.setName("Incoming");

        VendorDTO updated = new VendorDTO();
        updated.setName(request.getName());

        given(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).willReturn(updated);

        //when/then
        mockMvc.perform(patch(url).contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(updated.getName())))
        ;

    }

    @Test
    public void deleteVendor() throws Exception {
        Long id = 1L;
        String url = VendorController.BASE_URL + "/" + id;

        mockMvc.perform(delete(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        then(vendorService).should().deleteVendorById(anyLong());

    }
}