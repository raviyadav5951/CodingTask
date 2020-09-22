package com.example.codingapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.codingapp.model.ImageResponse
import com.example.codingapp.network.ImgurApiService
import com.example.codingapp.util.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MainViewModel(application: Application):AndroidViewModel(application){
    val images by lazy { MutableLiveData<ImageResponse>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    //create shared pref instance
    val prefs = SharedPreferenceHelper(getApplication())

    //create disposable and release it later in onCleared
    private val disposable = CompositeDisposable()

    //create api service
    private val api = ImgurApiService()

     fun getImages(searchInput: String?) {
        disposable.add(
            api.getImages(searchInput)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ImageResponse>(){
                    override fun onSuccess(imageResponse: ImageResponse) {
                        if(imageResponse.success){
                            loadError.value=false
                            loading.value=false
                            images.value=imageResponse
                        }
                        else{
                            loadError.value = true
                            loading.value = false
                            Log.e("api response fail=", imageResponse.toString())
                        }
                    }

                    override fun onError(e: Throwable) {
                        loading.value=false
                        loadError.value=true
                        images.value=null
                        e.printStackTrace()
                    }

                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}