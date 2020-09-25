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
import androidx.recyclerview.widget.RecyclerView
import com.example.codingapp.R
import com.example.codingapp.databinding.ActivityMainBinding
import com.example.codingapp.model.Images
import com.example.codingapp.ui.Constant.VIEW_TYPE_ITEM
import com.example.codingapp.ui.Constant.VIEW_TYPE_LOADING
import com.example.codingapp.ui.adapter.ImageListAdapter
import com.example.codingapp.ui.adapter.OnLoadMoreListener
import com.example.codingapp.ui.adapter.RecyclerViewLoadMoreScroll
import com.example.codingapp.ui.detail.ImageDetailActivity
import com.example.codingapp.util.Util
import com.example.codingapp.util.hideKeyboard
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), ImageListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val imageListAdapter = ImageListAdapter(arrayListOf(), this)

    //creating viewmodel
    private lateinit var viewModel: MainViewModel

    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    lateinit var searchInput: String
    var defaultPageNumber: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //** init viewmodel
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //** initialise the search view
        initSearchView()

        //** All observers
        observeDataChange()

        //**bind recyclerview adapter
        setRVLayoutManager()

        //** Set the scrollListerner of the RecyclerView
        setRVScrollListener()
    }

    /**
     * All Observers are present here
     */
    private fun observeDataChange() {
        viewModel.imageList.observe(this, imagesListDataObserver)
        viewModel.loading.observe(this, loadingLiveDataObserver)
        viewModel.loadError.observe(this, loadingErrorDataObserver)
        viewModel.noResults.observe(this, noResultsDataObserver)
        viewModel.loadMoreImageList.observe(this, loadMoreListDataObserver)
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
                        defaultPageNumber = 1
                        if (Util.isInternetAvailable(this@MainActivity)) {
                            subscriber.onNext(newText!!)
                        } else {
                            Util.showNoInternetMessage(this@MainActivity)
                        }

                        return false
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        defaultPageNumber = 1
                        if (Util.isInternetAvailable(this@MainActivity)) {
                            subscriber.onNext(query!!)
                        } else {
                            Util.showNoInternetMessage(this@MainActivity)
                        }
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
                searchInput = text
                viewModel.getImages(text)
            }
    }


    /**
     * Observers for each of the events.
     */

    private val imagesListDataObserver = Observer<List<Images>> { list ->
        list?.let {
            binding.searchView.hideKeyboard()
            binding.recyclerViewImage.visibility = View.VISIBLE
            imageListAdapter.updateImageList(list)
        }
    }
    private val loadMoreListDataObserver = Observer<List<Images>> { list ->
        list?.let {
            binding.recyclerViewImage.visibility = View.VISIBLE
            //Remove the Loading View
            imageListAdapter.removeLoadingView()
            //We adding the data to our main ArrayList
            imageListAdapter.appendData(list)
            //Change the boolean isLoading to false
            scrollListener.setLoaded()
            //Update the recyclerView in the main thread
            binding.recyclerViewImage.post {
                imageListAdapter.notifyDataSetChanged()
            }
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
        binding.listError.text = getString(R.string.no_results_message)
    }

    /**
     * Handled onclick event of adapter list item
     */
    override fun onItemClicked(image: Images?) {
        val detailActivityIntent = Intent(this, ImageDetailActivity::class.java)

        //Log.e("title",image.title)
        detailActivityIntent.putExtra("image_id", image?.id)
        detailActivityIntent.putExtra("image_link", image?.link)
        detailActivityIntent.putExtra("image_title", image?.title)
        startActivity(detailActivityIntent)
    }

    /**
     * Grid layour manager for adapter
     */
    private fun setRVLayoutManager() {
        mLayoutManager = GridLayoutManager(this, 4)
        binding.recyclerViewImage.layoutManager = mLayoutManager
        binding.recyclerViewImage.setHasFixedSize(true)
        binding.recyclerViewImage.adapter = imageListAdapter
        (mLayoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (imageListAdapter.getItemViewType(position)) {
                        VIEW_TYPE_ITEM -> 1
                        VIEW_TYPE_LOADING -> 4 //number of columns of the grid
                        else -> -1
                    }
                }
            }
    }

    /**
     * Custom load more scroll listener for recycler view
     */
    private fun setRVScrollListener() {
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as GridLayoutManager)
        scrollListener.setOnLoadMoreListener(object :
            OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })

        binding.recyclerViewImage.addOnScrollListener(scrollListener)
    }

    /**
     * Function responsible to make api calls on load more
     *
     */
    private fun loadMoreData() {
        if (Util.isInternetAvailable(this)) {
            //Add the Loading View
            imageListAdapter.addLoadingView()
            viewModel.getImagesByPageNumber(
                pageNum = defaultPageNumber++,
                searchInput = searchInput
            )
        } else {
            Util.showNoInternetMessage(this)
        }

    }

}