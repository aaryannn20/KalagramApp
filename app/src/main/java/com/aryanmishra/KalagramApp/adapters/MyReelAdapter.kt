package com.aryanmishra.KalagramApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aryanmishra.KalagramApp.Models.Reel
import com.aryanmishra.KalagramApp.databinding.MyPostRvDesignBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class MyReelAdapter (var context: Context, var reelList: ArrayList<Reel>) :
    RecyclerView.Adapter<MyReelAdapter.ViewHolder>(){

    inner class ViewHolder(var binding: MyPostRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return reelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //We use Glide to see preview of uploaded reels which we cannot do with picasso
        //also Glide lib is fast than picasso

        Glide.with(context)
            .load(reelList.get(position).reelUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // shows the thumbnail of the video/reel to be played
            .into(holder.binding.postImage)

    }

}