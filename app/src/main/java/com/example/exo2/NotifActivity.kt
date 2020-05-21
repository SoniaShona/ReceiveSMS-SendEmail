package com.example.exo2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_notif.*

class NotifActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notif)
        val i = intent
        try {
            val data = i.getStringExtra("data")
            txtReceiver.text = "Reçu : " + data
        } catch (ex: Exception) {
            txtReceiver.text = "Erreur"
        }
    }
}
