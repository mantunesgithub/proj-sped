package br.com.mantunes.sped.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.formatParaMoedaBrasileira
import br.com.mantunes.sped.model.Carrinho
import kotlinx.android.synthetic.main.item_carrinho.view.*

class CarrinhoAdapter(
    private val context: Context,
    private val carrinho: MutableList<Carrinho> = mutableListOf(),
    var onItemClickListener: (carrinho: Carrinho) -> Unit = {},
    var onItemClickListenerAdicionado: (carrinho: Carrinho) -> Unit = {},
    var onItemClickListenerRemovido: (carrinho: Carrinho) -> Unit = {},
    var onItemClickListenerDelete: (carrinho: Carrinho) -> Unit = {},

    ) : RecyclerView.Adapter<CarrinhoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewCriada = LayoutInflater.from(context).inflate(
            R.layout.item_carrinho,
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
        private val campoNome by lazy { itemView.item_carrinho_produto_nome }
        private val campoPreco by lazy { itemView.item_carrinho_produto_preco }
        private val campoQuantidade by lazy { itemView.item_carrinho_quantidade }

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
            itemView.item_carrinho_adicionar.setImageResource(R.drawable.ic_action_adicionar_carrinho)
            itemView.item_carrinho_remover.setImageResource(R.drawable.ic_action_remover_carrinho)
            itemView.item_carrinho_delete.setImageResource(R.drawable.ic_action_delete)
            itemView.item_carrinho_adicionar.setOnClickListener {
                it?.let { onItemClickListenerAdicionado(carrinho) }
            }
            itemView.item_carrinho_remover.setOnClickListener {
                it?.let { onItemClickListenerRemovido(carrinho) }
            }
            itemView.item_carrinho_delete.setOnClickListener {
                it?.let { onItemClickListenerDelete(carrinho) }
            }
        }
    }
}
