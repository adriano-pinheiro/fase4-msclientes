package br.com.techchallenge4.msclientes.service.impl;

import br.com.techchallenge4.msclientes.dto.ClienteDTO;
import br.com.techchallenge4.msclientes.model.Cliente;
import br.com.techchallenge4.msclientes.repository.ClienteRepository;
import br.com.techchallenge4.msclientes.repository.EnderecoRepository;
import br.com.techchallenge4.msclientes.service.ClienteService;
import br.com.techchallenge4.msclientes.utils.ClienteHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClienteServiceImplTest {

    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        clienteService = new ClienteServiceImpl (clienteRepository, enderecoRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirSalvarUmCliente() {
        var cliente = ClienteHelper.criarCliente();
        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(i -> i.getArgument(0));

        var clienteArmazenado = clienteService.saveCliente(cliente);

        assertThat(clienteArmazenado)
                .isInstanceOf(ClienteDTO.class)
                .isNotNull();
        assertThat(clienteArmazenado.nome())
                .isNotNull();
        assertThat(clienteArmazenado.nome())
                .isEqualTo(cliente.nome());
    }

    @Test
    void devePermitirBuscarUmCliente() {
        var id = 1L;
        var cliente = clienteService.toClienteEntity(ClienteHelper.criarCliente());
        cliente.setId(id);
        when(clienteRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(cliente));

        var clienteObtido = clienteService.getCliente(id);

        verify(clienteRepository, times(1))
                .findById(id);
        assertThat(clienteObtido.id())
                .isEqualTo(cliente.getId());
    }

}
