package com.ydhnwb.resepmau_mvvm

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.ydhnwb.resepmau_mvvm.utilities.Constant
import com.ydhnwb.resepmau_mvvm.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydhnwb.resepmau_mvvm.adapters.PostAdapter
import com.ydhnwb.resepmau_mvvm.models.Post
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var postViewModel : PostViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupRecycler()
        postViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    private fun setupRecycler(){
        rv_main.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PostAdapter(mutableListOf(), this@MainActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        postViewModel!!.all(Constant.api_token).observe(this, Observer<List<Post>> {
            rv_main.adapter?.let {adapter ->
                if(adapter is PostAdapter){
                    adapter.changeList(it)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
