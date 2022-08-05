package br.com.mantunes.sped.database.dao

import androidx.room.Dao
import androidx.room.Insert
import br.com.mantunes.sped.model.Pagamento

@Dao
interface PagamentoDAO {

    @Insert
    fun salva(pagamento: Pagamento) : Long

}