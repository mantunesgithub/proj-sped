package br.com.mantunes.sped.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.ActivityMainNavigationDrawerBinding
import br.com.mantunes.sped.extensions.vaiPara
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import com.google.android.material.navigation.NavigationView
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivityNavigationDrawer : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainNavigationDrawerBinding
    private val viewModelLogin: LoginViewModel by viewModel()
    var clienteIdLogado: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clienteIdLogado = viewModelLogin.buscaLoginDoCliente()
        Log.i("TAGy", "onCreate: {MainActivityNavigationDrawer} id= $clienteIdLogado")
        if (clienteIdLogado < 0) {
            Log.i("TAG", "onCreate: {MainActivityNavigationDrawer} id=null")
            vaiPara(LoginActivity::class.java) {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                finish()
            }
        }

        binding = ActivityMainNavigationDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment, R.id.mainActivity, R.id.cliente_form_cad_perfilpf_fragment,
                R.id.enderecoCadastradosFragment, R.id.menu_app_sair
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}