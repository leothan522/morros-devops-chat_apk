package com.leothan522.demoapp.apis

import com.leothan522.demoapp.prefs.SharedApp.Companion.prefs

class Direcciones() {

    private val id: String = prefs.getID()

    val BASE_URL_LOCAL: String = "http://172.16.10.100/morros-devops/android/"
    val BASE_URL_ANDROID: String = "https://morros-devops.xyz/android/"
    val BASE_URL_LARAVEL: String = "https://morros-devops.xyz/laravel/public"

    val URL_CHAT: String = "$BASE_URL_LARAVEL/chat-directo/1"
    /*val URL_PRINCIPAL: String = "$BASE_URL_LARAVEL$id/home"
    val URL_CATEGORIAS: String = "$BASE_URL_LARAVEL$id/listarcategorias"
    val URL_TIENDAS: String = "$BASE_URL_LARAVEL$id/listartiendas"
    val URL_FAVORITOS: String = "$BASE_URL_LARAVEL$id/favoritos"
    val URL_CARRITO: String = "$BASE_URL_LARAVEL$id/carrito"
    val URL_PEDIDOS: String = "$BASE_URL_LARAVEL$id/verpedidos"*/

}