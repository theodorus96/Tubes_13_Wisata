package com.example.coba.entity

import com.example.coba.R

class  Wisata(var name: String, var lokasi: String, var image:Int) {
    companion object{
        @JvmField
        var listOfWisatas = arrayOf(
            Wisata("Pulau Derawan","Kalimantan Timur", R.drawable.image_derawan),
            Wisata("Way Kambas","Lampung",R.drawable.image_waykambas),
            Wisata("Parai Tenggiri","Bangka Belitung",R.drawable.image_pantaitenggiri),
            Wisata("Nusa Dua","Bali",R.drawable.image_nusadua),
            Wisata("Gunung Rinjani","Lombok, NTB",R.drawable.image_rinjani),
            Wisata("Danau Toba","Sumatera Utara",R.drawable.image_danautoba),
            Wisata("Nusa Penida","Bali",R.drawable.image_nusapenida),
            Wisata("Laut Bunaken","Sulawesi Utara",R.drawable.image_bunake),
            Wisata("Wakatobi","Sulawesi Tenggara",R.drawable.image_wakatobi),
            Wisata("Raja Ampat","Papua Barat",R.drawable.image_rajaampat)
        )
    }
}