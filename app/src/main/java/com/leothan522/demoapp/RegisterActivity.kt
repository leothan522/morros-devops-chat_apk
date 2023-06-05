package com.leothan522.demoapp

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.leothan522.demoapp.apis.RetrofitHelper
import com.leothan522.demoapp.databinding.ActivityRegisterBinding
import com.leothan522.demoapp.dialogs.Dialogs
import com.leothan522.demoapp.model.Usuario
import com.leothan522.demoapp.prefs.SharedApp.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor(Color.parseColor("#ec2a0c"))

        binding.etName.requestFocus()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Error al obtener el token de registro de FCM", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = "FCM-TOKEN ACTUAL: $token"
            Log.d(ContentValues.TAG, msg)
            main(
                binding.btnRegister,
                binding.etName,
                binding.etEmail,
                binding.etTelefono,
                binding.etPassword,
                token
            )
        })
        /*main(
            binding.btnRegister,
            binding.etName,
            binding.etEmail,
            binding.etTelefono,
            binding.etPassword
        )*/

    }

    private fun main(
        btnRegister: ImageView,
        etName: EditText,
        etEmail: EditText,
        etTelefono: EditText,
        etPassword: EditText,
        token: String
    ) {
        btnRegister.setOnClickListener {
            hideKeyboard()
            verLoading(true)
            if (validarCampos(etName, etEmail, etTelefono, etPassword)){
                //registrar Usuario
                registrarUsuario(
                    etName.text.toString(),
                    etEmail.text.toString(),
                    etTelefono.text.toString(),
                    etPassword.text.toString(),
                    token)
            }else{
                //campos no validados
                Toast.makeText(this, "Los campos no cumplen con la validacion", Toast.LENGTH_LONG).show()
                verLoading(false)
            }
        }
    }

    private fun registrarUsuario(
        name: String,
        email: String,
        telefono: String,
        password: String,
        fcm_token: String
    ) {

        val retrofitData = RetrofitHelper.getAndroid().registrarUsuario(
            name, email, telefono, password, fcm_token
        )
        retrofitData.enqueue(object : Callback<Usuario?> {
            override fun onResponse(call: Call<Usuario?>, response: Response<Usuario?>) {
                val resultado = response.body()
                if (resultado?.success == true){
                    prefs.saveLogin(true)
                    prefs.saveID(resultado.id.toInt())
                    prefs.saveName(resultado.name)
                    prefs.saveEmail(resultado.email)
                    prefs.saveTelefono(resultado?.telefono.toString())
                    val intent = Intent(binding.btnRegister.context, ChatActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    binding.etEmail.error = getText(R.string.error_email_registrado)
                    Dialogs().showDialog(
                        binding.btnRegister.context,
                        layoutInflater.inflate(R.layout.dialog_mensaje_error, null),
                        resultado?.message.toString(),
                        "Email no disponible"
                    )
                    verLoading(false)
                }
            }

            override fun onFailure(call: Call<Usuario?>, t: Throwable) {
                Log.d("RegisterActivity", t.toString())
                verLoading(false)
                Dialogs().noInternet(
                    binding.btnRegister.context,
                    layoutInflater.inflate(R.layout.dialog_no_internet, null)
                )
            }

        })
        //Toast.makeText(this, "registro", Toast.LENGTH_LONG).show()
    }

    private fun validarCampos(
        etName: EditText,
        etEmail: EditText,
        etTelefono: EditText,
        etPassword: EditText
    ): Boolean {
        var resultado = true

        if (etName.text.isNullOrEmpty()){
            etName.error = getText(R.string.ingrese_nombre)
            resultado = false
        }else{
            if (etName.text.length < 4){
                etName.error = getText(R.string.min_4_caracteres)
                resultado = false
            }
        }

        if (etEmail.text.isNullOrEmpty()){
            etEmail.error = getText(R.string.ingrese_email)
            resultado = false
        }else{
            val patterns = Patterns.EMAIL_ADDRESS
            if (!patterns.matcher(etEmail.text.toString()).matches()){
                etEmail.error = getText(R.string.error_email)
                resultado = false
            }
        }

        if (etTelefono.text.isNullOrEmpty()){
            etTelefono.error = getText(R.string.ingrese_telefono)
            resultado = false
        }else{
            val patterns = Patterns.PHONE
            if (!patterns.matcher(etTelefono.text.toString()).matches()){
                etTelefono.error = getText(R.string.error_telefono)
                resultado = false
            }
        }

        if (etPassword.text.isNullOrEmpty()){
            etPassword.error = getText(R.string.ingrese_password)
            resultado = false
        }else{
            if (etPassword.text.length < 8){
                etPassword.error = getText(R.string.min_8_caracteres)
                resultado = false
            }
        }

        return resultado
    }

    private fun verLoading(loading: Boolean) {
        binding.layoutLoading.loading.isVisible = loading
        val visibility = if (loading){
            View.INVISIBLE
        }else{
            View.VISIBLE
        }
        binding.cvGoLogin.visibility = visibility
        binding.cvRegister.visibility = visibility
        binding.cvContainer.visibility = visibility
    }

    fun goLogin(View: View?){
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        //super.onBackPressed()
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay)
        finish()
    }

    //ocultar teclado
    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    //Cambiar color barra de estado
    fun Activity.setStatusBarColor(@ColorInt color: Int) {
        val window = getWindow()
        val decorView: View = window.getDecorView()
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = ColorUtils.calculateLuminance(color) > 0.5
        window.statusBarColor = color
    }

}