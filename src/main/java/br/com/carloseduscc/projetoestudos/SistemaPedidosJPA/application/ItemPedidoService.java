package br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application;

import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application.comand.AtualizarItemCommand;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application.dto.item.ItemDetalhadoDTO;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application.exception.NaoEncontradoException;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application.mapper.ItemPedidoMapper;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.infra.repository.ItemPedidoRepository;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.infra.repository.projections.ItemPedidoDetalhadoProjection;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.model.ItemPedido;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.model.exception.RegraDeNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemPedidoService {
    private final ItemPedidoRepository repository;
    private final ItemPedidoRepository itemRepository;
    private final ItemPedidoMapper mapper;

    public ItemDetalhadoDTO buscarDetalhes(UUID id){
        ItemPedidoDetalhadoProjection projection = repository.buscarItemPedidoDetalhadoProjection(id)
                .orElseThrow(() -> getItemNaoEncontradoException(id));

        return mapper.fromProjectionToDTO(projection);
    }

    public void remover(UUID idItem){
        if (!repository.existsById(idItem)) throw getItemNaoEncontradoException(idItem);
        int linhasAfetadas = repository.deleteByIdIfPedidoPendente(idItem);
        if (linhasAfetadas == 0) throw new RegraDeNegocioException("Não é possível remover um item de um pedido que já não está mais pendente");
    }

    @Transactional
    public void atualizar(AtualizarItemCommand dados, UUID id){

        ItemPedido itemPedido = itemRepository.buscarItemFetchPedido(id)
                .orElseThrow(() -> getItemNaoEncontradoException(id));

        if (dados.quantidade() != null){
            int quantidadeAcrescentada = dados.quantidade() - itemPedido.getQuantidade();
            if (quantidadeAcrescentada > 0){
                itemPedido.getPedido().validarNovoItemNoPedido(
                        new ItemPedido(
                                quantidadeAcrescentada,
                                itemPedido.getProduto(),
                                itemPedido.getPedido()
                        )
                );
            }
            itemPedido.setQuantidade(dados.quantidade());
        }
    }

    private static NaoEncontradoException getItemNaoEncontradoException(UUID id) {
        return new NaoEncontradoException("Não existe item com Id: " + id.toString());
    }
}
