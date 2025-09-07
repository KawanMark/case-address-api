package com.example.demo.controller;

import com.example.demo.service.AddressService;
import com.example.demo.exception.custom.InvalidCepFormatException;
import com.example.demo.dto.AddressDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Esta anotação combina @Controller e @ResponseBody, indicando que esta classe
 * é um controller REST e seus métodos retornarão JSON diretamente no corpo da resposta.
 */
@RestController
/**
 * Mapeia todas as requisições que começam com "/addresses" para este controller.
 */
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    /**
     * Usamos injeção de dependência via construtor. O Spring automaticamente
     * fornecerá uma instância de AddressService quando criar o AddressController.
     */
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Mapeia requisições HTTP GET para a URL "/addresses/{cep}".
     * O valor dentro de {cep} será capturado pela variável do método.
     * @param cep O CEP extraído da URL.
     * @return Um ResponseEntity contendo o AddressDTO e o status HTTP 200 OK.
     */
    @GetMapping("/{cep}")
    public ResponseEntity<AddressDTO> getByCep(@PathVariable String cep) {
        validateCepFormat(cep);

        AddressDTO address = addressService.getAddressByCep(cep);

        return ResponseEntity.ok(address);
    }

    /**
     * Método privado para validar a lógica de formato do CEP.
     * Se for inválido, lança uma exceção customizada que será capturada
     * pelo nosso GlobalExceptionHandler para retornar um erro 400.
     */
    private void validateCepFormat(String cep) {
        if (cep == null || !cep.replaceAll("[^0-9]", "").matches("\\d{8}")) {
            throw new InvalidCepFormatException("Formato de CEP inválido. O CEP deve conter 8 dígitos numéricos.");
        }
    }
}