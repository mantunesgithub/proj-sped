package br.com.mantunes.sped.model
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
@Parcelize
@Entity(
    foreignKeys = [ForeignKey(
        entity = Produto::class,
        parentColumns = ["id"],
        childColumns = ["categoriaId"]
    )],
    indices = [Index("categoriaId")]
)

data class Produto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String,
    val preco: BigDecimal,
    val imagem: String? = null,
    val categoriaId : Long
): Parcelable
