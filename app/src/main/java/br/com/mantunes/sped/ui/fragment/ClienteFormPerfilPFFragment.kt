package br.com.mantunes.sped.ui.fragment

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files.createFile
import java.text.SimpleDateFormat
import java.util.*


class ClienteFormPerfilPFFragment : Fragment() {
    private var selectedPhotoPath: String = ""
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

    companion object {
        private val PERMISSAO_GALERIA = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }

    private lateinit var dialog: AlertDialog

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
        buscaClienteLogado()
        abrirDialog()
        configuraBotaoAlterar()
        visualizaEnderecos()
        //activity?.title = TITULO_APPBAR_DADOS_PF
    }

    private fun abrirDialog() {
        _binding.clienteFormCadastroPerfilPfBotaoFoto.setOnClickListener {
            val dialog = ClienteFormDialogCameraFragment()
            dialog.show(childFragmentManager, dialog.tag)
            dialog.quandoEscolheFoto = {
                when (it) {
                    "foto" -> { abrirCamera() }
                    else -> { abrirGaleria() }
                }
            }
        }
    }

    private fun visualizaEnderecos() {
        _binding.clienteFormCadastroPerfilPfBtnEnderecos.setOnClickListener {
            val direcao =
                ClienteFormPerfilPFFragmentDirections.actionClienteFormCadPerfilpfFragmentToEnderecoCadastradosFragment()
            controlador.navigate(direcao)
        }
    }

    private fun buscaClienteLogado() {

        clienteIdLogado = viewModelLogin.buscaLoginDoCliente()
        viewModel.buscaPorId(clienteIdLogado)
            .observe(viewLifecycleOwner, Observer { clienteObserver ->
                clienteObserver?.let { cliente ->
                    clienteLogado = cliente
                    preencheCampos()
                }
            })
    }

    private fun preencheCampos() {
        clienteLogado?.let { clienteLogado ->
            _binding.clienteFormCadastroPerfilPfNome.setText(clienteLogado.nome)
            _binding.clienteFormCadastroPerfilPfCpf.setText(clienteLogado.cnpjCpf)
            _binding.clienteFormCadastroPerfilPfEmail.setText(clienteLogado.email)
            _binding.clienteFormCadastroPerfilPfDtNascimento.setText(clienteLogado.dataNascimento)
            _binding.clienteFormCadastroPerfilPfNaturalidade.setText(clienteLogado.naturalidade)
            _binding.clienteFormCadastroPerfilPfGenero.setText(clienteLogado.genero)
            _binding.clienteFormCadastroPerfilPfFoneCel.setText(clienteLogado.telefoneCel)
            _binding.clienteFormCadastroPerfilPfFoneRes.setText(clienteLogado.telefoneRes)
            _binding.clienteFormCadastroPerfilPfFoneOutr.setText(clienteLogado.telefoneOutro)
            Log.i("TAG", "preencheCampos: ${clienteLogado.caminhoFoto}")
            carregaImagem(clienteLogado.caminhoFoto)
        }
    }

    private fun configuraBotaoAlterar() {
        _binding.clienteFormCadastroPerfilPfBtnAlterar.setOnClickListener {
            if (alteraDadosCliente()) {
                mostraMsg("Perfil atualizado com sucesso!")
            }
        }
    }

    private fun alteraDadosCliente(): Boolean {
        var idLogado: Long = 0L
        clienteLogado?.let {

            if (!ValidaFormPerfilPFFragment()) {

                idLogado = it.id
                val nome = _binding.clienteFormCadastroPerfilPfNome.text.toString()
                var senha: String = it.senha
                if (!_binding.clienteFormCadastroPerfilPfSenhaNova.text.isNullOrBlank()) {
                    mostraMsg("Senha alterada")
                    senha = _binding.clienteFormCadastroPerfilPfSenhaNova.text.toString()
                }
                val tipo: String = getString(R.string.pessoa_fisica)
                val cpf = _binding.clienteFormCadastroPerfilPfCpf.text.toString()
                val email = _binding.clienteFormCadastroPerfilPfEmail.text.toString()
                val dataNascimento =
                    _binding.clienteFormCadastroPerfilPfDtNascimento.text.toString()
                val naturalidade = _binding.clienteFormCadastroPerfilPfNaturalidade.text.toString()
                val foneCelular = _binding.clienteFormCadastroPerfilPfFoneCel.text.toString()
                val foneResidencial = _binding.clienteFormCadastroPerfilPfFoneRes.text.toString()
                val foneOutro = _binding.clienteFormCadastroPerfilPfFoneOutr.text.toString()
                val genero = _binding.clienteFormCadastroPerfilPfGenero.text.toString()
                if (_binding.clienteFormCadastroPerfilPfImagem.tag != null) {
                    caminhoFoto = _binding.clienteFormCadastroPerfilPfImagem.tag as String
                    Log.i("TAG", "alteraDadosCliente: caminho = $caminhoFoto")
                }
                atualiza(
                    Cliente(
                        idLogado, nome, tipo, cpf, email, senha, dataNascimento, naturalidade,
                        genero, "", "", "", foneResidencial, foneCelular,
                        foneOutro, caminhoFoto
                    )
                )
                return true
            } else {
                return false
            }
        }
        return false
    }

    private fun ValidaFormPerfilPFFragment(): Boolean {
        var error = false
        if (_binding.clienteFormCadastroPerfilPfNome.text.isNullOrBlank()) {
            _binding.clienteFormCadastroPerfilPfNome.error = "Digite seu nome"
            error = true
        }
        if (_binding.clienteFormCadastroPerfilPfCpf.text.isNullOrBlank()) {
            _binding.clienteFormCadastroPerfilPfCpf.error = "Digite seu cpf"
            error = true
        }
        if (_binding.clienteFormCadastroPerfilPfEmail.text.isNullOrBlank()) {
            _binding.clienteFormCadastroPerfilPfEmail.error = "Digite seu email"
            error = true
        }
        if (_binding.clienteFormCadastroPerfilPfDtNascimento.text.isNullOrBlank()) {
            _binding.clienteFormCadastroPerfilPfDtNascimento.error = "Digite sua Data Nascimento"
            error = true
        }
        if (_binding.clienteFormCadastroPerfilPfNaturalidade.text.isNullOrBlank()) {
            _binding.clienteFormCadastroPerfilPfNaturalidade.error = "Digite sua Naturalidade"
            error = true
        }
        if (_binding.clienteFormCadastroPerfilPfFoneCel.text.isNullOrBlank()) {
            _binding.clienteFormCadastroPerfilPfFoneCel.error = "Digite seu telefone Celular"
            error = true
        }
        if (_binding.clienteFormCadastroPerfilPfGenero.text.isNullOrBlank()) {
            _binding.clienteFormCadastroPerfilPfGenero.error = "Digite Genero M/F"
        }
        if (_binding.clienteFormCadastroPerfilPfGenero.text.toString() != "M" &&
            _binding.clienteFormCadastroPerfilPfGenero.text.toString() != "F"
        ) {
            _binding.clienteFormCadastroPerfilPfGenero.error = "Digite Genero M ou F"
            error = true
        }
        return error
    }


    private fun atualiza(cliente: Cliente) {
        viewModel.atualiza(cliente).observe(viewLifecycleOwner, Observer {
            it?.dado?.let {
            }
        })
    }

    private fun abrirCamera() {
        val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.i("TAG", "abrirCamera: verso >= S")
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            val resolver = getActivity()?.getContentResolver()
            uriImagem =
                resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            intentCamera.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
        } else {
            Log.i("TAG", "Foto versao < s = ${Build.VERSION.SDK_INT} ")
            val autorizacao = getString(R.string.autorizacao)
            val diretorio =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            this.caminhoFoto = diretorio.path + "/Sped" + System.currentTimeMillis() + ".jpg"
            val file = File(this.caminhoFoto)
            uriImagem = FileProvider.getUriForFile(contextVaiPara, autorizacao, file)
        }
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uriImagem)
        startActivityForResult(intentCamera, CODIGO_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODIGO_CAMERA && uriImagem != null) {      //CAMERA
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
            _binding.clienteFormCadastroPerfilPfImagem.scaleType = ImageView.ScaleType.CENTER_CROP
            _binding.clienteFormCadastroPerfilPfImagem.tag = caminhoFoto
        }

    }

    private fun abrirGaleria() {
        verificaPermissaoGaleria()
    }

    private fun verificaPermissaoGaleria() {
        val permissaoGaleriaAceita = verificaPermissao(PERMISSAO_GALERIA)
        when {
            permissaoGaleriaAceita -> {
                resultGaleria.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            shouldShowRequestPermissionRationale(PERMISSAO_GALERIA) -> showDialogPermissao()

            else -> requestGaleria.launch(PERMISSAO_GALERIA)
        }
    }

    private fun showDialogPermissao() {
        val dialog = ClienteFormDialogGaleriaFragment()
        dialog.show(childFragmentManager, dialog.tag)
    }

    private fun verificaPermissao(permissao: String): Boolean {
        return ContextCompat.checkSelfPermission(contextVaiPara, permissao) ==
                PackageManager.PERMISSION_GRANTED
    }

    private val requestGaleria =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissao ->
            if (permissao) {
                resultGaleria.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showDialogPermissao()
            }
        }

    private val resultGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.data?.data != null) {
                val bitmap: Bitmap =
                    if (Build.VERSION.SDK_INT < 28) {
                        Log.i("TAG", "Galeria versao < 28 = ${Build.VERSION.SDK_INT} ")
                        MediaStore.Images.Media.getBitmap(
                            contextVaiPara.contentResolver,
                            result.data?.data
                        )
                    } else {
                        val source = ImageDecoder.createSource(
                            contextVaiPara.contentResolver,
                            result.data?.data!!
                        )
                        Log.i("TAG", "Galeria versao >= 28 = ${Build.VERSION.SDK_INT} ")
                        ImageDecoder.decodeBitmap(source)
                    }
                val photoFile: File? = try {
                    createFile(
                        contextVaiPara,
                        Environment.DIRECTORY_PICTURES,
                        "jpg"
                    )
                } catch (ex: IOException) {
                    Toast.makeText(
                        contextVaiPara, getString(R.string.create_file_error, ex.message),
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }
                photoFile?.also {
                    selectedPhotoPath = it.absolutePath
                    val resolver = contextVaiPara.applicationContext.contentResolver
                    resolver.openInputStream(result.data!!.data!!).use { stream ->
                        val output = FileOutputStream(photoFile)
                        stream!!.copyTo(output)
                    }
                    val uri = Uri.fromFile(photoFile)
                    _binding.clienteFormCadastroPerfilPfImagem.setImageURI(uri)
                    _binding.clienteFormCadastroPerfilPfImagem.tag = selectedPhotoPath
                }
                _binding.clienteFormCadastroPerfilPfImagem.setImageBitmap(bitmap)
                Log.i("TAG", "Galeria tag = ${_binding.clienteFormCadastroPerfilPfImagem.tag}")
                Log.i("TAG", "Galeria Camingo abs = $selectedPhotoPath")

            }
        }

    fun createFile(context: Context, folder: String, ext: String): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filesDir: File? = context.getExternalFilesDir(folder)
        val newFile = File(filesDir, "$timeStamp.$ext")
        newFile.createNewFile()
        return newFile
    }
}