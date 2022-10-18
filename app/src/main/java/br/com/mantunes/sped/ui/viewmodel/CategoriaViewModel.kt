package br.com.mantunes.sped.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.repository.CategoriaRepository
import br.com.mantunes.sped.repository.Resource

class CategoriaViewModel(private val repository: CategoriaRepository) : ViewModel() {

    fun buscaTodos(): LiveData<Resource<List<Categoria>?>> {
        return  repository.buscaTodos()
    }
}
