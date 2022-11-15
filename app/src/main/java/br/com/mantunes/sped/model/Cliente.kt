package br.com.mantunes.sped.model

import android.net.Uri
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
    val senha: String,
    val dataNascimento: String,
    val naturalidade: String,
    val genero: String,
    val nomeFantasia: String,
    val incricaoEstatual: String,
    val isentoIE: String,
    val telefoneRes: String,
    val telefoneCel: String,
    val telefoneOutro: String,
    val caminhoFoto : String? = null
)
