package guru.springfamework.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.org.apache.bcel.internal.generic.IFNE;
import guru.springfamework.annotation.IgnoredProperty;
import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VendorServiceImplTest {

    @Mock
    private VendorRepository vendorRepository;

    private VendorMapper vendorMapper = VendorMapper.INSTANCE;

    private VendorService vendorService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vendorService = new VendorServiceImpl(vendorRepository, vendorMapper);
    }

    @Test
    public void getAllVendors() {
        //given
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        vendor1.setName("BMW");

        Vendor vendor2 = new Vendor();
        vendor2.setId(2L);
        vendor2.setName("Benz");

        when(vendorRepository.findAll()).thenReturn(Arrays.asList(vendor1, vendor2));

        //when
        List<VendorDTO> vendorDTOS = vendorService.getAllVendors();

        //then
        assertEquals(2, vendorDTOS.size());
    }

    @Test
    public void getById() {
        //given
        Long id = 1L;
        Vendor vendor = new Vendor();
        vendor.setId(id);
        vendor.setName("Haval");

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));

        //when
        VendorDTO vendorDTO = vendorService.getById(id);

        //then
        assertEquals("Haval", vendorDTO.getName());
    }

    @Test
    public void createVendor() {
        //given
        Long id = 2L;
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("Geely");

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(id);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        //when
        VendorDTO savedDto = vendorService.createVendor(vendorDTO);

        //then
        assertEquals(vendorDTO.getName(), savedDto.getName());
        assertEquals("/api/v1/vendors/" + id, savedDto.getVendorUrl());
    }

    @Test
    public void updateVendor() {
        //given
        Long id = 1L;
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName("BMW");

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(id);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        //when
        VendorDTO savedDto = vendorService.updateVendor(id, vendorDTO);

        //then
        assertEquals(vendorDTO.getName(), savedDto.getName());
        assertEquals("/api/v1/vendors/" + id, savedDto.getVendorUrl());
    }

    @Test
    public void deleteVendorById() {

        Long id = 1L;
        vendorRepository.deleteById(id);
        verify(vendorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testCopyNonNullProperties() {
        VendorDTO incoming = new VendorDTO();
        incoming.setName("Incoming Inc.");

        VendorDTO existing = new VendorDTO();
        existing.setName("Existing Inc.");
        existing.setVendorUrl("/api/v1/vendors/1");

        System.out.println(String.format("incoming object: %s", incoming));
        System.out.println(String.format("existing object: %s", existing));

        Set<String> ignoredProperties = new HashSet<>();
        BeanWrapper beanWrapper = new BeanWrapperImpl(incoming);
        Arrays.asList(beanWrapper.getPropertyDescriptors()).forEach(pd ->{
            String propertyName = pd.getName();
            Object propertyValue = beanWrapper.getPropertyValue(propertyName);
            if (propertyValue == null) {
                System.out.println("detected null property: " + propertyName);
                ignoredProperties.add(propertyName);
            }

        });

        Arrays.asList(incoming.getClass().getDeclaredFields())
        .forEach(field -> {
            if (field.isAnnotationPresent(IgnoredProperty.class)) {
                System.out.println("Detected ignorable field with annotation: "+ field.getName());
                ignoredProperties.add(field.getName());
            }
        });

        BeanUtils.copyProperties(incoming, existing, new ArrayList<>(ignoredProperties).toArray(new String[]{}));

        System.out.println(String.format("merged object: %s", existing));
    }
}