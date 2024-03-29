package com.ydhnwb.resepmau_mvvm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.ydhnwb.resepmau_mvvm.utilities.Constant
import com.ydhnwb.resepmau_mvvm.viewmodels.RecipeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ydhnwb.resepmau_mvvm.adapters.PostAdapter
import com.ydhnwb.resepmau_mvvm.viewmodels.RecipeState
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var postViewModel : RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        isLoggedIn()
        setupRecycler()
        postViewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        postViewModel.getPosts().observe(this, Observer {
            rv_main.adapter?.let {adapter ->
                if(adapter is PostAdapter){
                    adapter.changeList(it)
                }
            }
        })
        postViewModel.getState().observe(this, Observer {
            handleStatus(it)
        })
        fab.setOnClickListener {
            startActivity(Intent(this, RecipeActivity::class.java).apply {
                putExtra("is_update", false)
            })
        }
    }

    private fun handleStatus(it : RecipeState){
        when(it){
            is RecipeState.Loading -> isLoading(it.state)
            is RecipeState.Error -> {
                isLoading(false)
                toast(it.message)
            }
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

    private fun isLoggedIn(){
        if(Constant.getToken(this).equals("undefined")){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also { finish() }
        }
    }

    private fun setupRecycler(){
        rv_main.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = PostAdapter(mutableListOf(), this@MainActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        postViewModel.fetchAllPost(Constant.getToken(this))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Constant.clearToken(this)
                startActivity(Intent(this, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }).also { finish() }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
