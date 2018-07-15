package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VendorControllerTest extends AbstractRestControllerTest {

    @Mock
    private VendorService vendorService;

    @InjectMocks
    private VendorController vendorController;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getAllVendors() throws Exception {
        VendorDTO vendor1 = new VendorDTO();
        vendor1.setName("BMW");
        vendor1.setVendorUrl(VendorController.BASE_URL + "/1");

        VendorDTO vendor2 = new VendorDTO();
        vendor2.setName("GM");
        vendor2.setVendorUrl(VendorController.BASE_URL + "/2");

        when(vendorService.getAllVendors()).thenReturn(Arrays.asList(vendor1, vendor2));

        //when / then
        mockMvc.perform(get(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void getVendorById() throws Exception {
        //given
        Long id = 1L;
        String uri = VendorController.BASE_URL + "/" + id;
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Benz");
        vendorDTO.setVendorUrl(uri);

        when(vendorService.getById(anyLong())).thenReturn(vendorDTO);

        //when
        //then
        mockMvc.perform(get(uri)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO.getName())));
    }

    @Test
    public void createVendor() throws Exception {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Jeep");

        VendorDTO savedDto = new VendorDTO();
        savedDto.setName(vendorDTO.getName());
        savedDto.setVendorUrl(VendorController.BASE_URL + "/1");

        when(vendorService.createVendor(vendorDTO)).thenReturn(savedDto);

        //when/then
        mockMvc.perform(post(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO.getName())));
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

        when(vendorService.updateVendor(anyLong(), any(VendorDTO.class))).thenReturn(updated);

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

        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(updated);

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

        verify(vendorService).deleteVendorById(anyLong());

    }
}