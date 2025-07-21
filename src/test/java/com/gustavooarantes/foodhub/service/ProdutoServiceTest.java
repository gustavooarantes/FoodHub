package com.gustavooarantes.foodhub.service;

import com.gustavooarantes.foodhub.domain.Produto;
import com.gustavooarantes.foodhub.dto.ProdutoDto;
import com.gustavooarantes.foodhub.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void quandoListarTodos_entaoDeveRetornarListaDeProdutosDto() {
        Produto produto1 = new Produto();
        produto1.setId(1L);
        produto1.setNome("X-Burger Clássico");
        produto1.setPreco(new BigDecimal("25.50"));

        Produto produto2 = new Produto();
        produto2.setId(2L);
        produto2.setNome("Refrigerante");
        produto2.setPreco(new BigDecimal("8.00"));

        given(produtoRepository.findAll()).willReturn(List.of(produto1, produto2));

        List<ProdutoDto> resultado = produtoService.listarTodos();

        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        assertThat(resultado.get(0).nome()).isEqualTo("X-Burger Clássico");
        assertThat(resultado.get(1).nome()).isEqualTo("Refrigerante");
    }

    @Test
    void quandoCriarProduto_entaoDeveSalvarERetornarProdutoDto() {
        ProdutoDto produtoParaCriar = new ProdutoDto(null, "Batata Frita", "Crocante", new BigDecimal("15.00"), true);

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(1L);
        produtoSalvo.setNome(produtoParaCriar.nome());
        produtoSalvo.setDescricao(produtoParaCriar.descricao());
        produtoSalvo.setPreco(produtoParaCriar.preco());
        produtoSalvo.setDisponivel(produtoParaCriar.disponivel());

        given(produtoRepository.save(any(Produto.class))).willReturn(produtoSalvo);

        ProdutoDto resultado = produtoService.criarProduto(produtoParaCriar);

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
        assertThat(resultado.nome()).isEqualTo("Batata Frita");

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }
}