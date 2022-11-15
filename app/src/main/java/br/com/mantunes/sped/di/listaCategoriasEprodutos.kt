package br.com.mantunes.sped.di

import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.model.Produto
import java.math.BigDecimal

fun criaCategorias(): List<Categoria> {
    val listaCategorias: List<Categoria> = listOf(
        Categoria(
            id = 0,
            nome = "Esportes",
            descricao = "Material de esportes",
            imagem = "https://images.pexels.com/photos/931325/pexels-photo-931325.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500"
        ),
        Categoria(
            id = 0,
            nome = "Informática",
            descricao = "Material de informatica",
            imagem = "https://images.pexels.com/photos/8721342/pexels-photo-8721342.jpeg?auto=compress&cs=tinysrgb&w=600"
        )
    )
    return listaCategorias
}
fun criaProdutos(): List<Produto> {
    val listaProdutos: List<Produto> = listOf(
        Produto(
            id = 0,
            nome = "Bola de Futebol da Alemanha - Nº.5 - Futebol e Magia",
            descricao = "Desenvolvido para chutes precisos e voos excelentes durante as partidas ou treinos. Ela possui 32 gomos costurados e traz revestimento externo em PVC, além de apresentar um design moderno que enaltece o escudo da equipe. Material em PVC, Medidas: 68 cm de circunferência, Peso: 320 gramas.Idade Recomendada: A partir de 04 anos de idade.Contém: 01 - Bola de Futebol de Campo No 5.",
            imagem = "https://images.pexels.com/photos/46798/the-ball-stadion-football-the-pitch-46798.jpeg?auto=compress&cs=tinysrgb&w=600",
            preco = BigDecimal("100.00"),
            categoriaId = 1L
        ),
        Produto(
            id = 0,
            nome = "Chuteira",
            descricao = "Chuteira Society Adidas Artilheira V - Preto+Branco",
            imagem = "https://images.pexels.com/photos/774321/pexels-photo-774321.jpeg?auto=compress&cs=tinysrgb&w=600",
            preco = BigDecimal("200.00"),
            categoriaId = 1L
        ),
        Produto(
            id = 0,
            nome = "Camisa de Futebol",
            descricao = "Camisa Futebol I 22/23 Libertadores s/n° Torcedor Puma Masculina ",
            imagem = "https://images.pexels.com/photos/5246963/pexels-photo-5246963.jpeg?auto=compress&cs=tinysrgb&w=600",
            preco = BigDecimal("500.00"),
            categoriaId = 1L
        ),
        Produto(
            id = 0,
            nome = "Notebook",
            descricao = "Notebook Dell com 16gb memoria, 1 tera HD, placa grafica",
            imagem = "https://images.pexels.com/photos/18105/pexels-photo.jpg?auto=compress&cs=tinysrgb&w=600",
            preco = BigDecimal("3000.00"),
            categoriaId = 2L
        ),
        Produto(
            id = 0,
            nome = "Impressora Epson",
            descricao = "Impressora Multifuncional Epson Ecotank L3210 - Tanque de Tinta Colorida USB",
            imagem = "https://images.pexels.com/photos/7014415/pexels-photo-7014415.jpeg?auto=compress&cs=tinysrgb&w=600",
            preco = BigDecimal("900.00"),
            categoriaId = 2L
        ),
        Produto(
            id = 0,
            nome = "Monitor",
            descricao = "Monitor para PC Philips V Line 193V5LHSB2 - 18,5 LED Widescreen HD HDMI VGA",
            imagem = "https://images.pexels.com/photos/1029757/pexels-photo-1029757.jpeg?auto=compress&cs=tinysrgb&w=600",
            preco = BigDecimal("600.00"),
            categoriaId = 2L
        )
    )
    return listaProdutos
}