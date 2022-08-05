package br.com.mantunes.sped.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.formatParaMoedaBrasileira
import br.com.mantunes.sped.extensions.tentaCarregarImagem
import br.com.mantunes.sped.model.Produto
import kotlinx.android.synthetic.main.item_produto.view.*

class ProdutosAdapter(
    private val context: Context,
    private val produtos: MutableList<Produto> = mutableListOf(),
    var onItemClickListener: (produto: Produto) -> Unit = {}

) : RecyclerView.Adapter<ProdutosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewCriada = LayoutInflater.from(context).inflate(
            R.layout.item_produto,
            parent,
            false
        )
        return ViewHolder(viewCriada)
    }

    override fun getItemCount() = produtos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(produtos[position])
    }

    fun atualiza(produtosNovos: List<Produto>) {
        notifyItemRangeRemoved(0, produtos.size)
        produtos.clear()
        produtos.addAll(produtosNovos)
        notifyItemRangeInserted(0, produtos.size)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var produto: Produto
        private val campoNome by lazy { itemView.item_produto_nome }
        private val campoDescricao by lazy { itemView.item_produto_descricao }
        private val campoPreco by lazy { itemView.item_produto_preco }
        private val campoImagemProduto by lazy { itemView.item_produto_imagem }

        init {
            itemView.setOnClickListener {
                if (::produto.isInitialized) {
                    onItemClickListener(produto)
                }
            }
        }

        fun vincula(produto: Produto) {
            this.produto = produto
            campoNome.text = produto.nome
            campoDescricao.text = produto.descricao
            campoPreco.text = produto.preco.formatParaMoedaBrasileira()
            campoImagemProduto.tentaCarregarImagem(produto.imagem)
            if (produto.imagem?.isEmpty() == true) {
                campoImagemProduto.visibility = View.GONE
            } else {
                campoImagemProduto.visibility = View.VISIBLE
            }
        }

    }
}
