package com.example.coba.entity

import com.example.coba.R

class  wisata(var name: String, var lokasi: String, var image:Int) {
    companion object{
        @JvmField
        var listOfWisata = arrayOf(
            wisata("Pulau Derawan","Kalimantan Timur", R.drawable.image_derawan),
            wisata("Way Kambas","Lampung",R.drawable.image_waykambas),
            wisata("Parai Tenggiri","Bangka Belitung",R.drawable.image_pantaitenggiri),
            wisata("Nusa Dua","Bali",R.drawable.image_nusadua),
            wisata("Gunung Rinjani","Lombok, NTB",R.drawable.image_rinjani),
            wisata("Danau Toba","Sumatera Utara",R.drawable.image_danautoba),
            wisata("Nusa Penida","Bali",R.drawable.image_nusapenida),
            wisata("Laut Bunaken","Sulawesi Utara",R.drawable.image_bunake),
            wisata("Wakatobi","Sulawesi Tenggara",R.drawable.image_wakatobi),
            wisata("Raja Ampat","Papua Barat",R.drawable.image_rajaampat)
        )
    }
}