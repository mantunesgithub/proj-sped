package br.com.mantunes.sped.di

import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.Categoria
import br.com.mantunes.sped.model.ProductCarousel
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
        ),
        Categoria(
            id = 0,
            nome = "Escritorio",
            descricao = "Materias de escritorio em geral",
            imagem = "https://images.pexels.com/photos/1957477/pexels-photo-1957477.jpeg?auto=compress&cs=tinysrgb&w=1600"
        ),
        Categoria(
            id = 0,
            nome = "Jardinagem",
            descricao = "Materias de jardim em geral",
            imagem = "https://images.pexels.com/photos/2013782/pexels-photo-2013782.jpeg?auto=compress&cs=tinysrgb&w=1600"
        ),
        Categoria(
            id = 0,
            nome = "Decoração",
            descricao = "Materias de decoração",
            imagem = "https://images.pexels.com/photos/13871100/pexels-photo-13871100.jpeg?auto=compress&cs=tinysrgb&w=1600"
        ),
        Categoria(
            id = 0,
            nome = "Moda Jovem",
            descricao = "Moda jovem feminina",
            imagem = "https://images.pexels.com/photos/15787315/pexels-photo-15787315.jpeg?auto=compress&cs=tinysrgb&w=1600"
        ),
        Categoria(
            id = 0,
            nome = "Moda Jovem",
            descricao = "Moda jovem masculina",
            imagem = "https://images.pexels.com/photos/15792253/pexels-photo-15792253.jpeg?auto=compress&cs=tinysrgb&w=1600"
        ),
        Categoria(
            id = 0,
            nome = "Utensílios domésticos",
            descricao = "Utilidades domésticas em geral",
            imagem = "https://images.pexels.com/photos/210897/pexels-photo-210897.jpeg?auto=compress&cs=tinysrgb&w=1600"
        ),
        Categoria(
            id = 0,
            nome = "Eletro domésticos",
            descricao = "Eletro domésticos para a cozinha e area de serviço",
            imagem = "https://media.istockphoto.com/id/1196974664/pt/foto/set-of-home-kitchen-appliances-in-the-room-on-the-wall-background.jpg?s=612x612&w=is&k=20&c=5B2w88vcWZhTp6ZQE7QKtVAUnKYeDVpZsJN1-ltgo0M="
        )
        )
    return listaCategorias
}
fun criaProdutos(): List<Produto> {
    val listaProdutos: List<Produto> = listOf(
        Produto(
            id = 0,
            nome = "Bola de Futebol da Alemanha Nº.5",
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
        ),
        Produto(
            id = 0,
            nome = "Mesa de escritório",
            descricao = "Mesa de madeira para trabalho",
            imagem = "https://images.pexels.com/photos/8960464/pexels-photo-8960464.jpeg?auto=compress&cs=tinysrgb&w=1600",
            preco = BigDecimal("800.00"),
            categoriaId = 3L
        ),
        Produto(
            id = 0,
            nome = "Cadeira de escritório",
            descricao = "Cadeira de visita para escritorio",
            imagem = "https://images.pexels.com/photos/8111799/pexels-photo-8111799.jpeg?auto=compress&cs=tinysrgb&w=1600",
            preco = BigDecimal("300.00"),
            categoriaId = 3L
        ),
        Produto(
            id = 0,
            nome = "Balde",
            descricao = "Balde para regar plantas",
            imagem = "https://images.pexels.com/photos/1424672/pexels-photo-1424672.jpeg?auto=compress&cs=tinysrgb&w=1600",
            preco = BigDecimal("60.00"),
            categoriaId = 4L
        ),
        Produto(
            id = 0,
            nome = "Abajour",
            descricao = "Abajur luminoso para decoração de qualquer ambiente",
            imagem = "https://images.pexels.com/photos/1112598/pexels-photo-1112598.jpeg?auto=compress&cs=tinysrgb&w=1600",
            preco = BigDecimal("500.00"),
            categoriaId = 5L
        ),
        Produto(
            id = 0,
            nome = "Pendente",
            descricao = "Pendentes luminosos para uso geral ",
            imagem = "https://images.pexels.com/photos/5094559/pexels-photo-5094559.jpeg?auto=compress&cs=tinysrgb&w=1600",
            preco = BigDecimal("300.00"),
            categoriaId = 5L
        )
    )
    return listaProdutos
}
fun criaProductCarousel(): List<ProductCarousel> {
    val listCarousel: List<ProductCarousel> = listOf(
        ProductCarousel(productImage = R.drawable.imagem_camisa, productName =   "Moda Jovem"),
        ProductCarousel(productImage = R.drawable.imagem_monitor, productName =   "Eletrônicos"),
        ProductCarousel(productImage = R.drawable.imagem_cama_mesa, productName = "Cama e Mesa "),
        ProductCarousel(productImage = R.drawable.imagem_raquete, productName =   "Esportes"),
        ProductCarousel(productImage = R.drawable.imagem_office, productName =    "Escritório"),
        ProductCarousel(productImage = R.drawable.imagem_utillar, productName =   "Utilidades Lar")
    )
    return listCarousel
}