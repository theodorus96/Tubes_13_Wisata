package com.example.coba

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coba.databinding.RvWisataBinding
import com.example.coba.room.Wisata

class RVWisataAdapter(private val data: ArrayList<Wisata>, private var listener: onAdapterListener) : RecyclerView.Adapter<RVWisataAdapter.viewHolder>() {
    private lateinit var binding: RvWisataBinding
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): viewHolder{
        binding = RvWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder,position: Int) {
        val currentItem = data[position]
        holder.bind(currentItem)
//        holder.tvNamaWisata.text = currentItem.nama
//        holder.tvLokasi.text = currentItem.lokasi
//        holder.tvImageWisata.setImageResource(R.drawable.image_wakatobi)

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

    class viewHolder(val binding: RvWisataBinding) : RecyclerView.ViewHolder(binding.root){
//        val tvNamaWisata : TextView = itemView.findViewById(R.id.tv_nama_wisata)
//        val tvLokasi : TextView = itemView.findViewById(R.id.tv_lokasi_wisata)
//        val tvImageWisata : ImageView = itemView.findViewById(R.id.imageWisata)
        fun bind(wisata: Wisata){
            binding.wisata = wisata
        }
        val icon_edit : ImageView = itemView.findViewById(R.id.icon_edit)
        val icon_delete : ImageView = itemView.findViewById(R.id.icon_delete)
    }

    interface onAdapterListener{
        fun onDelete(wisata: Wisata)
        fun onUpdate(wisata : Wisata)
    }
}