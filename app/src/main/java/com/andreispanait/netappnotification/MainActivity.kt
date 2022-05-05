package com.andreispanait.netappnotification

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.andreispanait.netappnotification.databinding.ActivityMainBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_main)

        val labelTitle = binding.labelTitle
        labelTitle.text = (intent.extras?.get("title") as? String) ?: "NO TITLE"

        val labelBody = binding.labelBody
        labelBody.text = (intent.extras?.get("body") as? String) ?: "NO BODY"

        binding.buttonRetrieveToken.setOnClickListener {
            if (checkGooglePlayServices()) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    val token = task.result
                    val msg = getString(R.string.token_prefisso, token)

                    binding.labelToken.text = msg
                })
            } else {
                Log.w(TAG, "Manca google play services")
            }
        }
        setContentView(binding.root)
    }

    fun checkGooglePlayServices(): Boolean {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        return status == ConnectionResult.SUCCESS
    }


}