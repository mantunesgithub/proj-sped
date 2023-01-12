package br.com.mantunes.sped

import androidx.test.InstrumentationRegistry
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.ui.fragment.LoginFragment
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test


class LoginTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        Assert.assertEquals("br.com.mantunes.sped", appContext.packageName)
    }
    //Nome metodo: Deve fazerTalCoisa Quando houverEssasCondições
    @Test
    fun `deve retornar verdadeiro quando email for valido`() {
        // Arrange
        val login = LoginFragment()
        val email = "ma@gmail.com"
        val senha = "ma"
        //Act
        val temErroValidacao = login.isValidString(email)

        //Assert - afirme
        assertTrue(temErroValidacao)
    }
}