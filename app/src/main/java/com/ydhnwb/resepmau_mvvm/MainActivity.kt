package com.ydhnwb.resepmau_mvvm

import android.content.pm.ActivityInfo
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ydhnwb.resepmau_mvvm.utilities.Constant
import com.ydhnwb.resepmau_mvvm.viewmodels.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydhnwb.resepmau_mvvm.adapters.PostAdapter
import com.ydhnwb.resepmau_mvvm.ui.BaseUIState
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var postViewModel : PostViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        setupRecycler()
        postViewModel = ViewModelProviders.of(this).get(PostViewModel::class.java)
        postViewModel!!.getState().observe(this, Observer {
            handleStatus(it)
        })
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        postViewModel!!.allPost(Constant.api_token).observe(this, Observer {
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

    private fun handleStatus(status : BaseUIState){
        when(status){
            BaseUIState.ERROR -> {
                isLoading(false)
                toast(postViewModel!!.getMessage())
            }
            BaseUIState.DONE_LOADING -> isLoading(false)
            BaseUIState.NO_NETWORK -> toast("No network connection")
            BaseUIState.LOADING -> isLoading(true)
        }
    }


    private fun isLoading(state : Boolean){
        if(state){
            loading.visibility = View.VISIBLE
            loading.isIndeterminate = true
        }else{
            loading.visibility = View.GONE
            loading.isIndeterminate = false
            loading.progress = 0
        }
    }
    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
