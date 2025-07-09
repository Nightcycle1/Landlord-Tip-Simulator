package com.example.landlordtipsimulator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.util.Log

class FenceAdapter(private val fences: List<Int>) : RecyclerView.Adapter<FenceAdapter.FenceViewHolder>() {
    class FenceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.fenceImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FenceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_fence, parent, false)
        return FenceViewHolder(view)
    }

    override fun onBindViewHolder(holder: FenceViewHolder, position: Int) {
        holder.imageView.setImageResource(fences[position])
        Log.d("FenceAdapter", "Binding fence at position $position")
    }

    override fun getItemCount() = fences.size
}