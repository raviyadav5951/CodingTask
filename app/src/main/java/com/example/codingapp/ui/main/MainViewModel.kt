package com.example.codingapp.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.codingapp.model.ImageResponse
import com.example.codingapp.model.Images
import com.example.codingapp.network.ImgurApiService
import com.example.codingapp.util.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val loadError by lazy { MutableLiveData<Boolean>() }
    val noResults by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    val imageList by lazy { MutableLiveData<List<Images>>() }

    //create shared pref instance
    val prefs = SharedPreferenceHelper(getApplication())

    //create disposable and release it later in onCleared
    private val disposable = CompositeDisposable()

    //create api service
    private val api = ImgurApiService()

    fun getImages(searchInput: String?) {
        loading.postValue(true)
        disposable.add(
            api.getImages(searchInput)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ImageResponse>() {
                    override fun onSuccess(imageResponse: ImageResponse) {
                        if (imageResponse.success) {
                            noResults.postValue(false)
                            loadError.postValue(false)
                            loading.postValue(false)
                            filterImagesFromGallery(imageResponse)
                        } else {
                            noResults.postValue(false)
                            loadError.postValue(true)
                            loading.postValue(false)
                            Log.e("api response fail=", imageResponse.toString())
                        }
                    }

                    override fun onError(e: Throwable) {
                        loading.postValue(false)
                        noResults.postValue(false)
                        loadError.postValue(true)
                        imageList.postValue(null)
                        e.printStackTrace()
                    }

                })
        )
    }

    /**
     * @param imageResponse the response from server
     * We are performing filter to separate the jpeg/png/gif images from gallery search.
     */

    fun filterImagesFromGallery(imageResponse: ImageResponse) {
        var finalList = mutableListOf<Images>()

        if (imageResponse.success && !imageResponse.data.isNullOrEmpty()) {
            for (data in imageResponse.data) {
                if (!data.images.isNullOrEmpty()) {
                    val list = data.images.filter {
                        it.type == "image/jpeg" || it.type == "image/png" || it.type == "image/gif"
                    }.toMutableList()

                    //assigning titles from parent gallery to child image
                    list.forEach { it.title=data.title }

                    finalList.addAll(list)
                }
            }

            imageList.postValue(finalList)

           // Log.d("size", "size=${finalList.size}")
        }
        else if(imageResponse.success && imageResponse.data.isNullOrEmpty()){
            noResults.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}