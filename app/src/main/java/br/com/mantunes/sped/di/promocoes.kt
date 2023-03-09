package br.com.mantunes.sped.di

import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.Promocao

fun criaPromocoes() : List <Promocao> {
    val listaPromocoes = listOf(
        Promocao(
            "Esportes",
            R.drawable.imagem_bola,
            "Bola Futebol",
            "Bola de futebol de campo e socciet da marca penalt",
            "de R$ 200,00 ",
            "por R$ 185,00"
        ),
        Promocao(
            "Informatica",
            R.drawable.imagem_notebook,
            "Notebook",
            "Notebook Dell Inspiron 15 8GB 256GB SSD 11 Prata",
            "de R$ 4.000,00 ",
            "por R$ 3.185,00"
        ),
        Promocao(
            "Moda Masculino",
            R.drawable.imagem_camisa,
            "Camiseta Masculina Básica Bluhen Nicarágua",
            "A Camiseta Masculina Básica Bluhen é confeccionada em malha na cor azul, contendo modelagem",
            "de R$ 80,00 ",
            "por R$ 65,00"
        )
    )
    return listaPromocoes
}