package br.com.mantunes.sped.di

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.mantunes.sped.database.AppDatabase
import br.com.mantunes.sped.database.dao.*
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.repository.*
import br.com.mantunes.sped.ui.fragment.*
import br.com.mantunes.sped.ui.recyclerview.adapter.CarrinhoAdapter
import br.com.mantunes.sped.ui.recyclerview.adapter.CategoriaAdapter
import br.com.mantunes.sped.ui.recyclerview.adapter.ItensDoPedidoAdapter
import br.com.mantunes.sped.ui.recyclerview.adapter.ProdutosAdapter
import br.com.mantunes.sped.ui.viewmodel.*
import br.com.mantunes.sped.webclient.CategoriaWebClient
import br.com.mantunes.sped.webclient.EnderecoWebViacep
import br.com.mantunes.sped.webclient.ProdutoWebClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.math.BigDecimal

private const val NOME_BANCO_DE_DADOS = "sped.db"
private const val NOME_BANCO_DE_DADOS_TESTE = "sped-test.db"

val testeDatabaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            NOME_BANCO_DE_DADOS_TESTE
        ).fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(IO).launch {
                            val daoCategoria : CategoriaDAO by inject ()

                            val listaCategorias: List<Categoria> = listOf(
                            Categoria(
                                id = 0,
                                nome = "Esportes",
                                descricao = "Material de futebol",
                                imagem = "https://images.pexels.com/photos/46798/the-ball-stadion-football-the-pitch-46798.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                            ),
                            Categoria(
                                id = 0,
                                nome = "Informatica",
                                descricao = "Material de informatica",
                                imagem = "https://images.pexels.com/photos/8720619/pexels-photo-8720619.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                            ))

 //                       daoCategoria.salva(listaCategorias)
//
//                        }else {
//                            Log.e("AppModule", "ATENÇÃO: Erro na Inicialização" )
//                        }

                    }
                }
            }).build()
    }
}

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            NOME_BANCO_DE_DADOS
        ).build()
    }
}

val daoModule = module {
    single<CategoriaDAO> { get<AppDatabase>().categoriaDao() }
    single<ProdutoDAO> { get<AppDatabase>().produtoDao() }
    single<ClienteDAO> { get<AppDatabase>().clienteDao() }
    single<CarrinhoDAO> { get<AppDatabase>().carrinhoDao() }
    single<PagamentoDAO> { get<AppDatabase>().pagamentoDao() }
    single<EnderecoDAO> { get<AppDatabase>().enderecoDao() }
    single<PedidoDAO> { get<AppDatabase>().pedidoDao() }
    single<ItensDoPedidoDAO> { get<AppDatabase>().itensDoPedido() }
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(get()) }

    single<EnderecoViacepRepository> { EnderecoViacepRepository(get())}
    single<CategoriaRepository> { CategoriaRepository(get(),get())}
    single<EnderecoRepository> { EnderecoRepository(get()) }
    single<ProdutoRepository> { ProdutoRepository(get(),get()) }
    single<PagamentoRepository> { PagamentoRepository(get()) }
    single<PedidoRepository> { PedidoRepository(get()) }
    single<ItensDoPedidoRepository> { ItensDoPedidoRepository(get()) }
    single<CarrinhoRepository> { CarrinhoRepository(get()) }
    single<ClienteRepository> { ClienteRepository(get()) }
    single<LoginRepository> { LoginRepository(get()) }
    single<CategoriaWebClient> { CategoriaWebClient() }
    single<ProdutoWebClient> { ProdutoWebClient() }
    single<EnderecoWebViacep> { EnderecoWebViacep() }
}

val uiModule = module {
    factory<ClienteFormCadastroPerfilPFFragment> { ClienteFormCadastroPerfilPFFragment() }
    factory<ClienteFormCadastroFragment> { ClienteFormCadastroFragment() }
    factory<DetalhesProdutoFragment> { DetalhesProdutoFragment() }
    factory<ListaCategoriasFragment> { ListaCategoriasFragment() }
    factory<ListaProdutosFragment> { ListaProdutosFragment() }
    factory<EnderecoFragment> { EnderecoFragment() }
    factory<PagamentoComBoletoFragment> { PagamentoComBoletoFragment() }
    factory<PagamentoComPixFragment> { PagamentoComPixFragment() }
    factory<PagamentoComCartaoFragment> { PagamentoComCartaoFragment() }
    factory<FormaPagamentoFragment> { FormaPagamentoFragment() }
    factory<PedidoFragment> { PedidoFragment() }
    factory<ListaCarrinhoFragment> { ListaCarrinhoFragment() }
    factory<PagamentoDoPedidoConfirmadoFragment> { PagamentoDoPedidoConfirmadoFragment() }
    factory<PagamentoRegistradoFragment> { PagamentoRegistradoFragment() }
    factory<AdicionaNoCarrinhoFragment> { AdicionaNoCarrinhoFragment() }
    factory<AtualizaMaisUmNoCarrinhoFragment> { AtualizaMaisUmNoCarrinhoFragment() }
    factory<ProdutosAdapter> { ProdutosAdapter(get()) }
    factory<CategoriaAdapter> { CategoriaAdapter(get()) }
    factory<CarrinhoAdapter> { CarrinhoAdapter(get()) }
    factory<ItensDoPedidoAdapter> { ItensDoPedidoAdapter(get()) }
}

val viewModelModule = module {
    viewModel<PedidoViewModel> { PedidoViewModel(get()) }
    viewModel<ItensDoPedidoViewModel> { ItensDoPedidoViewModel(get()) }
    viewModel<LoginViewModel> { LoginViewModel(get()) }
    viewModel<CategoriaViewModel> { CategoriaViewModel(get()) }
    viewModel<ProdutosViewModel> { (categoriaId: Long) -> ProdutosViewModel(categoriaId, get()) }
    viewModel<EnderecoViacepViewModel> { EnderecoViacepViewModel(get()) }
    viewModel<EnderecoViewModel> { (clienteId: Long) -> EnderecoViewModel(clienteId, get()) }
    viewModel<CarrinhoViewModel> { (clienteId: Long) -> CarrinhoViewModel(clienteId, get()) }
    viewModel<ClienteViewModel> { (clienteId: Long) -> ClienteViewModel(clienteId, get()) }
    viewModel<DetalhesProdutoViewModel> { (id: Long) -> DetalhesProdutoViewModel(id, get()) }
    viewModel<PagamentoViewModel> { PagamentoViewModel(get()) }
}