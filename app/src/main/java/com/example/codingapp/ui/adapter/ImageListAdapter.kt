package com.example.codingapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codingapp.R
import com.example.codingapp.databinding.ItemImagesBinding
import com.example.codingapp.model.Images
import com.example.codingapp.util.getProgressDrawable
import com.example.codingapp.util.loadImage

class ImageListAdapter(private val imageList:ArrayList<Images>):RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val itemBinding=ItemImagesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImageViewHolder(itemBinding)
    }

    override fun getItemCount()=imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image:Images=imageList[position]
        holder.bind(image)
    }

    fun updateImageList(newAnimalList:List<Images>){
        Log.e("tag","iupdateImageLists")
        imageList.clear()
        imageList.addAll(newAnimalList)
        Log.e("tag","iupdateImageLists size=${imageList.size}")
        notifyDataSetChanged()
    }

    class ImageViewHolder(private val itemBinding: ItemImagesBinding):RecyclerView.ViewHolder(itemBinding.root){
        fun bind(image: Images) {

            itemBinding.imgurImage.loadImage(itemBinding.imgurImage,image.link)

        }

    }
}