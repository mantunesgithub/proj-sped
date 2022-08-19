package br.com.mantunes.sped.model
import android.os.Parcelable
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
@Parcelize
class PedidoDTO(
    var tipoPagamento: TIPO_PAGAMENTO,
    var numeroCartao: Long,
    var dataValidadeCartao: String,
    var cvcCartao: Int,
    var numeroParcelasCartao: Int,
    var dataVencimentoBoleto: String,
    var chavePix: String,
): Parcelable {
    lateinit var clienteLogado: Cliente
    lateinit var enderecoEscolhido: Endereco
    var listaCarrinhoDoCliente: MutableList<Carrinho> = mutableListOf()

    constructor(
          tipoPagamento: TIPO_PAGAMENTO,
          numeroCartao: Long,
          dataValidadeCartao: String,
          cvcCartao: Int,
          numeroParcelasCartao: Int,
          dataVencimentoBoleto: String,
          chavePix: String,
          clienteLogado: Cliente,
          enderecoEscolhido: Endereco,
          listaCarrinhoDoCliente: MutableList<Carrinho> )
          : this (
            tipoPagamento,numeroCartao,dataValidadeCartao,cvcCartao,numeroParcelasCartao,
            dataVencimentoBoleto,chavePix
            ) {
            this.clienteLogado = clienteLogado
            this.enderecoEscolhido = enderecoEscolhido
            this.listaCarrinhoDoCliente = listaCarrinhoDoCliente
          }
    fun getCarrinho() : List<Carrinho> = listaCarrinhoDoCliente
}

