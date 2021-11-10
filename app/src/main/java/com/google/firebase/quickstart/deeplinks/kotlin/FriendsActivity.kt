package com.google.firebase.quickstart.deeplinks.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.quickstart.deeplinks.databinding.ActivityFriendsBinding

class FriendsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 넘겨 받은 코드가 있는 경우 화면에 표시해주고
        // 다이얼로그를 띄워 친구 추가할 건지 묻는다
        intent.getStringExtra("code")?.let {
            binding.linkViewReceive2.text = it

            AlertDialog.Builder(this)
                .setMessage("code $it 친구를 추가하시겠습니까?")
                .setPositiveButton("추가") { dialog, which ->
                    Toast.makeText(this, "친구를 추가했습니다!", Toast.LENGTH_SHORT).show()
                }
                .create()
                .show()
        }

    }

    companion object {

        private const val TAG = "FriendsActivity"
        //        private const val DEEP_LINK_URL = "https://kotlin.example.com/deeplinks"
        private const val DEEP_LINK_URL = "https://eunsub.com/invite"
    }
}