package com.example.coba.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.coba.ActivityReview
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList
import com.example.coba.AddEditActivityReview
import com.example.coba.R
import com.example.coba.models.Review


class ReviewAdapter (private var reviewList: ArrayList<Review>, context: Context):
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>(), Filterable {

        private var filteredReviewList: MutableList<Review>
        private val context: Context

        init {
            filteredReviewList = ArrayList(reviewList)
            this.context = context
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.item_review, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return filteredReviewList.size
        }

        fun setReviewList(reviewListData: Array<Review>){
            filteredReviewList.clear()
            filteredReviewList.addAll(reviewListData)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val review =filteredReviewList[position]
            holder.tvNama.text = review.nama
            holder.tvKomentar.text = review.komentar
            holder.tvNilai.text = review.nilai
            holder.tvSaran.text = review.saran

            holder.btnDelete.setOnClickListener {
                val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context)
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                    .setMessage("Apakah anda yakin ingin menghapus komentar ini?")
                    .setNegativeButton("Batal", null)
                    .setPositiveButton("Hapus"){_,_ ->
                        if (context is ActivityReview) review.id?.let { it1 ->
                            context.deleteReview(
                                it1
                            )
                        }
                    }
                    .show()

            }

            holder.cvReview.setOnClickListener {
                val i = Intent(context, AddEditActivityReview::class.java)
                i.putExtra("id", review.id)
                if(context is ActivityReview)
                    context.startActivityForResult(i, ActivityReview.LAUNCH_ADD_ACTIVITY)
            }


        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(charSequence: CharSequence): FilterResults {
                    val charSequenceString = charSequence.toString()
                    val filtered: MutableList<Review> = java.util.ArrayList()
                    if(charSequenceString.isEmpty()){
                        filtered.addAll(reviewList)
                    }else{
                        for (review in reviewList){
                            if(review.nama.lowercase(Locale.getDefault())
                                    .contains(charSequenceString.lowercase(Locale.getDefault()))

                            )filtered.add(review)

                        }
                    }
                    val filterResults = FilterResults()
                    filterResults.values = filtered
                    return filterResults

                }

                override fun publishResults( CharSequence: CharSequence, filterResults: FilterResults) {
                    filteredReviewList.clear()
                    filteredReviewList.addAll(filterResults.values as List<Review>)
                    notifyDataSetChanged()
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var tvNama: TextView
            var tvKomentar: TextView
            var tvNilai: TextView
            var tvSaran: TextView
            var btnDelete: ImageButton
            var cvReview: CardView

            init {
                tvNama = itemView.findViewById(R.id.tv_nama)
                tvKomentar = itemView.findViewById(R.id.tv_komentar)
                tvNilai = itemView.findViewById(R.id.tv_nilai)
                tvSaran = itemView.findViewById(R.id.tv_saran)
                btnDelete = itemView.findViewById(R.id.btn_delete)
                cvReview = itemView.findViewById(R.id.cv_review)
            }

        }
}