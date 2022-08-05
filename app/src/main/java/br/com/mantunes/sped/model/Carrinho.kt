package br.com.mantunes.sped.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
@Parcelize
@Entity(primaryKeys = ["idCliente", "produtoId"],
    foreignKeys = [ForeignKey(
        entity = Carrinho::class,
        parentColumns = ["idCliente", "produtoId"],
        childColumns = ["idCliente", "produtoId"]
    )],
    indices = [Index("produtoId")]
)
data class Carrinho(
    val idCliente: Long,
    val produtoId: Long,
    val nome: String,
    val preco: BigDecimal,
    var quantidade: Int
): Parcelable
