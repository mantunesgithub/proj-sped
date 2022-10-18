package br.com.mantunes.sped.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.mantunes.sped.database.converter.ConversorBigDecimal
import br.com.mantunes.sped.database.dao.*
import br.com.mantunes.sped.model.*

@Database(
    version = 1,
    entities = [Produto::class, Pagamento::class, Categoria::class,
               Carrinho::class, Cliente::class, Endereco::class,
               Pedido::class, ItensDoPedido::class],
    exportSchema = false
)
@TypeConverters(ConversorBigDecimal::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carrinhoDao(): CarrinhoDAO
    abstract fun produtoDao(): ProdutoDAO
    abstract fun clienteDao(): ClienteDAO
    abstract fun enderecoDao(): EnderecoDAO
    abstract fun categoriaDao(): CategoriaDAO
    abstract fun pagamentoDao(): PagamentoDAO
    abstract fun pedidoDao(): PedidoDAO
    abstract fun itensDoPedido(): ItensDoPedidoDAO
}