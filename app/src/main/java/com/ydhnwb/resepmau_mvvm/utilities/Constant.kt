package com.ydhnwb.resepmau_mvvm.utilities

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Constant {
    companion object{
        const val API_ENDPOINT = "https://resep-mau.herokuapp.com/"
        const val api_token = "Bearer \$2y\$10\$suZZvr1RdWEeW/ly/NyHIOzv35i0DDkxdOsM0V/E0JIDqw2pBSCVG"

        fun getToken(c : Context) : String {
            val s = c.getSharedPreferences("TOKEN", MODE_PRIVATE)
            val token = s?.getString("TOKEN", "undefined")
            return token!!
        }

        fun setToken(context: Context, token : String){
            val pref = context.getSharedPreferences("TOKEN", MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun clearToken(context: Context){
            val pref = context.getSharedPreferences("TOKEN", MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail(email : String) : Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(password : String) = password.length > 8
    }
}