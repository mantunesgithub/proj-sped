package br.com.mantunes.sped.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.formatParaMoedaBrasileira
import br.com.mantunes.sped.extensions.tentaCarregarImagem
import br.com.mantunes.sped.model.Carrinho
import kotlinx.android.synthetic.main.item_pedido_itens.view.*
import kotlinx.android.synthetic.main.item_produto.view.*
import java.math.BigDecimal

class ItensDoPedidoAdapter(

    private val context: Context,
    private val carrinho: MutableList<Carrinho> = mutableListOf(),
    var onItemClickListener: (carrinho: Carrinho) -> Unit = {}

    ) : RecyclerView.Adapter<ItensDoPedidoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewCriada = LayoutInflater.from(context).inflate(
            R.layout.item_pedido_itens,
            parent,
            false
        )
        return ViewHolder(viewCriada)
    }

    override fun getItemCount() = carrinho.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(carrinho[position])
    }

    fun atualiza(carrinhoNovo: List<Carrinho>) {
        notifyItemRangeRemoved(0, carrinho.size)
        carrinho.clear()
        carrinho.addAll(carrinhoNovo)
        notifyItemRangeInserted(0, carrinho.size)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var carrinho: Carrinho
        private val campoNome by lazy { itemView.item_pedido_nome_produto }
        private val campoPreco by lazy { itemView.item_pedido_preco_produto }
        private val campoQuantidade by lazy { itemView.item_pedido_quantidade }
        private val campoTotalProduto by lazy { itemView.item_pedido_total_produto }
        private val campoImagemProduto by lazy { itemView.item_pedido_imagem}
        init {
            itemView.setOnClickListener {
                if (::carrinho.isInitialized) {
                    onItemClickListener(carrinho)
                }
            }
        }

        fun vincula(carrinho: Carrinho) {
            this.carrinho = carrinho
            campoQuantidade.text = carrinho.quantidade.toString()
            campoNome.text = carrinho.nome
            campoPreco.text = carrinho.preco.formatParaMoedaBrasileira()
            val totalProduto = calculaSubTotal(carrinho.preco, carrinho.quantidade)
            campoTotalProduto.text = totalProduto.formatParaMoedaBrasileira()
            campoImagemProduto.tentaCarregarImagem(carrinho.imagem)
            if (carrinho.imagem?.isEmpty() == true) {
                campoImagemProduto.visibility = View.GONE
            } else {
                campoImagemProduto.visibility = View.VISIBLE
            }
        }
    }
    fun calculaSubTotal(valor: BigDecimal?, quantidade: Int): BigDecimal {
        var valorDouble: Double? = valor?.toDouble()
        return valorDouble?.let {
            BigDecimal(it * quantidade)
        } ?: BigDecimal.ZERO
    }

}
