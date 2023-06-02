package com.leothan522.demoapp.dialogs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.leothan522.demoapp.LoginActivity
import com.leothan522.demoapp.RecuperarActivity
import com.leothan522.demoapp.databinding.DialogEmailSendBinding
import com.leothan522.demoapp.databinding.DialogMensajeErrorBinding
import com.leothan522.demoapp.databinding.DialogNoInternetBinding

class Dialogs {
    fun noInternet(context: Context, view: View){
        //asignando valores del Alert Dialog
        val builder = AlertDialog.Builder(context)
        //pasando la vista al builder
        builder.setView(view)
        //creando el alert Dialog
        val dialog = builder.create()
        dialog.show()
        //programamos accion del boton
        val dialogBinding = DialogNoInternetBinding.bind(view)
        dialogBinding.btnNoIntermet.setOnClickListener {
            dialog.dismiss()
            //Toast.makeText(dialogBinding.btnNoIntermet.context, "funciona boton", Toast.LENGTH_SHORT).show()
        }
    }

    fun noInternetMain(context: Context, view: View, activity: Activity){
        //asignando valores del Alert Dialog
        val builder = AlertDialog.Builder(context)
        //pasando la vista al builder
        builder.setView(view)
        //creando el alert Dialog
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        //programamos accion del boton
        val dialogBinding = DialogNoInternetBinding.bind(view)
        dialogBinding.btnNoIntermet.setOnClickListener {
            dialog.dismiss()
            activity.onBackPressed()
            //Toast.makeText(dialogBinding.btnNoIntermet.context, "funciona boton", Toast.LENGTH_SHORT).show()
        }
    }

    fun emailSend(context: Context, view: View, recuperarActivity: RecuperarActivity){
        //asignando valores del Alert Dialog
        val builder = AlertDialog.Builder(context)
        //pasando la vista al builder
        builder.setView(view)
        //creando el alert Dialog
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        //programamos accion del boton
        val dialogBinding = DialogEmailSendBinding.bind(view)
        dialogBinding.btnCerrarDialogEmail.setOnClickListener {
            //dialog.hide()
            context.startActivity(Intent(context, LoginActivity::class.java))
            recuperarActivity.finish()
        }
    }

    fun showDialog(context: Context, view: View, mensaje: String, titulo: String?){
        //asignando valores del Alert Dialog
        val builder = AlertDialog.Builder(context)
        //pasando la vista al builder
        builder.setView(view)
        //creando el alert Dialog
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.show()
        //programamos accion del boton
        val dialogBinding = DialogMensajeErrorBinding.bind(view)
        dialogBinding.tvMensajeError.text = mensaje
        dialogBinding.tvTituloError.text = titulo
        dialogBinding.btnCerrarDialog.setOnClickListener {
            dialog.dismiss()
        }
    }
}