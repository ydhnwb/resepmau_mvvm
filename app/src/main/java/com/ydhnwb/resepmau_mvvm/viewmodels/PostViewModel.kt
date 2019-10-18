package com.ydhnwb.resepmau_mvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.resepmau_mvvm.models.Post
import com.ydhnwb.resepmau_mvvm.repositories.PostRepository

class PostViewModel : ViewModel(){
    private var postRepo : PostRepository? = PostRepository()
    private var posts : MutableLiveData<List<Post>>? = null

    fun all(token : String) : MutableLiveData<List<Post>>{
        posts = postRepo!!.getAllPost(token)
        return posts as MutableLiveData<List<Post>>
    }

}