package com.gustavooarantes.foodhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavooarantes.foodhub.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.gustavooarantes.foodhub.domain.Produto;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.gustavooarantes.foodhub.dto.ProdutoDto;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
    }

    @Test
    void quandoListarProdutos_comProdutosExistentes_entaoDeveRetornarStatus200EListaDeProdutos() throws Exception {
        Produto produto = new Produto();
        produto.setNome("Pizza Mussarela");
        produto.setPreco(new BigDecimal("45.00"));
        produto.setDisponivel(true);
        produtoRepository.save(produto);

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Pizza Mussarela")));
    }

    @Test
    void quandoListarProdutos_semProdutosNoBanco_entaoDeveRetornarStatus200EListaVazia() throws Exception {
        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void quandoCriarProduto_comUsuarioAdmin_entaoDeveRetornarStatus201() throws Exception {
        ProdutoDto novoProdutoDto = new ProdutoDto(null, "X-Bacon", "Pão, bife e bacon", new BigDecimal("32.00"), true);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoProdutoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("X-Bacon")));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void quandoCriarProduto_comUsuarioCliente_entaoDeveRetornarStatus403() throws Exception {
        ProdutoDto novoProdutoDto = new ProdutoDto(null, "X-Bacon", "Pão, bife e bacon", new BigDecimal("32.00"), true);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoProdutoDto)))
                .andExpect(status().isForbidden());
    }
}