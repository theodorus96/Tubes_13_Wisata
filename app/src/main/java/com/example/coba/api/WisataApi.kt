package com.example.coba.api

class WisataApi {
    companion object{
        val BASE_URL = "https://tubeswisata.herokuapp.com/"

        val GET_ALL_URL = BASE_URL + "wisata"
        val GET_BY_ID_URL = BASE_URL + "wisata/"
        val ADD_URL = BASE_URL + "wisata/"
        val UPDATE_URL = BASE_URL + "wisata/"
        val DELETE_URL = BASE_URL + "wisata/"
    }
}