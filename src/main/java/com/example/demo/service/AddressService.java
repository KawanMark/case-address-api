package com.example.demo.service;


import com.example.demo.client.ViaCepClient;
import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.ViaCepResponseDTO;
import com.example.demo.exception.custom.CepNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final ViaCepClient viaCepClient;

    public AddressService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    @Cacheable(value = "addresses", key = "#cep")
    public AddressDTO getAddressByCep(String cep) {
        System.out.println("Buscando CEP no serviço externo (ViaCEP): " + cep);

        ViaCepResponseDTO viaCepResponse = viaCepClient.getAddressByCep(cep)
                .orElseThrow(() -> new CepNotFoundException("CEP não encontrado."));

        if (viaCepResponse.isErro()) {
            throw new CepNotFoundException("CEP não encontrado.");
        }

        return convertToAddressDTO(viaCepResponse);
    }

    private AddressDTO convertToAddressDTO(ViaCepResponseDTO viaCepResponse) {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCep(viaCepResponse.getCep());
        addressDTO.setLogradouro(
                viaCepResponse.getLogradouro() != null ?
                        viaCepResponse.getLogradouro().toLowerCase() : null
        );
        addressDTO.setComplemento(viaCepResponse.getComplemento());
        addressDTO.setBairro(viaCepResponse.getBairro());
        addressDTO.setLocalidade(viaCepResponse.getLocalidade());
        return addressDTO;
    }
}
