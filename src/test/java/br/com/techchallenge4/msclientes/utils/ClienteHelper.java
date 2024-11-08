package br.com.techchallenge4.msclientes.utils;

import br.com.techchallenge4.msclientes.dto.ClienteDTO;
import br.com.techchallenge4.msclientes.dto.EnderecoDTO;

import java.time.LocalDate;
import java.util.List;

public abstract class ClienteHelper {

    public static ClienteDTO criarCliente(){
        return new ClienteDTO(null,
                "Cliente 1",
                "00000000000",
                LocalDate.of(1989,10,01),
                "11999998888",
                "teste@teste.com.br",
                List.of(new EnderecoDTO(
                        null,
                        "logradouro",
                        "1",
                        "compl",
                        "bairro",
                        "cidade",
                        "SP",
                        "99999999")
                )
        );
    }

}
