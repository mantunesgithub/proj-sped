package br.com.mantunes.sped.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.mantunes.sped.R
import br.com.mantunes.sped.extensions.tentaCarregarImagem
import br.com.mantunes.sped.model.Categoria
import kotlinx.android.synthetic.main.item_categoria.view.*

class CategoriaAdapter(
    private val context: Context,
    private val categorias: MutableList<Categoria> = mutableListOf(),
    var onItemClickListener: (categoria: Categoria) -> Unit = {}

) : RecyclerView.Adapter<CategoriaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewCriada = LayoutInflater.from(context).inflate(
            R.layout.item_categoria,
            parent,
            false
        )
        return ViewHolder(viewCriada)
    }

    override fun getItemCount() = categorias.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.vincula(categorias[position])
    }

    fun atualiza(categoriasNovos: List<Categoria>) {
        notifyItemRangeRemoved(0, categorias.size)
        categorias.clear()
        categorias.addAll(categoriasNovos)
        notifyItemRangeInserted(0, categorias.size)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var categoria: Categoria
        private val campoNome by lazy { itemView.item_categoria_nome }
        private val campoDescricao by lazy { itemView.item_categoria_descricao }

        init {
            itemView.setOnClickListener {
                if (::categoria.isInitialized) {
                    onItemClickListener(categoria)
                }
            }
        }

        fun vincula(categoria: Categoria) {
            this.categoria = categoria
            campoNome.text = categoria.nome
            campoDescricao.text = categoria.descricao
            val visibilidade = if (categoria.imagem != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            itemView.item_categoria_imagem.visibility = visibilidade
            itemView.item_categoria_imagem.tentaCarregarImagem(categoria.imagem)
        }
    }
}
