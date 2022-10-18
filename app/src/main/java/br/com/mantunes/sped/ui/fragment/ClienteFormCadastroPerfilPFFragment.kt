package br.com.mantunes.sped.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.ClienteFormCadastroPerfilPfBinding
import br.com.mantunes.sped.model.Cliente
import br.com.mantunes.sped.ui.fragment.extensions.mostraMsg
import br.com.mantunes.sped.ui.viewmodel.ClienteViewModel
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class ClienteFormCadastroPerfilPFFragment : Fragment() {

    private val controlador by lazy { findNavController() }
    private lateinit var _binding: ClienteFormCadastroPerfilPfBinding
    var uriImagem: Uri? = null
    private val viewModelLogin: LoginViewModel by viewModel()
    var clienteIdLogado: Long = 0
    var clienteLogado: Cliente? = null
    private var caminhoFoto: String? = null
    private val clienteId: Long = 0
    private val viewModel: ClienteViewModel by viewModel { parametersOf(clienteId) }
    private val contextVaiPara by lazy {
        requireContext().applicationContext
            ?: throw IllegalArgumentException(PROBLEMA_EXTRAIR_CONTEXTO)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ClienteFormCadastroPerfilPfBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("TAG", "onViewCreated:-{ClienteFormCadastroPerfilPFFragment} chegou")
        buscaClienteLogado()
        abrirCamera()
        configuraBotaoAlterar()
        visualizaEnderecos()
        activity?.title = TITULO_APPBAR_DADOS_PF
    }

    private fun visualizaEnderecos() {
        _binding.clienteFormCadastroPerfilPfBtnEnderecos.setOnClickListener {
            val direcao =
                ClienteFormCadastroPerfilPFFragmentDirections.actionClienteFormCadPerfilpfFragmentToEnderecoCadastradosFragment()
            controlador.navigate(direcao)
        }
    }

    private fun buscaClienteLogado() {

        clienteIdLogado = viewModelLogin.buscaLoginDoCliente()
        Log.i(
            "TAG",
            "buscaClienteLogado: {ClienteFormCadastroPerfilPFFragment} clid= $clienteIdLogado"
        )
        viewModel.buscaPorId(clienteIdLogado)
            .observe(viewLifecycleOwner, Observer { clienteObserver ->
                clienteObserver?.let { cliente ->
                    clienteLogado = cliente
                    preencheCampos()
                    Log.i("extende PF", "buscaClienteLogado: $clienteLogado")
                }
            })
    }

    private fun preencheCampos() {
        Log.i("TAG", "preencheCampos: {ClienteFormCadastroPerfilPFFragment} chegou $clienteLogado")
        clienteLogado?.let { clienteLogado ->
            Log.i(
                "TAG",
                "preencheCampos: {ClienteFormCadastroPerfilPFFragment} cliente: $clienteLogado"
            )
            _binding.clienteFormCadastroPerfilPfNome.setText(clienteLogado.nome)
            _binding.clienteFormCadastroPerfilPfCpf.setText(clienteLogado.cnpjCpf)
            _binding.clienteFormCadastroPerfilPfEmail.setText(clienteLogado.email)
            _binding.clienteFormCadastroPerfilPfSenha.setText(clienteLogado.senha)
            _binding.clienteFormCadastroPerfilPfDtNascimento.setText(clienteLogado.dataNascimento)
            _binding.clienteFormCadastroPerfilPfNaturalidade.setText(clienteLogado.naturalidade)
            _binding.clienteFormCadastroPerfilPfGenero.setText(clienteLogado.genero)
            _binding.clienteFormCadastroPerfilPfFoneCom.setText(clienteLogado.telefoneCom)
            _binding.clienteFormCadastroPerfilPfFoneRes.setText(clienteLogado.telefoneRes)
            _binding.clienteFormCadastroPerfilPfFoneOutr.setText(clienteLogado.telefoneOutro)
            carregaImagem(clienteLogado.caminhoFoto)
        }
    }

    private fun configuraBotaoAlterar() {
        _binding.clienteFormCadastroPerfilPfBtnAlterar.setOnClickListener {
            alteraDadosCliente()
            mostraMsg("Perfil atualizado com sucesso!")
        }
    }

    private fun alteraDadosCliente() {
        var idLogado: Long = 0L
        clienteLogado?.let {
            idLogado = it.id
            Log.i("TAG", "alteraDadosCliente: id logado = $idLogado")

            val nome = _binding.clienteFormCadastroPerfilPfNome.text.toString()
            val senha = _binding.clienteFormCadastroPerfilPfSenhaNova.text.toString()
            val tipo: String = "PF"
            val cpf = _binding.clienteFormCadastroPerfilPfCpf.text.toString()
            val email = _binding.clienteFormCadastroPerfilPfEmail.text.toString()
            val dataNascimento = _binding.clienteFormCadastroPerfilPfDtNascimento.text.toString()
            val naturalidade = _binding.clienteFormCadastroPerfilPfNaturalidade.text.toString()
            val foneResidencial = _binding.clienteFormCadastroPerfilPfFoneRes.text.toString()
            val foneComercial = _binding.clienteFormCadastroPerfilPfFoneCom.text.toString()
            val foneOutro = _binding.clienteFormCadastroPerfilPfFoneOutr.text.toString()
            val genero = _binding.clienteFormCadastroPerfilPfGenero.text.toString()
            if (_binding.clienteFormCadastroPerfilPfImagem.getTag() != null) {
                val caminhoFoto = _binding.clienteFormCadastroPerfilPfImagem.getTag() as String
            }
            atualiza(
                Cliente(
                    idLogado, nome, tipo, cpf, email, senha, dataNascimento, naturalidade,
                    genero, "", "", "", foneResidencial, foneComercial,
                    foneOutro, caminhoFoto
                )
            )
        }
    }

    private fun atualiza(cliente: Cliente) {
        Log.i("TAG", "atualiza cliente = : $cliente")
        viewModel.atualiza(cliente).observe(viewLifecycleOwner, Observer {
            it?.dado?.let {
            }
        })
    }

    private fun abrirCamera() {
        _binding.clienteFormCadastroPerfilPfBotaoCamera.setOnClickListener {
            Log.i("TAG", "abrirCamera: clicou")
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intentCamera.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            val autorizacao = getString(R.string.autorizacao)
            val diretorio =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            this.caminhoFoto = diretorio.path + "/Projma" + System.currentTimeMillis() + ".jpg"
            val file = File(this.caminhoFoto)
            uriImagem = FileProvider.getUriForFile(contextVaiPara, autorizacao, file)
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uriImagem)
            startActivityForResult(intentCamera, CODIGO_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA && uriImagem != null) {      //CAMERA
                Log.i("TAG", "onActivityResult: carregou imagem $caminhoFoto")
                carregaImagem(caminhoFoto)
                _binding.clienteFormCadastroPerfilPfImagem.setImageURI(uriImagem)
            }
        }
    }

    private fun carregaImagem(caminhoFoto: String?) {
        if (caminhoFoto != null) {
            val bitmap = BitmapFactory.decodeFile(caminhoFoto)
            val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
            _binding.clienteFormCadastroPerfilPfImagem.setImageBitmap(bitmapReduzido)
            _binding.clienteFormCadastroPerfilPfImagem.setScaleType(ImageView.ScaleType.FIT_XY)
            _binding.clienteFormCadastroPerfilPfImagem.setTag(caminhoFoto)
        }
    }
}
