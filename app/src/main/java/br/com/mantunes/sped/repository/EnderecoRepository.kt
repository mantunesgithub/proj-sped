package br.com.mantunes.sped.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.mantunes.sped.database.dao.CarrinhoDAO
import br.com.mantunes.sped.database.dao.EnderecoDAO
import br.com.mantunes.sped.database.dao.ProdutoDAO
import br.com.mantunes.sped.model.Carrinho
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.model.Produto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
class EnderecoRepository(private val dao: EnderecoDAO) {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun buscaTodos(clienteId: Long): LiveData<List<Endereco>> = dao.buscaTodos(clienteId)

    fun buscaPorId(id: Long): LiveData<Endereco> = dao.buscaPorId(id)

    fun salva(endereco: Endereco): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                try {
                    val idEndereco = dao.salva(endereco)
                    liveDate.postValue(Resource(idEndereco))
                } catch (e: Exception) {
                    Log.e("Endereco Repository", "Erro ao Salvar Endereço: $e", )
                }
            }
        }
    }
    fun remove(endereco: Endereco): LiveData<Resource<Long>> {
        return MutableLiveData<Resource<Long>>().also { liveDate ->
            scope.launch {
                dao.remove(endereco)
            }
        }
    }
}
