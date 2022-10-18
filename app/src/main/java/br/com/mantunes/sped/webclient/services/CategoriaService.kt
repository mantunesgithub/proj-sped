package br.com.mantunes.sped.webclient.services
import br.com.mantunes.sped.model.Categoria
import retrofit2.Call
import retrofit2.http.*

interface CategoriaService {

    @GET("categorias")
    fun buscaTodas() : Call<List<Categoria>>

    @POST("categorias")
    fun salva(@Body categoria: Categoria) : Call<Categoria>

}
