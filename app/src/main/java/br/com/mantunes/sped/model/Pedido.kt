package br.com.mantunes.sped.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    foreignKeys = [ForeignKey(
        entity = Pedido::class,
        parentColumns = ["id"],
        childColumns = ["idCliente"]
    )],
    indices = [Index("idCliente")]
)
class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val idCliente: Long,
    val dataPedido: String,
    val idEnderecoEntrega: Long
)
