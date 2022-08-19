package br.com.mantunes.sped.database.dao

import androidx.room.Dao
import androidx.room.Insert
import br.com.mantunes.sped.model.ItensDoPedido

@Dao
interface ItensDoPedidoDAO {
    @Insert
    fun salva(itensDoPedido: ItensDoPedido) : Long
}