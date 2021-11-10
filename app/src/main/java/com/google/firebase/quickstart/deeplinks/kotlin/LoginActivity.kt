package com.google.firebase.quickstart.deeplinks.kotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.quickstart.deeplinks.R
import com.google.firebase.quickstart.deeplinks.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            DataManager.login = true    // 로그인 처리

            val code = intent.getStringExtra("code")
            if (code == null) { // 일반적으로 로그인 화면으로 이동한 경우
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {    // 초대 다이나믹 링크로 들어와 로그인 화면으로 이동한 경우
                val intent = Intent(this, FriendsActivity::class.java)
                intent.putExtra("code", code)   // friend 화면에서 바로 추가할 수 있도록 코드를 넘겨준다
                startActivity(intent)
            }
            finish()
        }
    }
}