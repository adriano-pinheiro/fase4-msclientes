package br.com.techchallenge4.msclientes.controller;

import br.com.techchallenge4.msclientes.dto.ClienteDTO;
import br.com.techchallenge4.msclientes.handler.GlobalExceptionHandler;
import br.com.techchallenge4.msclientes.service.ClienteService;
import br.com.techchallenge4.msclientes.utils.ClienteHelper;
import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ClienteControllerTest {

    private MockMvc mockMvc;

    @RegisterExtension
    LogTrackerStub logTracker = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.INFO)
            .recordForType(ClienteController.class);

    @Mock
    private ClienteService clienteService;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        ClienteController clienteController = new ClienteController(clienteService);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void devePermitirBuscarUmCliente() throws Exception {
        var id = 1L;
        var cliente = ClienteHelper.criarCliente();

        when(clienteService.getCliente(any(Long.class))).thenReturn(cliente);

        mockMvc.perform(get("/api/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(cliente.nome().toString()));
        verify(clienteService, times(1)).getCliente(any(Long.class));
    }

    @Test
    void devePermirirAlterarUmCliente() throws Exception {
        var id = 1L;
        var cliente = ClienteHelper.criarCliente();

        when(clienteService.updateCliente(any(Long.class), any(ClienteDTO.class)))
                .thenAnswer(i -> i.getArgument(1));

        mockMvc.perform(put("/api/v1/clientes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cliente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(cliente.nome()));
        verify(clienteService, times(1))
                .updateCliente(any(Long.class), any(ClienteDTO.class));
    }

    @Test
    void devePermitirApagarUmCliente() throws Exception {
        var id = 1L;

        when(clienteService.deleteCliente(any(Long.class)))
                .thenReturn(true);

        mockMvc.perform(delete("/api/v1/clientes/{id}", id))
                .andExpect(status().isNoContent());
        verify(clienteService, times(1))
                .deleteCliente(any(Long.class));
    }

    public static String asJsonString(final Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
