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
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getUIState().observer(this, Observer {
            handleState(it)
        })
        doRegister()
    }

    private fun doRegister(){
        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val passwd = et_password.text.toString().trim()
            if(userViewModel.validate(name, email, passwd)){
                userViewModel.register(name, email, passwd)
            }
        }
    }

    private fun handleState(it : UserState){
        when(it){
            is UserState.Error -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Message -> toast(it.message)
            is UserState.Failed -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Reset -> {
                setEmailError(null)
                setPasswordError(null)
            }
            is UserState.Loading -> isLoading(it.state)
            is UserState.UserInvalid -> {
                it.nameIsValid?.let{
                    setNameError(it)
                }

                it.emailIsValid?.let {
                    setEmailError(it)
                }
                it.passwordIsValid?.let {
                    setPasswordError(it)
                }
            }
            is UserState.Success -> {
                Constant.setToken(this@RegisterActivity, "Bearer ${it.token!!}")
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                }).also {
                    finish()
                }
            }
        }
    }

    private fun setEmailError(err : String?) { in_email.error = err }

    private fun setPasswordError(err : String?) { in_password.error = err }

    private fun setNameError(err : String?) { in_name.error = err }

    private fun isLoading(state : Boolean){
        if(state){
            btn_register.isEnabled = false
            loading.visibility = View.VISIBLE
            loading.isIndeterminate = true
        }else{
            btn_register.isEnabled = true
            loading.isIndeterminate = false
            loading.progress = 0
        }
    }

    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
