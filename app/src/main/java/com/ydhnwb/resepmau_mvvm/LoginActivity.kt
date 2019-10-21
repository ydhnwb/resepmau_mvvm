package com.ydhnwb.resepmau_mvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ydhnwb.resepmau_mvvm.ui.BaseUIState
import com.ydhnwb.resepmau_mvvm.ui.LoginState
import com.ydhnwb.resepmau_mvvm.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getLoginState().observer(this, Observer {
            handleLoginState(it)
        })
        doLogin()
    }

    private fun doLogin(){
        btn_login.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if(userViewModel.validate(email, password)){
                userViewModel.login(email, password, this@LoginActivity)
            }
        }
    }

    private fun handleLoginState(login_state : LoginState){
        when(login_state){
            LoginState.RESET -> {
                setEmailError(null)
                setPasswordError(null)
            }
            LoginState.EMAIL_INVALID -> setEmailError("Email tidak valid")
            LoginState.PASSWORD_INVALID -> setPasswordError("Password tidak valid")
            LoginState.SUCCESS -> {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                }).also { finish() }
            }
            LoginState.LOADING -> isLoading(true)
            LoginState.DONE_LOADING -> isLoading(false)
            LoginState.ERROR -> {
                isLoading(false)
                toast(userViewModel.getMessage())
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
