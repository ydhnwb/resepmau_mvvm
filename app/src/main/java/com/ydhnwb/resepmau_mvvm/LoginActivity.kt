package com.ydhnwb.resepmau_mvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ydhnwb.resepmau_mvvm.utilities.Constant
import com.ydhnwb.resepmau_mvvm.viewmodels.UserState
import com.ydhnwb.resepmau_mvvm.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getUIState().observer(this, Observer {
            handleLoginState(it)
        })
        doLogin()
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if(userViewModel.validate(email, password)){
                userViewModel.login(email, password)
            }
        }
    }

    private fun handleLoginState(it : UserState){
        when(it){
            is UserState.Error -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Message -> toast(it.message)
            is UserState.LoginFailed -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
            is UserState.Loading -> isLoading(it.state)
            is UserState.UserInvalid -> {
                it.emailIsValid?.let {
                    setEmailError(it)
                }
                it.passwordIsValid?.let {
                    setPasswordError(it)
                }
            }
            is UserState.LoginSuccess -> {
                Constant.setToken(this@LoginActivity, it.token!!)
                startActivity(Intent(this@LoginActivity, MainActivity::class.java)).also {
                    finish()
                }
            }
        }
    }

    private fun setEmailError(err : String?) { in_email.error = err }
    private fun setPasswordError(err : String?) { in_pass.error = err }

    private fun isLoading(state : Boolean){
        if(state){
            btn_login.isEnabled = false
            btn_register.isEnabled = false
            loading.visibility = View.VISIBLE
            loading.isIndeterminate = true
        }else{
            btn_login.isEnabled = true
            btn_register.isEnabled = true
            loading.isIndeterminate = false
            loading.progress = 0
        }
    }

    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
