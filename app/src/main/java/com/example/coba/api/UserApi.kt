package com.example.coba.api

class UserApi {
    companion object{
        val BASE_URL = "http://192.168.100.61/Tubes_Wisata/ci4-apiserver/public/"

        val GET_BY_ID_URL = BASE_URL + "user/"
        val ADD_URL = BASE_URL + "user"
        val UPDATE_URL = BASE_URL + "user/"
        val LOGIN_URL = BASE_URL + "login"
    }
}