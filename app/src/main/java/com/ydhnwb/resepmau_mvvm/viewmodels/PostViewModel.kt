package com.ydhnwb.resepmau_mvvm.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.resepmau_mvvm.models.Post
import com.ydhnwb.resepmau_mvvm.ui.BaseUIState
import com.ydhnwb.resepmau_mvvm.utilities.SingleLiveEvent
import com.ydhnwb.resepmau_mvvm.webservices.ApiClient
import com.ydhnwb.resepmau_mvvm.webservices.WrappedListResponse
import com.ydhnwb.resepmau_mvvm.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostViewModel : ViewModel(){
    private var posts = MutableLiveData<List<Post>>()
    private var post = MutableLiveData<Post>()
    private var state = SingleLiveEvent<BaseUIState>()
    private var message : String? = null
    private var api = ApiClient.instance()

    fun fetchAllPost(token : String){
        state.value = BaseUIState.LOADING
        api.allPost(token).enqueue(object : Callback<WrappedListResponse<Post>> {
            override fun onFailure(call: Call<WrappedListResponse<Post>>, t: Throwable) {
                println(t.message.toString())
                message = t.message.toString()
                state.value = BaseUIState.ERROR
            }

            override fun onResponse(call: Call<WrappedListResponse<Post>>, response: Response<WrappedListResponse<Post>>) {
                if(response.isSuccessful){
                    val data = response.body() as WrappedListResponse<Post>
                    if(data.status.equals("1")){
                        val p = data.data
                        posts.postValue(p)
                    }
                    state.value = BaseUIState.DONE_LOADING
                }else{
                    message = "Something went wrong"
                    state.value = BaseUIState.ERROR
                }
            }
        })
    }

    fun fetchPost(token : String, id : String){
        state.value = BaseUIState.LOADING
        api.getPost(token, id).enqueue(object : Callback<WrappedResponse<Post>>{
            override fun onFailure(call: Call<WrappedResponse<Post>>, t: Throwable) {
                println(t.message.toString())
                message = t.message.toString()
                state.value = BaseUIState.ERROR
            }

            override fun onResponse(call: Call<WrappedResponse<Post>>, response: Response<WrappedResponse<Post>>) {
                if(response.isSuccessful){
                    val resp = response.body() as WrappedResponse<Post>
                    if(resp.status.equals("1")){
                        val p = resp.data
                        post.postValue(p)
                    }
                    state.value = BaseUIState.DONE_LOADING
                }else{
                    message = "Something went wrong"
                    state.value = BaseUIState.ERROR
                }
            }
        })
    }

    fun getPost() = post
    fun getPosts() = posts
    fun getState() = state
    fun getMessage() = message
}