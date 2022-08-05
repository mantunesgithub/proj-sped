package br.com.mantunes.sped.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val descricao: String,
    val imagem: String? = null,
)
