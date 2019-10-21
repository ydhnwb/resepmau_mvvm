package com.ydhnwb.resepmau_mvvm.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.resepmau_mvvm.models.Post
import com.ydhnwb.resepmau_mvvm.repositories.PostRepository

class PostViewModel : ViewModel(){
    private var postRepository : PostRepository = PostRepository()
    private var posts : MutableLiveData<List<Post>> = MutableLiveData()
    private var post : MutableLiveData<Post> = MutableLiveData()


    fun allPost(token : String) : MutableLiveData<List<Post>>{
        posts = postRepository.getAllPost(token)
        return posts
    }

    fun getPost(token : String, id : String) : MutableLiveData<Post>{
        post = postRepository.getPost(token, id)
        return post
    }

    fun getState() = postRepository.getState()
    fun getMessage() = postRepository.getErrorMessage()
}