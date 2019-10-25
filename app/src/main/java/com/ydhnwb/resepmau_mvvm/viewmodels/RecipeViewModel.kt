package com.ydhnwb.resepmau_mvvm.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.resepmau_mvvm.models.Recipe
import com.ydhnwb.resepmau_mvvm.utilities.SingleLiveEvent
import com.ydhnwb.resepmau_mvvm.webservices.ApiClient
import com.ydhnwb.resepmau_mvvm.webservices.WrappedListResponse
import com.ydhnwb.resepmau_mvvm.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeViewModel : ViewModel(){
    private var recipes = MutableLiveData<List<Recipe>>()
    private var recipe = MutableLiveData<Recipe>()
    private var state : SingleLiveEvent<RecipeState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchAllPost(token : String){
        state.value = RecipeState.Loading(true)
        api.allPost(token).enqueue(object : Callback<WrappedListResponse<Recipe>> {
            override fun onFailure(call: Call<WrappedListResponse<Recipe>>, t: Throwable) {
                println(t.message.toString())
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Recipe>>, response: Response<WrappedListResponse<Recipe>>) {
                if(response.isSuccessful){
                    val data = response.body() as WrappedListResponse<Recipe>
                    if(data.status.equals("1")){
                        val p = data.data
                        recipes.postValue(p)
                    }
                    state.value = RecipeState.Loading(false)
                }else{
                    state.value = RecipeState.Error("Oops. Something went wrong")
                }
            }
        })
    }

    fun fetchPost(token : String, id : String){
        state.value = RecipeState.Loading(true)
        api.getPost(token, id).enqueue(object : Callback<WrappedResponse<Recipe>>{
            override fun onFailure(call: Call<WrappedResponse<Recipe>>, t: Throwable) {
                println(t.message.toString())
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Recipe>>, response: Response<WrappedResponse<Recipe>>) {
                if(response.isSuccessful){
                    val resp = response.body() as WrappedResponse<Recipe>
                    if(resp.status.equals("1")){
                        val p = resp.data
                        recipe.postValue(p)
                    }
                    state.value = RecipeState.Loading(false)
                }else{
                    state.value = RecipeState.Error("Oops. Something went wrong")
                }
            }
        })
    }

    fun create(token: String, r : Recipe){
        state.value = RecipeState.Loading(true)
        api.createPost(token, r.title.toString(), r.content.toString()).enqueue(object :Callback<WrappedResponse<Recipe>>{
            override fun onFailure(call: Call<WrappedResponse<Recipe>>, t: Throwable) {
                println(t.message)
                state.value = RecipeState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Recipe>>, response: Response<WrappedResponse<Recipe>>) {
                if(response.isSuccessful){
                    val resp = response.body() as WrappedResponse<Recipe>
                    if(resp.status.equals("1")){
                        state.value = RecipeState.IsSuccess(true)
                    }else{
                        state.value = RecipeState.IsSuccess(false)
                    }
                }else{
                    state.value = RecipeState.Error("Oops. Something went wrong")
                }
            }
        })
    }

    fun validate(title : String, content : String){
        //still in development
    }

    fun getOnePost() = recipe
    fun getPosts() = recipes
    fun getState() = state
}



sealed class RecipeState{
    data class Loading(var state : Boolean = false) : RecipeState()
    data class RecipeInvalid(var title : String? = null, var content : String? = null) : RecipeState()
    data class Error(var message : String?) : RecipeState()
    data class IsSuccess(var state : Boolean = false) : RecipeState()
}