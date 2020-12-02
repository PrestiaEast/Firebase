package com.example.firebase.handlers

import com.example.firebase.models.Youtube
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class YoutubeHandler {
    var database: FirebaseDatabase
    var youtubeRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        youtubeRef = database.getReference("youtube")
    }

    fun create(youtube: Youtube): Boolean {
        val id = youtubeRef.push().key
        youtube.id = id

        youtubeRef.child(id!!).setValue(youtube)
        return true
    }
}