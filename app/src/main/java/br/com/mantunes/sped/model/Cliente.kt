package br.com.mantunes.sped.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cliente (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val tipo: String,
    val cnpjCpf: String,
    val email: String,
    val senha: String
)
