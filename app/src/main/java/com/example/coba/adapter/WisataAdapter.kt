package com.example.coba.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.coba.EditWisataActivity
import com.example.coba.R
import com.example.coba.fragment.FragmentWisata
import com.example.coba.models.Wisata
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class WisataAdapter(private var wisataList: ArrayList<Wisata>, val context: FragmentWisata) :
    RecyclerView.Adapter<WisataAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_wisata, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return wisataList.size
    }

    fun setWisataList(wisataListData: Array<Wisata>){
        wisataList.clear()
        wisataList.addAll(wisataListData)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val wisata = wisataList[position]
        holder.tvNamaWisata.text = wisata.nama
        holder.tvLokasi.text = wisata.lokasi


        holder.icon_delete.setOnClickListener{
            val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context.requireContext())
            materialAlertDialogBuilder.setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin ingin menghapus wisata ini?")
                .setNegativeButton("Batal", null)
                .setPositiveButton("Hapus"){_,_ ->
                        context.deleteWisata(
                            wisata.id!!.toInt()
                        )
                }
                .show()
        }
        holder.icon_edit.setOnClickListener{
            val intent = Intent(context.requireContext(), EditWisataActivity::class.java)
            println()
            intent.putExtra("id", wisata.id)
            intent.putExtra("namaWisata", wisata.nama)
            intent.putExtra("lokasiWisata", wisata.lokasi)
            intent.putExtra("intent_type", 2)
            context.startActivity(intent)

        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNamaWisata: TextView
        var tvLokasi: TextView
        var icon_delete: ImageView
        var icon_edit: ImageView
        var cvWisata: CardView

        init{
            tvNamaWisata = itemView.findViewById(R.id.tv_nama_wisata)
            tvLokasi = itemView.findViewById(R.id.tv_lokasi_wisata)
            icon_edit = itemView.findViewById(R.id.icon_edit)
            icon_delete = itemView.findViewById(R.id.icon_delete)
            cvWisata = itemView.findViewById(R.id.cv_wisata)
        }
    }

}