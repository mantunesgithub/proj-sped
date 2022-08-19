package br.com.mantunes.sped.database.dao

import androidx.room.Dao
import androidx.room.Insert
import br.com.mantunes.sped.model.Pedido

@Dao
interface PedidoDAO {
    @Insert

    fun salva(pedido: Pedido) : Long
}