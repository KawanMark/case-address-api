package com.example.demo.service;
import com.example.demo.client.ViaCepClient;
import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.ViaCepResponseDTO;
import com.example.demo.exception.*;
import com.example.demo.exception.custom.CepNotFoundException;
import com.example.demo.exception.custom.ServiceUnavailableException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private AddressService addressService;

    @Test
    void getAddressByCep_ValidCep_ReturnsAddressDTO() throws Exception {
        String cep = "01001000";
        ViaCepResponseDTO mockResponse = new ViaCepResponseDTO();
        mockResponse.setLogradouro("Praça da Sé");
        mockResponse.setErro(false);

        when(viaCepClient.getAddressByCep(cep)).thenReturn(Optional.of(mockResponse));

        AddressDTO result = addressService.getAddressByCep(cep);

        assertNotNull(result);
        assertEquals("praça da sé", result.getLogradouro(), "O logradouro deveria ter sido convertido para minúsculas.");
    }

    @Test
    void getAddressByCep_InvalidFormat_ReturnsBadRequest() throws Exception {
        String cep = "99999999";
        ViaCepResponseDTO mockResponse = new ViaCepResponseDTO();
        mockResponse.setErro(true);
        when(viaCepClient.getAddressByCep(cep)).thenReturn(Optional.of(mockResponse));

        assertThrows(CepNotFoundException.class, () -> {
            addressService.getAddressByCep(cep);
        }, "Deveria ter lançado CepNotFoundException quando o CEP não é encontrado.");
    }

    @Test
    void getAddressByCep_NonExistentCep_ReturnsNotFound() throws Exception {
        String cep = "00000000";
        when(viaCepClient.getAddressByCep(cep)).thenThrow(new ServiceUnavailableException("Falha na API externa"));

        assertThrows(ServiceUnavailableException.class, () -> {
            addressService.getAddressByCep(cep);
        }, "Deveria ter lançado ServiceUnavailableException quando a API externa falha.");

    }

}
