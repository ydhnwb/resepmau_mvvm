package com.ydhnwb.resepmau_mvvm.viewmodels

import androidx.lifecycle.ViewModel
import com.ydhnwb.resepmau_mvvm.models.User
import com.ydhnwb.resepmau_mvvm.utilities.Constant
import com.ydhnwb.resepmau_mvvm.utilities.SingleLiveEvent
import com.ydhnwb.resepmau_mvvm.webservices.ApiClient
import com.ydhnwb.resepmau_mvvm.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private var state : SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun login(email : String, password : String){
        state.value = UserState.Loading(true)
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message.toString())
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val resp = response.body() as WrappedResponse<User>
                    if(resp.status.equals("1")){
                        state.value = UserState.Success("Bearer ${resp.data!!.api_token}")
                    }else{
                        state.value = UserState.Failed("Login failed. Please check your email and password")
                    }
                }else{
                    state.value = UserState.Error("Login failed")
                }
            }
        })
    }

    fun validate(name : String?, email: String, password: String) : Boolean{
        state.value = UserState.Reset

        if(name != null){
            if(name.isEmpty()){
                state.value = UserState.Message("Name cannot be empty")
                return false
            }

            if (name.length < 5){
                state.value = UserState.UserInvalid("Name at lease five characaters")
                return false
            }
        }

        if(email.isEmpty() || password.isEmpty()){
            state.value = UserState.Message("Please fill all form")
            return false
        }
        if(!Constant.isValidEmail(email)){
            state.value = UserState.UserInvalid(emailIsValid = "Email is not valid")
            return false
        }
        if(!Constant.isValidPassword(password)){
            state.value = UserState.UserInvalid(passwordIsValid = "Password at least contains eight character")
            return false
        }
        return true
    }


    fun register(name : String, email : String, password: String){
        state.value = UserState.Loading(true)
        api.register(name, email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message)
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val b = response.body() as WrappedResponse
                    if(b.status.equals("1")){
                        state.value = UserState.Success(b.data!!.api_token)
                    }else{
                        state.value = UserState.Failed("Failed to create account")
                    }
                }else{
                    state.value = UserState.Failed("Failed to register")
                }
            }
        })
    }

    fun getUIState() = state

}


sealed class UserState {
    data class Error(var message : String?) : UserState()
    data class Loading(var state : Boolean = false) : UserState()
    data class UserInvalid(var nameIsValid : String? = null, var emailIsValid : String? = null, var passwordIsValid : String? = null) : UserState()
    data class Success(var token : String? = null) : UserState()
    data class Failed(var message : String) : UserState()
    data class Message(var message : String?) : UserState()
    object Reset : UserState()
}