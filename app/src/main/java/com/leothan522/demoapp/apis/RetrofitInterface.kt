package com.leothan522.demoapp.apis

import com.leothan522.demoapp.model.RespuestaSimple
import com.leothan522.demoapp.model.Usuario
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitInterface {

    @FormUrlEncoded
    @POST("login.php")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fcm_token") fcm_token: String
    ) : Call<Usuario>

    @FormUrlEncoded
    @POST("register.php")
    fun registrarUsuario(
        @Field("name") name : String,
        @Field("email") email: String,
        @Field("telefono") telefono :String,
        @Field("password") password: String,
        @Field("fcm_token") fcm_token: String
    ) : Call<Usuario>

    @FormUrlEncoded
    @POST("recuperar.php")
    fun recuperarCLave(@Field("email") email: String) : Call<RespuestaSimple>

    @FormUrlEncoded
    @POST("update.php")
    fun actualizarUsuario(
        @Field("name") name : String,
        @Field("email") email: String,
        @Field("telefono") telefono :String,
        @Field("nuevo_password") actual: String,
        @Field("password") password: String,
        @Field("id") id: String,
        @Field("fcm_token") fcm_token: String
    ) : Call<Usuario>

}