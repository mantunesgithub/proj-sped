package br.com.mantunes.sped.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Telefone::class,
        parentColumns = ["id"],
        childColumns = ["clienteId"]
    )],
    indices = [Index("clienteId")]
)
data class Telefone (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val numero: String,
    val clienteId: Long
)