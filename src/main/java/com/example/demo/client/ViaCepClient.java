package com.example.demo.client;


import com.example.demo.dto.ViaCepResponseDTO;
import com.example.demo.exception.custom.ServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class ViaCepClient {
    private final RestTemplate restTemplate;

    public ViaCepClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<ViaCepResponseDTO> getAddressByCep(String cep) {
        String viaCepUrl = "https://viacep.com.br/ws/" + cep + "/json/";
        try {
            ViaCepResponseDTO response = restTemplate.getForObject(viaCepUrl, ViaCepResponseDTO.class);
            return Optional.ofNullable(response);
        } catch (RestClientException e) {
            throw new ServiceUnavailableException("O serviço externo (ViaCEP) está indisponível.");
        }
    }

}
