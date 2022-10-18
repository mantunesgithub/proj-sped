package br.com.mantunes.sped.webclient.services
import br.com.mantunes.sped.model.Produto
import retrofit2.Call
import retrofit2.http.*

interface ProdutoService {

    @GET("produtos/categoria/{id}")
    fun buscaTodas(@Path("id") id: Long) : Call<List<Produto>>

    @POST("produtos")
    fun salva(@Body produto: Produto) : Call<Produto>

}
