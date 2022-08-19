package br.com.mantunes.sped.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.mantunes.sped.model.enum.TIPO_PAGAMENTO
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
@Entity(
    foreignKeys = [ForeignKey(
        entity = ItensDoPedido::class,
        parentColumns = ["id"],
        childColumns = ["idPedido"]
    )],
    indices = [Index("idPedido")]
)
data class ItensDoPedido(
    @PrimaryKey(autoGenerate = true)
    val id: Long ,
    val idProduto: Long,
    val preco: BigDecimal,
    val desconto: BigDecimal,
    val quantidade: Int,
    val idPedido: Long,
    val imagem: String?
)
