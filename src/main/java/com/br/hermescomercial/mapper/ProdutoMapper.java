package com.br.hermescomercial.mapper;

import com.br.hermescomercial.dto.ProdutoDTO;
import com.br.hermescomercial.model.Produto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para conversão entre Produto e ProdutoDTO
 * Versão 2.0.0 - DTO Pattern Implementation
 */
public class ProdutoMapper {
    
    /**
     * Converte Produto para ProdutoDTO
     */
    public ProdutoDTO toDTO(Produto produto) {
        if (produto == null) {
            return null;
        }
        
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setCodigo(produto.getCodigo());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setCategoria(produto.getCategoria());
        dto.setPreco(produto.getPrecoVenda());
        dto.setEstoque(produto.getEstoque());
        dto.setUnidade(produto.getUnidade());
        // Status não existe na classe Produto original
        dto.setDataCriacao(produto.getDataCriacao());
        dto.setDataAtualizacao(produto.getDataAtualizacao());
        
        return dto;
    }
    
    /**
     * Converte ProdutoDTO para Produto
     */
    public Produto toEntity(ProdutoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setCodigo(dto.getCodigo());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setCategoria(dto.getCategoria());
        produto.setPrecoVenda(dto.getPreco());
        produto.setEstoque(dto.getEstoque());
        produto.setUnidade(dto.getUnidade());
        // Status não existe na classe Produto original
        produto.setDataCriacao(dto.getDataCriacao());
        produto.setDataAtualizacao(dto.getDataAtualizacao());
        
        return produto;
    }
    
    /**
     * Converte lista de Produto para lista de ProdutoDTO
     */
    public List<ProdutoDTO> toDTOList(List<Produto> produtos) {
        if (produtos == null) {
            return null;
        }
        
        return produtos.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Converte lista de ProdutoDTO para lista de Produto
     */
    public List<Produto> toEntityList(List<ProdutoDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Atualiza entidade Produto com dados do DTO
     */
    public void updateEntity(Produto produto, ProdutoDTO dto) {
        if (produto == null || dto == null) {
            return;
        }
        
        produto.setId(dto.getId());
        produto.setCodigo(dto.getCodigo());
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setCategoria(dto.getCategoria());
        produto.setPrecoVenda(dto.getPreco());
        produto.setEstoque(dto.getEstoque());
        produto.setUnidade(dto.getUnidade());
        // Status não existe na classe Produto original
        produto.setDataAtualizacao(dto.getDataAtualizacao());
    }
    
    /**
     * Atualiza DTO com dados da entidade Produto
     */
    public void updateDTO(ProdutoDTO dto, Produto produto) {
        if (dto == null || produto == null) {
            return;
        }
        
        dto.setId(produto.getId());
        dto.setCodigo(produto.getCodigo());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setCategoria(produto.getCategoria());
        dto.setPreco(produto.getPrecoVenda());
        dto.setEstoque(produto.getEstoque());
        dto.setUnidade(produto.getUnidade());
        // Status não existe na classe Produto original
        dto.setDataCriacao(produto.getDataCriacao());
        dto.setDataAtualizacao(produto.getDataAtualizacao());
    }
}
