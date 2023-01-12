package br.com.mantunes.sped.repository

import br.com.mantunes.sped.database.dao.ClienteDAO
import br.com.mantunes.sped.model.Cliente
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ClienteRepositoryTest  {
    @Test
    fun `deve chamar o dao quando salva um cliente`() = runTest {
        //Arrange - Config
        val dao = mockk<ClienteDAO>()
        val clienteRepository = ClienteRepository(dao)
        val cliente = Cliente(
            id = 0L, nome = "Marcio Teste", "PF", cnpjCpf =  "123.456.789-12",
            email = "mateste@gmail.com", senha = "mateste", dataNascimento = "16/12/1967",
            naturalidade = "Osasco", genero = "M", "","","s","2758123",
            "912345678", "", "")

        coEvery {
            dao.salva(cliente)
        }.returns(Unit)                     // ou .returns(mockk())
        //Act
        clienteRepository.salva(cliente)
        //Assert
        coVerify {
            dao.salva(cliente)
        }
    }
}