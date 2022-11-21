package com.example.coba.api

class UserApi {
    companion object{
        val BASE_URL = "https://tubeswisata.herokuapp.com/"

        val GET_ALL_URL = BASE_URL + "user/"
        val GET_BY_ID_URL = BASE_URL + "user/"
        val ADD_URL = BASE_URL + "user"
        val UPDATE_URL = BASE_URL + "user/"
        val LOGIN_URL = BASE_URL + "login"
    }
}