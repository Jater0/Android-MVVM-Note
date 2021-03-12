package com.example.app_2.view_model

import android.annotation.SuppressLint
import androidx.databinding.ObservableField
import com.example.app_2.model.Article
import com.example.app_2.service.PaoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PaoViewModel(private val remote: PaoService) {
    val articleDetail = ObservableField<String>()
    @SuppressLint("CheckResult")
    fun loadArticle() {
        remote.getArticleDetail(8773)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                t: Article? -> articleDetail.set(t?.toString())
            }, {
                t: Throwable? -> articleDetail.set(t?.message?:"error")
            })
    }
}