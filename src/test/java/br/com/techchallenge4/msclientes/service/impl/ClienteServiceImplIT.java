package br.com.techchallenge4.msclientes.service.impl;

import br.com.techchallenge4.msclientes.controller.exception.ControllerNotFoundException;
import br.com.techchallenge4.msclientes.dto.ClienteDTO;
import br.com.techchallenge4.msclientes.model.Endereco;
import br.com.techchallenge4.msclientes.service.ClienteService;
import br.com.techchallenge4.msclientes.utils.ClienteHelper;
import jakarta.transaction.Transactional;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ClienteServiceImplIT {

    @Autowired
    private ClienteService clienteService;

    @Test
    void devePermitirSalvarUmCliente() {
        var cliente = ClienteHelper.criarCliente();
        var clienteArmazenado = clienteService.saveCliente(cliente);

        assertThat(clienteArmazenado)
                .isNotNull()
                .isInstanceOf(ClienteDTO.class);
        assertThat(clienteArmazenado.id())
                .isNotNull();
        assertThat(clienteArmazenado.nome())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(cliente.nome());
    }

    @Test
    void devePermitirBuscarUmCliente() {
        var cliente = ClienteHelper.criarCliente();
        var clienteArmazenado = clienteService.saveCliente(cliente);
        var clienteLocalizado = clienteService.getCliente(clienteArmazenado.id());

        assertThat(clienteLocalizado)
                .isNotNull()
                .isInstanceOf(ClienteDTO.class);
        assertThat(clienteLocalizado.id())
                .isNotNull();
        assertThat(clienteLocalizado.nome())
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(clienteArmazenado.nome());
    }

    @Test
    void devePermitirDeletarUmCliente() {
        var cliente = ClienteHelper.criarCliente();
        var clienteArmazenado = clienteService.saveCliente(cliente);
        var resultado = clienteService.deleteCliente(clienteArmazenado.id());

        assertThat(resultado).isTrue();
    }

    @Test
    void devePermirirAlterarUmCliente() {
        var cliente = ClienteHelper.criarCliente();
        var clienteOriginal = clienteService.saveCliente(cliente);
        var clienteAlterado = clienteService.toClienteEntity(clienteOriginal);
        clienteAlterado.setNome("Nome alterado");
        var clienteAtualizado = clienteService.updateCliente(clienteAlterado.getId(), clienteService.toClienteDTO(clienteAlterado));

        assertThat(clienteAtualizado)
                .isInstanceOf(ClienteDTO.class)
                .isNotNull();
        assertThat(clienteAtualizado.id())
                .isEqualTo(clienteOriginal.id());
        assertThat(clienteAlterado.getNome())
                .isEqualTo(clienteAtualizado.nome());
    }

    @Test
    void devePermitirListarClientes() {
        var cliente1 = ClienteHelper.criarCliente();
        clienteService.saveCliente(cliente1);

        var cliente2 = ClienteHelper.criarCliente();
        clienteService.saveCliente(cliente2);

        Page<ClienteDTO> clienteDTOS = clienteService.getClientes(Pageable.unpaged());

        assertThat(clienteDTOS).hasSizeGreaterThan(0);
        assertThat(clienteDTOS.getContent())
                .allSatisfy(clientes -> {
                    assertThat(clientes).isNotNull();
                    assertThat(clientes).isInstanceOf(ClienteDTO.class);
                });
    }

    @Test
    void deveGerarExcecao_QuandoBuscarCliente_IdClienteNaoExistente() {
        var id = 99999999L;

        assertThatThrownBy(() -> clienteService.getCliente(id))
                .isInstanceOf(ControllerNotFoundException.class)
                .hasMessage("Cliente não encontrado.");
    }

    @Test
    void deveGerarExcecao_QuandoAlterarCliente_IdClienteNaoExistente() {
        var id = 99999999L;
        var cliente = ClienteHelper.criarCliente();
        var clienteOriginal = clienteService.saveCliente(cliente);
        var clienteAlterado = clienteService.toClienteEntity(clienteOriginal);
        clienteAlterado.setNome("Nome alterado");

        try {
            ClienteDTO clienteAlteradoDTO = clienteService.toClienteDTO(clienteAlterado);
            Executable executable = () -> clienteService.updateCliente(id, clienteAlteradoDTO);

            assertThatThrownBy((ThrowableAssert.ThrowingCallable) executable)
                    .isInstanceOf(ControllerNotFoundException.class)
                    .hasMessage("Cliente não encontrado.");
        }
        catch (Exception ex) {
            ex.getMessage();
        }

    }

    @Test
    void deveGerarExcecao_QuandoAlterarCliente_IdEnderecoNaoExistente() {
        var id = 88888888L;
        var cliente = ClienteHelper.criarCliente();
        var clienteOriginal = clienteService.saveCliente(cliente);
        var clienteAlterado = clienteService.toClienteEntity(clienteOriginal);
        List<Endereco> enderecos =  clienteAlterado.getEnderecos();
        enderecos.get(0).setId(id);
        clienteAlterado.setEnderecos(enderecos);

        ClienteDTO clienteAlteradoDTO = clienteService.toClienteDTO(clienteAlterado);
        try {
            Executable executable = () -> clienteService.updateCliente(clienteOriginal.id(), clienteAlteradoDTO);

            assertThatThrownBy((ThrowableAssert.ThrowingCallable) executable)
                    .isInstanceOf(ControllerNotFoundException.class)
                    .hasMessage("Endereço não encontrado.");
        }
        catch (Exception ex) {
            ex.getMessage();
        }



    }

}
