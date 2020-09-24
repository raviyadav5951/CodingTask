package com.example.codingapp.ui.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.codingapp.databinding.ItemImagesBinding
import com.example.codingapp.databinding.ProgressLoadingBinding
import com.example.codingapp.model.Images
import com.example.codingapp.ui.Constant
import com.example.codingapp.util.loadImage

class ImageListAdapter(
    private var imageList: ArrayList<Images?>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            Constant.VIEW_TYPE_ITEM -> {
                val itemBinding =
                    ItemImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ImageViewHolder(itemBinding)
            }
            else -> {
                val itemBinding =
                    ProgressLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(itemBinding)
            }
        }
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            val image: Images? = imageList[position]
            if (holder is ImageViewHolder) {
                holder.bind(image)
                holder.itemView.setOnClickListener {
                    itemClickListener.onItemClicked(image)
                }
            }

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
        ViewHolder(itemBinding.root) {
        fun bind(image: Images?) {
            itemBinding.imgurImage.loadImage(itemBinding.imgurImage, image?.link)
        }
    }

    class LoadingViewHolder(private val itemBinding: ProgressLoadingBinding) :
        ViewHolder(itemBinding.root)

    interface OnItemClickListener {
        fun onItemClicked(image: Images?)
    }

    /**
     * Show progress loading view at the bottom
     */
    fun addLoadingView() {
        //add loading item
        Handler().post {
            imageList.add(null)
            notifyItemInserted(imageList.size - 1)
        }
    }

    /**
     * Append data to existing list on load more
     */
    fun appendData(imageViews: List<Images>) {
        this.imageList.addAll(imageViews)
        notifyDataSetChanged()
    }

    /**
     * Remove the loading view after the new list is ready to be appended
     */
    fun removeLoadingView() {
        //Remove loading item
        if (imageList.size != 0) {
            imageList.removeAt(imageList.size - 1)
            notifyItemRemoved(imageList.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (imageList[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }
}