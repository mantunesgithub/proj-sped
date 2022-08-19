package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.model.ItensDoPedido
import br.com.mantunes.sped.model.Pagamento
import br.com.mantunes.sped.model.Pedido
import br.com.mantunes.sped.repository.ItensDoPedidoRepository
import br.com.mantunes.sped.repository.PagamentoRepository
import br.com.mantunes.sped.repository.ProdutoRepository

class ItensDoPedidoViewModel(
    private val itensDoPedidoRepository: ItensDoPedidoRepository
) : ViewModel() {
    fun salva(itensDoPedido: ItensDoPedido) =
        itensDoPedidoRepository.salva(itensDoPedido)
}