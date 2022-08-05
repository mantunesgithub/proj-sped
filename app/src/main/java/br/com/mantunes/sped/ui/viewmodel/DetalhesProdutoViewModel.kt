package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.repository.ProdutoRepository

class DetalhesProdutoViewModel(
    produtoId: Long,
    repository: ProdutoRepository
) : ViewModel() {

    val produtoEncontrado = repository.buscaPorId(produtoId)

}