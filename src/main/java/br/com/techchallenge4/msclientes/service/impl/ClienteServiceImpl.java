package br.com.techchallenge4.msclientes.service.impl;

import br.com.techchallenge4.msclientes.controller.exception.ControllerNotFoundException;
import br.com.techchallenge4.msclientes.dto.ClienteDTO;
import br.com.techchallenge4.msclientes.dto.EnderecoDTO;
import br.com.techchallenge4.msclientes.model.Cliente;
import br.com.techchallenge4.msclientes.model.Endereco;
import br.com.techchallenge4.msclientes.repository.ClienteRepository;
import br.com.techchallenge4.msclientes.repository.EnderecoRepository;
import br.com.techchallenge4.msclientes.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    private final EnderecoRepository enderecoRepository;

    @Override
    public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clienteRepository.save(toClienteEntity(clienteDTO));
        if (cliente.getId() != null) {
            for (Endereco endereco : cliente.getEnderecos()) {
                endereco.setCliente(cliente);
                enderecoRepository.save(endereco);
            }
        }
        return toClienteDTO(cliente);
    }

    @Override
    public ClienteDTO getCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow( () ->
                        new ControllerNotFoundException("Cliente não encontrado."));
        return toClienteDTO(cliente);
    }

    @Override
    public Page<ClienteDTO> getClientes(Pageable pageable) {
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        return clientes.map(this::toClienteDTO);
    }

    @Override
    public ClienteDTO updateCliente(Long id, ClienteDTO clienteDTO) throws EntityNotFoundException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow( () ->
                        new ControllerNotFoundException("Cliente não encontrado."));
        cliente.setNome(clienteDTO.nome());
        cliente.setCpfCnpj(clienteDTO.cpfCnpj());
        cliente.setDtNascimento(clienteDTO.dtNascimento());
        cliente.setTelefone(clienteDTO.telefone());
        cliente.setEmail(clienteDTO.email());
        clienteRepository.save(cliente);

        for (EnderecoDTO enderecoDto : clienteDTO.enderecos()) {
            Endereco endereco  = enderecoRepository.findById(enderecoDto.id())
                    .orElseThrow( () ->
                            new ControllerNotFoundException("Endereço não encontrado."));
            endereco.setLogradouro(enderecoDto.logradouro());
            endereco.setNumero(enderecoDto.numero());
            endereco.setComplemento(enderecoDto.complemento());
            endereco.setBairro(enderecoDto.bairro());
            endereco.setCidade(enderecoDto.cidade());
            endereco.setEstado(enderecoDto.estado());
            endereco.setCep(enderecoDto.cep());

            enderecoRepository.save(endereco);
        }
        return toClienteDTO(cliente);
    }

    @Override
    public boolean deleteCliente(Long id) {
        clienteRepository.deleteById(id);
        return true;
    }

    @Override
    public ClienteDTO toClienteDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj(),
                cliente.getDtNascimento(),
                cliente.getTelefone(),
                cliente.getEmail(),
                toEnderecoDTO(cliente.getEnderecos())
        );
    }

    @Override
    public List<EnderecoDTO> toEnderecoDTO(List<Endereco> enderecos) {
        List<EnderecoDTO> enderecoDTOs = new ArrayList<>();

        for (Endereco endereco : enderecos) {
            EnderecoDTO enderecoDTO = new EnderecoDTO(
                    endereco.getId(),
                    endereco.getLogradouro(),
                    endereco.getNumero(),
                    endereco.getComplemento(),
                    endereco.getBairro(),
                    endereco.getCidade(),
                    endereco.getEstado(),
                    endereco.getCep()
            );
            enderecoDTOs.add(enderecoDTO);
        }
        return enderecoDTOs;
    }

    @Override
    public Cliente toClienteEntity(ClienteDTO clienteDTO) {
        return new Cliente(
                clienteDTO.id(),
                clienteDTO.nome(),
                clienteDTO.cpfCnpj(),
                clienteDTO.dtNascimento(),
                clienteDTO.telefone(),
                clienteDTO.email(),
                toEnderecoEntity(clienteDTO.enderecos())
        );
    }

    @Override
    public List<Endereco> toEnderecoEntity(List<EnderecoDTO> enderecosDTO) {
        List<Endereco> enderecos = new ArrayList<>();

        for (EnderecoDTO enderecoDto : enderecosDTO) {
            Endereco endereco = new Endereco();
            endereco.setId(enderecoDto.id());
            endereco.setLogradouro(enderecoDto.logradouro());
            endereco.setNumero(enderecoDto.numero());
            endereco.setComplemento(enderecoDto.complemento());
            endereco.setBairro(enderecoDto.bairro());
            endereco.setCidade(enderecoDto.cidade());
            endereco.setEstado(enderecoDto.estado());
            endereco.setCep(enderecoDto.cep());

            enderecos.add(endereco);
        }
        return enderecos;
    }

}
