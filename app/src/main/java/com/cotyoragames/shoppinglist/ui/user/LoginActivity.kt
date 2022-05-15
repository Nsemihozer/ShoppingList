package com.cotyoragames.shoppinglist.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cotyoragames.shoppinglist.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        supportFragmentManager.beginTransaction().replace(R.id.frame,LoginFragment()).commit()
    }
}