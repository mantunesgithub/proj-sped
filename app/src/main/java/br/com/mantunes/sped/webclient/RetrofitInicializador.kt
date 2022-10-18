package br.com.mantunes.sped.webclient
import br.com.mantunes.sped.webclient.services.CategoriaService
import br.com.mantunes.sped.webclient.services.ProdutoService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://192.168.0.12:8081/"

class RetrofitInicializador {

    private val client by lazy {
        val interceptador = HttpLoggingInterceptor()
        interceptador.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(interceptador)
            .build()
    }
    private val retrofit: Retrofit by lazy {
         Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
    }

    val categoriaService:CategoriaService by lazy {
        retrofit.create(CategoriaService::class.java)
    }
    val produtoService: ProdutoService by lazy {
        retrofit.create(ProdutoService::class.java)
    }

}