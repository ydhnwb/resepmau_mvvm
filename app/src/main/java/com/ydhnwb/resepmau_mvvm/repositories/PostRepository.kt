package com.ydhnwb.resepmau_mvvm.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ydhnwb.resepmau_mvvm.models.Post
import com.ydhnwb.resepmau_mvvm.webservices.ApiClient
import com.ydhnwb.resepmau_mvvm.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostRepository {
    private var api = ApiClient.instance()

    fun getAllPost(token : String) : MutableLiveData<List<Post>>{
        val posts = MutableLiveData<List<Post>>()
        api.allPost(token).enqueue(object : Callback<WrappedListResponse<Post>>{
            override fun onFailure(call: Call<WrappedListResponse<Post>>, t: Throwable) {
                println(t.message.toString())
            }

            override fun onResponse(call: Call<WrappedListResponse<Post>>, response: Response<WrappedListResponse<Post>>) {
                if(response.isSuccessful){
                    val data = response.body() as WrappedListResponse<Post>
                    println(data.status)
                    if(data.status.equals("1")){
                        val p = data.data
                        println("SOKSES")
                        posts.postValue(p)
                    }else{
                        println("KONTORU")
                    }
                }else{
                    println("Something went wrong....")
                    println(response.code())
                }
            }
        })

        return posts
    }
}