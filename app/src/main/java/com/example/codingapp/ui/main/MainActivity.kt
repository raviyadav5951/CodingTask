package com.example.codingapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.codingapp.R
import com.example.codingapp.databinding.ActivityMainBinding
import com.example.codingapp.model.Images
import com.example.codingapp.ui.adapter.ImageListAdapter
import com.example.codingapp.ui.detail.ImageDetailActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(),ImageListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val imageListAdapter = ImageListAdapter(arrayListOf(),this)

    //creating viewmodel
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //initialise the search view
        initSearchView()

        //init viewmodel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //observer assignment
        viewModel.imageList.observe(this, imagesListDataObserver)
        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, loadingErrorDataObserver)
        viewModel.noResults.observe(this, noResultsDataObserver)

        //bind recyclerview adapter
        binding.recyclerViewImage.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = imageListAdapter
        }
    }

    /**
     * Searchview control handling for searching the image
     */
    @SuppressLint("CheckResult")
    private fun initSearchView() {
        // Set up the query listener that executes the search
        Observable
            .create(ObservableOnSubscribe<String> { subscriber ->
                binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean {
                        subscriber.onNext(newText!!)
                        return false
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        subscriber.onNext(query!!)
                        return false
                    }
                })
            })
            .map { text -> text.toLowerCase().trim() }
            .debounce(250, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { text -> text.isNotBlank() }
            .subscribe { text ->
                Log.d("rx", "subscriber: $text")
                viewModel.getImages(text)
            }

    }

    /**
     * Observers for each of the events.
     */

    private val imagesListDataObserver = Observer<List<Images>> { list ->
        list?.let {
            binding.recyclerViewImage.visibility = View.VISIBLE
            imageListAdapter.updateImageList(list)
        }
    }

    private val loadingLiveDataObserver = Observer<Boolean> { isLoading ->
        binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            binding.listError.visibility = View.GONE
            binding.recyclerViewImage.visibility = View.GONE
        }
    }
    private val loadingErrorDataObserver = Observer<Boolean> { isError ->
        binding.listError.visibility = if (isError) View.VISIBLE else View.GONE
    }
    private val noResultsDataObserver = Observer<Boolean> { isError ->
        binding.listError.visibility = if (isError) View.VISIBLE else View.GONE
        binding.listError.text=getString(R.string.no_results_message)
    }


    override fun onItemClicked(image: Images) {
        val detailActivityIntent=Intent(this,ImageDetailActivity::class.java)

        //Log.e("title",image.title)
        detailActivityIntent.putExtra("image_id",image.id)
        detailActivityIntent.putExtra("image_link",image.link)
        detailActivityIntent.putExtra("image_title",image.title)
        startActivity(detailActivityIntent)
    }


}