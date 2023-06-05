package com.leothan522.demoapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.leothan522.demoapp.apis.RetrofitHelper
import com.leothan522.demoapp.databinding.ActivityRecuperarBinding
import com.leothan522.demoapp.dialogs.Dialogs
import com.leothan522.demoapp.model.RespuestaSimple
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecuperarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecuperarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor(Color.parseColor("#ec2a0c"))

        binding.etEmail.requestFocus()
        main(binding.btnRecuperar, binding.etEmail)

    }

    private fun main(btnRecuperar: ImageView, etEmail: EditText) {
        btnRecuperar.setOnClickListener {
            hideKeyboard()
            verLoading(true)
            if (validarCampos(etEmail)){
                //recuperar
                recuperar(etEmail.text.toString())
            }else{
                Toast.makeText(this, "Los campos no cumplen con la validacion", Toast.LENGTH_SHORT).show()
                verLoading(false)
            }
        }
    }

    private fun recuperar(email: String) {
        val retrofitData = RetrofitHelper.getAndroid().recuperarCLave(email)
        retrofitData.enqueue(object : Callback<RespuestaSimple?> {
            override fun onResponse(
                call: Call<RespuestaSimple?>,
                response: Response<RespuestaSimple?>
            ) {
                val resultado = response.body()
                if (resultado?.success == true){
                    //se envio el correo
                    Dialogs().emailSend(
                        binding.btnRecuperar.context,
                        layoutInflater.inflate(R.layout.dialog_email_send, null),
                        this@RecuperarActivity
                    )
                }else{
                    binding.etEmail.error = "Email no encontrado"
                    Dialogs().showDialog(
                        binding.btnRecuperar.context,
                        layoutInflater.inflate(R.layout.dialog_mensaje_error, null),
                        resultado?.message.toString(),
                        "Email no encontrado"
                    )
                    verLoading(false)
                }
            }

            override fun onFailure(call: Call<RespuestaSimple?>, t: Throwable) {
                Log.d("RecuperarActivity", t.toString())
                verLoading(false)
                Dialogs().noInternet(
                    binding.btnRecuperar.context,
                    layoutInflater.inflate(R.layout.dialog_no_internet, null)
                )
            }
        })
    }

    private fun validarCampos(etEmail: EditText): Boolean {
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
        binding.cvContainer.visibility = visibility
        binding.cvRecuperar.visibility = visibility
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