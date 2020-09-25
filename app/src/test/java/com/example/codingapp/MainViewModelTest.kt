package com.example.codingapp

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.codingapp.di.AppModule
import com.example.codingapp.di.DaggerViewModelComponent
import com.example.codingapp.model.Data
import com.example.codingapp.model.ImageResponse
import com.example.codingapp.model.Images
import com.example.codingapp.network.ImgurApiService
import com.example.codingapp.ui.main.MainViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @get:Rule
    var rule= InstantTaskExecutorRule()


    @Mock
    lateinit var imgurApiService: ImgurApiService

    val application= Mockito.mock(Application::class.java)

    var listViewModel=MainViewModel(application)


    @Before
    fun setUpRxSchedulers(){
        val immediate=object: Scheduler(){
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker({it.run()},true)
            }
        }
        RxJavaPlugins.setInitNewThreadSchedulerHandler {scheduler->immediate  }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate  }
    }


    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(imgurApiService))
            .build()
            .inject(listViewModel)

    }

    @Test
    fun getImageApiSuccess(){
        //created necessary objects for proceeding ahead
        //Going from bottom to top approach to provide the objects
        //imageObject->imageList
        //imageList->imageData
        //imageData->listOfData
        //listOfData->imageResponse

        val imageObject=Images(id = "i1",null,null,null,type = "image/jpeg")
        val imageList= listOf(imageObject)
        val imageData= Data(id = "d1", null,images = imageList)
        val listOfData= listOf(imageData)
        val imageResponse=ImageResponse(data = listOfData,success = true,status = 200)

        val testSingle= Single.just(imageResponse)
        val searchValue="cats"
        Mockito.`when`(imgurApiService.getImages(searchInput = searchValue)).thenReturn(testSingle)
        listViewModel.getImages(searchValue)

        Assert.assertEquals(1,listViewModel.imageList.value?.size)
        Assert.assertEquals(false,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
        Assert.assertEquals(false,listViewModel.noResults.value)
    }


    @Test
    fun getImageApiFailure(){
//        val imageObject=Images(id = "i1",null,null,null,null)
//        val imageList= listOf(imageObject)
//        val imageData= Data(id = "d1", null,images = imageList)
//        val listOfData= listOf(imageData)
        //Pass NULL as Data
        val imageResponse=ImageResponse(data = null,success = true,status = 200)

        val testSingle= Single.just(imageResponse)
        val searchValue="cats"
        Mockito.`when`(imgurApiService.getImages(searchInput = searchValue)).thenReturn(testSingle)
        listViewModel.getImages(searchValue)

        Assert.assertEquals(null,listViewModel.imageList.value?.size)
        Assert.assertEquals(false,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
        Assert.assertEquals(true,listViewModel.noResults.value)
    }
}