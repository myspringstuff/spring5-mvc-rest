package guru.springfamework.services;

import guru.springfamework.BeanSupport;
import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public VendorListDTO getAllVendors() {
        List<VendorDTO> vendors = vendorRepository.findAll().stream()
                .map(vendorMapper::vendorToVendorDTO)
                .collect(Collectors.toList());
        return new VendorListDTO(vendors);
    }

    @Override
    public VendorDTO getById(@NotNull Long id) {
        return vendorMapper.vendorToVendorDTO(vendorRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public VendorDTO createVendor(VendorDTO vendorDTO) {
        Vendor vendor = vendorRepository.save(vendorMapper.vendorDtoToVendor(vendorDTO));
        VendorDTO savedDto = vendorMapper.vendorToVendorDTO(vendor);
        savedDto.setVendorUrl("/api/v1/vendors/"+vendor.getId());
        return savedDto;
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
        Vendor vendor = vendorMapper.vendorDtoToVendor(vendorDTO);
        vendor.setId(id);

        VendorDTO savedDto = vendorMapper.vendorToVendorDTO(vendorRepository.save(vendor));
        savedDto.setVendorUrl("/api/v1/vendors/"+vendor.getId());
        return savedDto;
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        Vendor incoming = vendorMapper.vendorDtoToVendor(vendorDTO);
        incoming.setId(id);

        Vendor existing = vendorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        BeanSupport.copyProperties(incoming, existing, true);
        VendorDTO savedDto =  vendorMapper.vendorToVendorDTO(vendorRepository.save(existing));
        savedDto.setVendorUrl("/api/v1/vendors/"+existing.getId());
        return savedDto;
    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }

}
