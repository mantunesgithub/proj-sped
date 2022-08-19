package br.com.mantunes.sped.di

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.mantunes.sped.database.AppDatabase
import br.com.mantunes.sped.database.dao.*
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.model.Endereco
import br.com.mantunes.sped.model.Produto
import br.com.mantunes.sped.repository.*
import br.com.mantunes.sped.ui.fragment.PagamentoComBoletoFragment
import br.com.mantunes.sped.ui.fragment.PagamentoComPixFragment
import br.com.mantunes.sped.ui.fragment.ClienteFormCadastroFragment
import br.com.mantunes.sped.ui.fragment.*
import br.com.mantunes.sped.ui.recyclerview.adapter.CarrinhoAdapter
import br.com.mantunes.sped.ui.recyclerview.adapter.CategoriaAdapter
import br.com.mantunes.sped.ui.recyclerview.adapter.ItensDoPedidoAdapter
import br.com.mantunes.sped.ui.recyclerview.adapter.ProdutosAdapter
import br.com.mantunes.sped.ui.viewmodel.*
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
                        val daoCategoria: CategoriaDAO by inject()
                        daoCategoria.salva(
                            Categoria(
                                nome = "Esportes",
                                descricao = "Material de futebol",
                                imagem = "https://images.pexels.com/photos/46798/the-ball-stadion-football-the-pitch-46798.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                            ),
                            Categoria(
                                nome = "Informatica",
                                descricao = "Material de informatica",
                                imagem = "https://images.pexels.com/photos/8720619/pexels-photo-8720619.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                            ),
                            Categoria(
                                nome = "Papelaria",
                                descricao = "Material Escolar e Livraria",
                                imagem = "https://images.pexels.com/photos/53874/pexels-photo-53874.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=699.825&fit=crop&h=1133.05"
                            )
                        )
                        val daoEndereco: EnderecoDAO by inject()
                        daoEndereco.salva(
                            Endereco(
                                logradouro = "Avenida Matos",
                                numero = "105",
                                complemento = "Sala 800",
                                bairro = "Centro",
                                cep = "38777012",
                                cidade = "São Paulo",
                                estado = "São Paulo",
                                pais = "Brasil",
                                clienteId = 1
                            ),
                            Endereco(
                                logradouro = "Rua Braga",
                                numero = "202",
                                complemento = "Apto 101 - Torre 4",
                                bairro = "Vila Luzitania",
                                cep = "09725160",
                                cidade = "Sao Bernardo do Campo",
                                estado = "São Paulo",
                                pais = "Brasil",
                                clienteId = 1
                            ),
                            Endereco(
                                logradouro = "Rua Jurubatuba",
                                numero = "2000",
                                complemento = "Sala 800",
                                bairro = "Centro",
                                cep = "0925000",
                                cidade = "São Bernardo do Campo",
                                estado = "São Paulo",
                                pais = "Brasil",
                                clienteId = 1
                            ),
                            Endereco(
                                logradouro = "Alameda das Oliveiras",
                                numero = "2000",
                                complemento = "",
                                bairro = "Jerusalem",
                                cep = "0925000",
                                cidade = "São Bernardo do Campo",
                                estado = "São Paulo",
                                pais = "Brasil",
                                clienteId = 2
                            ),
                            Endereco(
                                logradouro = "Av Capitao Casa",
                                numero = "110",
                                complemento = "",
                                bairro = "Assunção",
                                cep = "0925000",
                                cidade = "São Bernardo do Campo",
                                estado = "São Paulo",
                                pais = "Brasil",
                                clienteId = 2
                            )
                        )
                        val dao : ProdutoDAO by inject ()
                        dao.salva(
                            Produto(
                                nome = "Bola de futebol",
                                descricao = "Bola de futebol salão cor branca de couro " +
                                        "marca Top" +
                                        "Versão Copa do Mundo",
                                preco = BigDecimal("100"),
                                imagem = "https://images.pexels.com/photos/47730/the-ball-stadion-football-the-pitch-47730.jpeg?auto=compress&cs=tinysrgb&w=1600",
                                categoriaId = 1
                            ), Produto(
                                nome = "Camisa",
                                descricao = "Camisa de futebol salão cor verde do Palmeiras",
                                preco = BigDecimal("80"),
                                imagem = "https://images.pexels.com/photos/8404030/pexels-photo-8404030.jpeg?auto=compress&cs=tinysrgb&w=1600",
                                categoriaId = 1,
                            ),
                            Produto(
                                nome = "Chuteira",
                                descricao = "Chuteira de futebol salão cor verde do Palmeiras",
                                preco = BigDecimal("120"),
                                imagem = "https://images.pexels.com/photos/774321/pexels-photo-774321.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                                categoriaId = 1
                            ), Produto(
                                nome = "Meião de futebol",
                                descricao = "Meião de futebol para campo da marca topper",
                                preco = BigDecimal("60"),
                                imagem = "https://images.pexels.com/photos/8941581/pexels-photo-8941581.jpeg?auto=compress&cs=tinysrgb&w=1600",
                                categoriaId = 1
                            ), Produto(
                                nome = "Mouse",
                                descricao = "Mouse sansung",
                                preco = BigDecimal("160"),
                                imagem = "https://images.pexels.com/photos/2115256/pexels-photo-2115256.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                                categoriaId = 2
                            ), Produto(
                                nome = "Teclado",
                                descricao = "Teclado sansung",
                                preco = BigDecimal("200"),
                                imagem = "https://images.pexels.com/photos/1772123/pexels-photo-1772123.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                                categoriaId = 2
                            ), Produto(
                                nome = "Pendrive",
                                descricao = "Pendrive king",
                                preco = BigDecimal("100"),
                                imagem = "https://images.pexels.com/photos/7442196/pexels-photo-7442196.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                                categoriaId = 2
                            ), Produto(
                                nome = "Caderno",
                                descricao = "Caderno Aspiral 200 fl",
                                preco = BigDecimal("40"),
                                imagem = "https://images.pexels.com/photos/315791/pexels-photo-315791.jpeg?auto=compress&cs=tinysrgb&w=1600",
                                categoriaId = 3
                            ), Produto(
                                nome = "Canetas",
                                descricao = "Jogo de Caneta com tinta azul ponta macia",
                                preco = BigDecimal("50"),
                                imagem = "https://media.istockphoto.com/photos/set-of-eight-different-pens-picture-id183057646?b=1&k=20&m=183057646&s=612x612&w=0&h=vioK9G4hWmYGECyLzd12MMy9qFf-Z64K5xf1WRaZffM=",
                                categoriaId = 3
                            )
                        )
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

    single<EnderecoRepository> { EnderecoRepository(get()) }
    single<ProdutoRepository> { ProdutoRepository(get()) }
    single<CategoriaRepository> { CategoriaRepository(get()) }
    single<PagamentoRepository> { PagamentoRepository(get()) }
    single<PedidoRepository> { PedidoRepository(get()) }
    single<ItensDoPedidoRepository> { ItensDoPedidoRepository(get()) }
    single<CarrinhoRepository> { CarrinhoRepository(get()) }
    single<ClienteRepository> { ClienteRepository(get()) }
}

val uiModule = module {
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
    viewModel<CategoriaViewModel> { CategoriaViewModel(get()) }
    viewModel<ProdutosViewModel> { (categoriaId: Long) -> ProdutosViewModel(categoriaId, get()) }
    viewModel<EnderecoViewModel> { (clienteId: Long) -> EnderecoViewModel(clienteId, get()) }
    viewModel<CarrinhoViewModel> { (clienteId: Long) -> CarrinhoViewModel(clienteId, get()) }
    viewModel<ClienteViewModel> { (clienteId: Long) -> ClienteViewModel(clienteId, get()) }
    viewModel<DetalhesProdutoViewModel> { (id: Long) -> DetalhesProdutoViewModel(id, get()) }
    viewModel<PagamentoViewModel> { PagamentoViewModel(get()) }
}