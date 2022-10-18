package br.com.mantunes.sped.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    foreignKeys = [ForeignKey(
        entity = Endereco::class,
        parentColumns = ["id"],
        childColumns = ["clienteId"]
    )],
    indices = [Index("clienteId")]
)

data class Endereco (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val logradouro: String,
    val numero: String,
    val complemento: String,
    val bairro: String,
    val cep: String,
    val cidade: String,
    val estado: String,
    val pais: String,
    val tipo: String,
    val clienteId : Long
): Parcelable