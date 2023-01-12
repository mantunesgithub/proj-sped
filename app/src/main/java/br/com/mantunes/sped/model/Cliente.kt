package br.com.mantunes.sped.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
private const val EMAIL_REGEX= "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
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
) {
    @Ignore
    fun ehValido() = emailValido && senhaValida

    @Ignore
    val emailValido = email.matches(Regex(EMAIL_REGEX))

    @Ignore
    val senhaValida = senha.count() >= 2
}
