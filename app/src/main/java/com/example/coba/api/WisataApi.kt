package com.example.coba.api

class WisataApi {
    companion object{
        val BASE_URL = "http://192.168.0.107/Tubes_Wisata/ci4-apiserver/public/"

        val GET_ALL_URL = BASE_URL + "wisata"
        val GET_BY_ID_URL = BASE_URL + "wisata/"
        val ADD_URL = BASE_URL + "wisata"
        val UPDATE_URL = BASE_URL + "wisata/"
        val DELETE_URL = BASE_URL + "wisata/"
    }
}