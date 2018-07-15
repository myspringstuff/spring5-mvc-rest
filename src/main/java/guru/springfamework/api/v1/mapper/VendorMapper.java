package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    default VendorDTO vendorToVendorDTO(Vendor vendor){
        if ( vendor == null ) {
            return null;
        }

        VendorDTO vendorDTO = new VendorDTO();

        vendorDTO.setName( vendor.getName() );
        vendorDTO.setVendorUrl(VendorController.BASE_URL + "/" + vendor.getId());
        return vendorDTO;
    }

    Vendor vendorDtoToVendor(VendorDTO vendorDTO);
}
