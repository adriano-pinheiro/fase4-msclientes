package br.com.techchallenge4.msclientes.dto;

import java.time.LocalDate;
import java.util.List;

public record ClienteDTO (
    Long id,
    String nome,
    String cpfCnpj,
    LocalDate dtNascimento,
    String telefone,
    String email,
    List<EnderecoDTO> enderecos
){}
