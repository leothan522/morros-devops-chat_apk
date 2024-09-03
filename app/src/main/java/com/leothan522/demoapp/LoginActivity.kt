package com.leothan522.demoapp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
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
import androidx.core.view.isVisible
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.leothan522.demoapp.apis.RetrofitHelper
import com.leothan522.demoapp.databinding.ActivityLoginBinding
import com.leothan522.demoapp.dialogs.Dialogs
import com.leothan522.demoapp.model.Usuario
import com.leothan522.demoapp.prefs.SharedApp.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etEmail.requestFocus()

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
                binding.btnLogin,
                binding.etEmail,
                binding.etPassword,
                token
            )
        })

        /*main(
            binding.btnLogin,
            binding.etEmail,
            binding.etPassword
        )*/

    }

    private fun main(btnLogin: ImageView, etEmail: EditText, etPassword: EditText, token: String) {
        btnLogin.setOnClickListener {
            hideKeyboard()
            verLoading(true)
            if (validarCampos(etEmail, etPassword)){
                //login
                login(etEmail.text.toString(), etPassword.text.toString(), token)
            }else{
                //campos no validados
                Toast.makeText(this, "Los campos no cumplen con la validacion", Toast.LENGTH_SHORT).show()
                verLoading(false)
            }
        }
    }

    private fun login(email: String, password: String, fcm_token: String) {
        val retrofitData = RetrofitHelper.getAndroid().login(
            email, password, fcm_token
        )
        retrofitData.enqueue(object : Callback<Usuario?> {
            override fun onResponse(call: Call<Usuario?>, response: Response<Usuario?>) {
                val resultado = response.body()
                if (resultado?.success == true){
                    prefs.saveLogin(true)
                    prefs.saveID(resultado.id.toString())
                    prefs.saveName(resultado.name.toString())
                    prefs.saveEmail(resultado.email.toString())
                    prefs.saveTelefono(resultado?.telefono.toString())
                    startActivity(Intent(binding.btnLogin.context, ChatActivity::class.java))
                    finish()
                }else{
                    Dialogs().showDialog(
                        binding.btnLogin.context,
                        layoutInflater.inflate(R.layout.dialog_mensaje_error, null),
                        resultado?.message.toString(),
                        resultado?.error.toString()
                    )
                    verLoading(false)
                }
            }

            override fun onFailure(call: Call<Usuario?>, t: Throwable) {
                Log.d("LoginActivity", t.toString())
                verLoading(false)
                Dialogs().noInternet(
                    binding.btnLogin.context,
                    layoutInflater.inflate(R.layout.dialog_no_internet, null)
                )
            }
        })
    }

    private fun validarCampos(etEmail: EditText, etPassword: EditText): Boolean {

        var resultado = true

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
        binding.cvLogin.visibility = visibility
        binding.cvContainer.visibility = visibility
        binding.cvGoRegister.visibility = visibility
        binding.btnGoRecuperar.visibility = visibility

    }

    fun goRegister(View: View?){
        startActivity(Intent(this, RegisterActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left, R.anim.stay)
        finish()
    }

    fun goRecuperar(View: View?){
        startActivity(Intent(this, RecuperarActivity::class.java))
        //overridePendingTransition(R.anim.slide_in_left, R.anim.stay)
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
}