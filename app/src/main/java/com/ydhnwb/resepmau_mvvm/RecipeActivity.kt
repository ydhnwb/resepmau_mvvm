package com.ydhnwb.resepmau_mvvm

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ydhnwb.resepmau_mvvm.models.Recipe
import com.ydhnwb.resepmau_mvvm.utilities.Constant
import com.ydhnwb.resepmau_mvvm.viewmodels.RecipeState
import com.ydhnwb.resepmau_mvvm.viewmodels.RecipeViewModel
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.content_recipe.*

class RecipeActivity : AppCompatActivity() {
    private lateinit var recipeViewModel: RecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        toolbar.setNavigationOnClickListener { finish() }
        recipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel::class.java)
        if (isUpdate()){
            doUpdate()
            recipeViewModel.fetchPost(Constant.getToken(this), getId().toString())
            recipeViewModel.getOnePost().observe(this, Observer {
                fill(it)
            })
        }else{
            doCreate()
        }
        recipeViewModel.getState().observer(this, Observer {
            handleState(it)
        })
    }

    private fun isUpdate() = intent.getBooleanExtra("is_update", false)
    private fun getId() = intent.getIntExtra("id", 0)

    private fun doCreate(){
        btn_submit.setOnClickListener {
            val title = et_title.text.toString().trim()
            val content = et_content.text.toString().trim()
            if(recipeViewModel.validate(title, content)){
                recipeViewModel.create(Constant.getToken(this@RecipeActivity), Recipe(title = title, content = content))
            }
        }
    }

    private fun doUpdate(){
        btn_submit.setOnClickListener {
            val title = et_title.text.toString().trim()
            val content = et_content.text.toString().trim()
            if(recipeViewModel.validate(title, content)){
                recipeViewModel.update(Constant.getToken(this@RecipeActivity), Recipe(id = getId(), title = title, content = content))
            }
        }
    }

    private fun fill(r : Recipe){
        et_title.setText(r.title)
        et_content.setText(r.content)
    }

    private fun handleState(it : RecipeState){
        when(it){
            is RecipeState.IsSuccessDelete -> {
                if(it.state){
                    finish()
                }
            }
            is RecipeState.ShowMessage -> {
                toast("Success deleted")
                isLoading(false)
            }
            is RecipeState.Loading -> isLoading(it.state)
            is RecipeState.Reset -> setError(null, null)
            is RecipeState.Error -> toast(it.message)
            is RecipeState.RecipeInvalid -> {
                it.title?.let {
                    setError(it, null)
                }
                it.content?.let {
                    setError(null, it)
                }
            }
            is RecipeState.IsSuccess -> {
                if(isUpdate()){
                    toast("Successfully updated")
                }else{
                    toast("Successfully created")
                }
                finish()
            }
        }
    }


    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun setError(err_title : String?, err_content : String?){
        in_title.error = err_title
        in_content.error = err_content
    }
    private fun isLoading(state : Boolean){ btn_submit.isEnabled = !state }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(isUpdate()){
            menuInflater.inflate(R.menu.menu_recipe, menu)
            return true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_delete -> {
                delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun delete() = recipeViewModel.delete(Constant.getToken(this), getId().toString())

}
