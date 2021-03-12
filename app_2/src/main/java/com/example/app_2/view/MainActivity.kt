package com.example.app_2.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.app_2.R
import com.example.app_2.service.PaoService
import com.example.app_2.view_model.PaoViewModel
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: MainActivityBinding
    lateinit var mViewMode: PaoViewModel
    private val BASEURL = "http://api.jcodecraeer.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val remote = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(PaoService::class.java)
        mViewMode = PaoViewModel(remote)
        mBinding.vm = mViewMode
    }
}