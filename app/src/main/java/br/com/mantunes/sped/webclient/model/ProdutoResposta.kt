package br.com.mantunes.sped.webclient.model

import br.com.mantunes.sped.model.Produto
import java.math.BigDecimal
import java.util.*

class ProdutoResposta(
    val id: Integer?,
    val nome: String?,
    val descricao: String?,
    val imagem: String? ,
    val preco: Double?
) {
    val produto : Produto get() = Produto (
        id = 0,
        nome = nome ?: "",
        descricao = descricao ?: "",
        imagem = imagem ?: "",
        preco = (preco ?: 0.0) as BigDecimal,
        categoriaId = 0
    )
}