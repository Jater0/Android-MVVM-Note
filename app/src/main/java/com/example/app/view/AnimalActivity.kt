package com.example.app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.app.R
import com.example.app.model.Animal
import com.example.app.view_modal.AnimalViewModal

class AnimalActivity : AppCompatActivity() {
    lateinit var mViewMode: AnimalViewModal
    lateinit var mBinding: AnimalActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val animal = Animal("Dog", 0)
        mViewMode = AnimalViewModal(animal)
        mBinding.vm = mViewMode
    }
}