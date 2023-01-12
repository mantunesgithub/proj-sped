package br.com.mantunes.sped

import br.com.mantunes.sped.model.Cliente
import org.junit.Assert
import org.junit.Test

class ClienteTest {
    @Test
    fun `deve retornar verdadeiro quando email for valido`() {
        // Arrange
        val cliente = Cliente(
            id = 0L, nome = "Marcio Teste", "PF", cnpjCpf =  "123.456.789-12",
            email = "mateste@gmail.com", senha = "ma", dataNascimento = "16/12/1967",
            naturalidade = "Osasco", genero = "M", "","","s","2758123",
            "912345678", "", "")
        //Act
        val ehValido = cliente.ehValido()
        //Assert - afirme
        Assert.assertTrue(ehValido)
    }
}