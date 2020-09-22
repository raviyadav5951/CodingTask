package com.example.codingapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.codingapp.databinding.ActivityMainBinding
import com.example.codingapp.model.ImageResponse

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //creating viewmodel
    private lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initSearchView()
        //creating viewmodel
        viewModel=ViewModelProviders.of(this).get(MainViewModel::class.java)
        //observer assignment
        viewModel.images.observe(this,imagesListDataObserver)
        viewModel.loading.observe(this,loadingLiveDataObserver)
        viewModel.loadError.observe(this,loadingErrorDataObserver)
    }


    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("tag", "text submit is=$query")
                viewModel.getImages(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("tag", "text change is=$newText")
                return false
            }

        })
    }

    //Step2 :Creating observer for each and assign in onViewCreated
    private val imagesListDataObserver= Observer<ImageResponse>{
            list->list?.let {
        binding.recyclerViewImage.visibility= View.VISIBLE
//        listAdapter.updateAnimalList(list)
    }

    }
    private val loadingLiveDataObserver=Observer<Boolean>{
            isLoading->binding.loadingView.visibility= if(isLoading) View.VISIBLE else View.GONE
        if(isLoading){
            binding.listError.visibility= View.GONE
            binding.recyclerViewImage.visibility= View.GONE
        }
    }
    private val loadingErrorDataObserver=Observer<Boolean>{
            isError-> binding.listError.visibility=if(isError) View.VISIBLE else View.GONE
    }

}