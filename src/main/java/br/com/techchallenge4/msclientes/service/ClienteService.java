package br.com.techchallenge4.msclientes.service;

import br.com.techchallenge4.msclientes.dto.ClienteDTO;
import br.com.techchallenge4.msclientes.dto.EnderecoDTO;
import br.com.techchallenge4.msclientes.model.Cliente;
import br.com.techchallenge4.msclientes.model.Endereco;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ClienteService {

    ClienteDTO saveCliente(ClienteDTO clienteDTO);
    ClienteDTO getCliente(Long id);
    Page<ClienteDTO> getClientes(Pageable pageable);
    ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO) throws EntityNotFoundException;
    boolean deleteCliente(Long id);
    ClienteDTO toClienteDTO(Cliente cliente);
    List<EnderecoDTO> toEnderecoDTO(List<Endereco> enderecos);
    Cliente toClienteEntity(ClienteDTO clienteDTO);
    List<Endereco> toEnderecoEntity(List<EnderecoDTO> enderecosDTO);
}
