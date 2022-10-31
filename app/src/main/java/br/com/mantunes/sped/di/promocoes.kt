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
            "de R$ 200,00", " por R$ 185,00"
        ),
        Promocao(
            "Informatica",
            R.drawable.imagem_notebook,
            "Notebook",
            "Notebook Dell Inspiron 15 3000 a0500-MM10S 15.6 FHD AMD Ryzen 5 8GB 256GB SSD Windows 11 Prata" +
                    "A tela Full HD de 15,6”, antirreflexo de alta definição com três lados de bordas finas" ,
            "de R$ 4000,00", " por R$ 3185,00"
        ),
        Promocao(
            "Moda Masculino",
            R.drawable.imagem_camisas,
            "Camiseta Masculina Básica Bluhen Nicarágua",
            "A Camiseta Masculina Básica Bluhen é confeccionada em malha na cor azul, contendo modelagem" +
                    " tradicional, gola em ribana para melhor ajuste, mangas curtas com excelente caimento." ,
            "de R$ 80,00"," por R$ 65,00"
        )
    )
    return listaPromocoes
}