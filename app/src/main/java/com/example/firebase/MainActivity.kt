package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.firebase.handlers.YoutubeHandler
import com.example.firebase.models.Youtube
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var youtubeHandler: YoutubeHandler
    lateinit var channelName: EditText
    lateinit var ranking: EditText
    lateinit var channelLink: EditText
    lateinit var comments: EditText
    lateinit var addButton: Button
    lateinit var youtubeChannels: ArrayList<Youtube>
    lateinit var youtubeListView: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        channelName = findViewById(R.id.channel_name)
        ranking = findViewById(R.id.ranking)
        channelLink = findViewById(R.id.channel_link)
        comments = findViewById(R.id.comments)
        addButton = findViewById(R.id.add)
        youtubeHandler = YoutubeHandler()

        youtubeChannels = ArrayList()
        youtubeListView = findViewById(R.id.channelList)

        addButton.setOnClickListener {
            val name = channelName.text.toString()
            val rank = ranking.text.toString()
            val link = channelLink.text.toString()
            val comment = comments.text.toString()

            val youtube = Youtube(channel = name, rank = rank, link = link, comment = comment)
            if (youtubeHandler.create(youtube)) {
                Toast.makeText(applicationContext, "Channel Added", Toast.LENGTH_SHORT).show()
            }
        }

        registerForContextMenu(youtubeListView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater: MenuInflater = menuInflater
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_channel -> {
                val youtube = youtubeChannels[info.position]
                channelName.setText(youtube.channel)
                ranking.setText(youtube.rank)
                channelLink.setText(youtube.link)
                comments.setText(youtube.comment)
                addButton.setText("Update")
                true
            }
            R.id.delete_channel -> {
                val youtube = youtubeChannels[info.position]
                if (youtubeHandler.delete(youtube)) {
                    youtubeChannels.removeAt(info.position)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Channel Deleted.", Toast.LENGTH_SHORT)
                }
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        youtubeHandler.youtubeRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach { it ->
                    val youtube = it.getValue(Youtube::class.java)
                    youtubeChannels.add(youtube!!)
                }

                val adapter = ArrayAdapter<Youtube>(
                    applicationContext,
                    android.R.layout.simple_list_item_1,
                    youtubeChannels
                )
                youtubeListView.adapter = adapter

            }

            override fun onCancelled(p0: DatabaseError) {
//                    TODO("Not yet implemented")
            }


        })
    }

    fun clearfields(){
        channelName.text.clear()
        ranking.text.clear()
        channelLink.text.clear()
        comments.text.clear()
    }
}



