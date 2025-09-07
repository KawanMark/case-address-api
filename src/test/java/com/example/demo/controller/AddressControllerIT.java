package com.example.demo.controller;


import com.example.demo.dto.AddressDTO;
import com.example.demo.exception.custom.CepNotFoundException;
import com.example.demo.service.AddressService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class AddressControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    void validCep_returnsOkWithAddressData() throws Exception {

        String cep = "01001000";
        AddressDTO mockAddress = new AddressDTO();
        mockAddress.setLogradouro("praça da sé");
        mockAddress.setCep("01001-000");
        when(addressService.getAddressByCep(cep)).thenReturn(mockAddress);

        mockMvc.perform(get("/addresses/" + cep))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.logradouro").value("praça da sé")); // Verifica o conteúdo do JSON de resposta
    }

    @Test
    void invalidCepFormat_returnsBadRequest() throws Exception {
        String cepInvalido = "12345";

        mockMvc.perform(get("/addresses/" + cepInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void nonExistentCep_returnsNotFound() throws Exception {
        String cepInexistente = "99999999";
        when(addressService.getAddressByCep(cepInexistente))
                .thenThrow(new CepNotFoundException("CEP não encontrado."));

        mockMvc.perform(get("/addresses/" + cepInexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("CEP não encontrado."));
    }

}
