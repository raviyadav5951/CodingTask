package com.example.codingapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codingapp.databinding.ItemImagesBinding
import com.example.codingapp.model.Images
import com.example.codingapp.util.loadImage

class ImageListAdapter(
    private val imageList: ArrayList<Images>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ImageListAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val itemBinding =
            ItemImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(itemBinding)
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image: Images = imageList[position]
        holder.bind(image)
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClicked(image)
        }
    }

    /**
     * Update the list as soon as results arrived in the list
     */
    fun updateImageList(newAnimalList: List<Images>) {
        imageList.clear()
        imageList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    class ImageViewHolder(private val itemBinding: ItemImagesBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(image: Images) {
            itemBinding.imgurImage.loadImage(itemBinding.imgurImage, image.link)
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(image: Images)
    }
}