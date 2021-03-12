package com.example.app.view_modal

import androidx.databinding.ObservableField
import com.example.app.model.Animal

class AnimalViewModal(private val animal: Animal) {
    // data
    val info = ObservableField<String> ("${animal.name} shout ${animal.shoutCount}")
    // binding
    fun shout() {
        animal.shoutCount++
        info.set("${animal.name} shout ${animal.shoutCount}")
    }
}