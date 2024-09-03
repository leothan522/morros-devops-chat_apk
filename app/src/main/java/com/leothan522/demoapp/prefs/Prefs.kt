package com.leothan522.demoapp.prefs

import android.content.Context

class Prefs( val context: Context) {

    val SHARED_DB = "MorrosDevopsLeothan522"
    val SHARED_LOGIN = "morrosdevops"
    val SHARED_ID = "id"
    val SHARED_EMAIL = "email"
    val SHARED_NAME = "name"
    val SHARED_TELEFONO = "telefono"

    val storage = context.getSharedPreferences(SHARED_DB, 0)

    fun saveLogin(login:Boolean){
        storage.edit().putBoolean(SHARED_LOGIN, login).apply()
    }

    fun getLogin():Boolean{
        return storage.getBoolean(SHARED_LOGIN, false)
    }

    fun saveID(id:String){
        storage.edit().putString(SHARED_ID, id).apply()
    }

    fun getID():String{
        return storage.getString(SHARED_ID, "")!!
    }

    fun saveEmail(email: String){
        storage.edit().putString(SHARED_EMAIL, email).apply()
    }

    fun getEmail(): String {
        return storage.getString(SHARED_EMAIL, "")!!
    }

    fun saveName(name: String){
        storage.edit().putString(SHARED_NAME, name).apply()
    }

    fun getName(): String {
        return storage.getString(SHARED_NAME, "")!!
    }

    fun saveTelefono(telefono: String){
        storage.edit().putString(SHARED_TELEFONO, telefono).apply()
    }

    fun getTelefono(): String {
        return storage.getString(SHARED_TELEFONO, "")!!
    }

    fun wipe(){
        storage.edit().clear().apply()
    }

}