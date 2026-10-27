package br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application.dto.pedido;

import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application.dto.item.ItemResumoDTO;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.application.dto.usuario.UsuarioDTO;
import br.com.carloseduscc.projetoestudos.SistemaPedidosJPA.model.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "Pedido")
public record PedidoDTO(
        UUID id,
        @Schema(description = "Data e hora de quando o pedido foi aberto")
        LocalDateTime dataHoraPedido,
        @Schema(description = "Status do pedido")
        StatusPedido status,
        @Schema(description = "Lista de itens do pedido")
        List<ItemResumoDTO> itens,
        @Schema(description = "Nome do cliente")
        String nomeCliente
) {
}