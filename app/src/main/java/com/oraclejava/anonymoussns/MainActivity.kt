package com.oraclejava.anonymoussns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.oraclejava.anonymoussns.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    val TAG = "MainActivity"

    val ref = FirebaseDatabase.getInstance().getReference("name")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ref.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val message = p0.value.toString()
                Log.d(TAG,message)
                //binding.hello.text = message
            }

            override fun onCancelled(p0: DatabaseError) {
                p0.toException().printStackTrace()
            }
        })
    }
}