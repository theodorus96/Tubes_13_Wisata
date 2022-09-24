package com.example.coba

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coba.room.Wisata

class RVWisataAdapter(private val data: ArrayList<Wisata>, private var listener: onAdapterListener) : RecyclerView.Adapter<RVWisataAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): viewHolder{
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_wisata, parent, false)
        return viewHolder(itemView)
    }
    override fun onBindViewHolder(holder: viewHolder,position: Int) {
        val currentItem = data[position]
        holder.tvNamaWisata.text = currentItem.nama
        holder.tvLokasi.text = currentItem.lokasi
        holder.tvImageWisata.setImageResource(R.drawable.image_wakatobi)

        holder.icon_edit.setOnClickListener{
            listener.onUpdate(currentItem)
        }
        holder.icon_delete.setOnClickListener{
            listener.onDelete(currentItem)
        }
    }
    @SuppressLint("NotifyDataSetChange")
    fun setData(list: List<Wisata>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvNamaWisata : TextView = itemView.findViewById(R.id.tv_nama_wisata)
        val tvLokasi : TextView = itemView.findViewById(R.id.tv_lokasi_wisata)
        val tvImageWisata : ImageView = itemView.findViewById(R.id.imageWisata)
        val icon_edit : ImageView = itemView.findViewById(R.id.icon_edit)
        val icon_delete : ImageView = itemView.findViewById(R.id.icon_delete)
    }

    interface onAdapterListener{
        fun onDelete(wisata: Wisata)
        fun onUpdate(wisata : Wisata)
    }
}