package com.gustavooarantes.foodhub.service;

import com.gustavooarantes.foodhub.domain.Produto;
import com.gustavooarantes.foodhub.dto.ProdutoDto;
import com.gustavooarantes.foodhub.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Cacheable(value = "produtos")
    @Transactional(readOnly = true)
    public List<ProdutoDto> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "produto", key = "#id")
    @Transactional(readOnly = true)
    public ProdutoDto buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));
        return mapToDto(produto);
    }

    @CacheEvict(value = "produtos", allEntries = true)
    @Transactional
    public ProdutoDto criarProduto(ProdutoDto produtoDto) {
        Produto produto = mapToEntity(produtoDto);
        Produto produtoSalvo = produtoRepository.save(produto);
        return mapToDto(produtoSalvo);
    }

    @Caching(evict = {
            @CacheEvict(value = "produtos", allEntries = true),
            @CacheEvict(value = "produto", key = "#id")
    })
    @Transactional
    public ProdutoDto atualizarProduto(Long id, ProdutoDto produtoDto) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado com id: " + id));

        produtoExistente.setNome(produtoDto.nome());
        produtoExistente.setDescricao(produtoDto.descricao());
        produtoExistente.setPreco(produtoDto.preco());
        produtoExistente.setDisponivel(produtoDto.disponivel());

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return mapToDto(produtoAtualizado);
    }

    @Caching(evict = {
            @CacheEvict(value = "produtos", allEntries = true),
            @CacheEvict(value = "produto", key = "#id")
    })
    @Transactional
    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntityNotFoundException("Produto não encontrado com id: " + id);
        }
        produtoRepository.deleteById(id);
    }

    private ProdutoDto mapToDto(Produto produto) {
        return new ProdutoDto(produto.getId(), produto.getNome(), produto.getDescricao(), produto.getPreco(), produto.isDisponivel());
    }

    private Produto mapToEntity(ProdutoDto produtoDto) {
        Produto produto = new Produto();
        produto.setNome(produtoDto.nome());
        produto.setDescricao(produtoDto.descricao());
        produto.setPreco(produtoDto.preco());
        produto.setDisponivel(produtoDto.disponivel());
        return produto;
    }
}