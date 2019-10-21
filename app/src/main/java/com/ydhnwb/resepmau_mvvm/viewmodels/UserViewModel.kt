package com.ydhnwb.resepmau_mvvm.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ydhnwb.resepmau_mvvm.models.User
import com.ydhnwb.resepmau_mvvm.ui.BaseUIState
import com.ydhnwb.resepmau_mvvm.ui.LoginState
import com.ydhnwb.resepmau_mvvm.utilities.Constant
import com.ydhnwb.resepmau_mvvm.utilities.SingleLiveEvent
import com.ydhnwb.resepmau_mvvm.webservices.ApiClient
import com.ydhnwb.resepmau_mvvm.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    private var loginState = SingleLiveEvent<LoginState>()
    private var message : String? = null
    private var api = ApiClient.instance()

    fun login(email : String, password : String, context: Context){
        loginState.value = LoginState.LOADING
        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>>{
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                println(t.message.toString())
                message = t.message.toString()
                loginState.value = LoginState.ERROR
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if(response.isSuccessful){
                    val resp = response.body() as WrappedResponse<User>
                    if(resp.status.equals("1")){
                        Constant.setToken(context, "Bearer "+resp.data!!.api_token!!)
                    }
                    loginState.value = LoginState.DONE_LOADING
                    loginState.value = LoginState.SUCCESS
                }else{
                    message = "Cannot login [${response.code()}]"
                    loginState.value = LoginState.ERROR
                }
            }
        })
    }

    fun validate(email: String, password: String) : Boolean{
        loginState.value = LoginState.RESET
        if(email.isEmpty() || !Constant.isValidEmail(email)){
            loginState.value = LoginState.EMAIL_INVALID
            return false
        }

        if(password.isEmpty() || !Constant.isValidPassword(password)){
            loginState.value = LoginState.PASSWORD_INVALID
            return false
        }
        return true
    }

    fun getLoginState() = loginState
    fun getMessage() = message
}