package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.model.Pagamento
import br.com.mantunes.sped.model.Pedido
import br.com.mantunes.sped.repository.PagamentoRepository
import br.com.mantunes.sped.repository.PedidoRepository
import br.com.mantunes.sped.repository.ProdutoRepository

class PedidoViewModel(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {
    fun salva(pedido: Pedido) =
        pedidoRepository.salva(pedido)
}