package br.com.techchallenge4.msclientes.dto;

public record EnderecoDTO (
        Long id,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
){}