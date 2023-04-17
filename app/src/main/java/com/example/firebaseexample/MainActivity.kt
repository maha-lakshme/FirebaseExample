package com.example.firebaseexample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    lateinit var etEmail: EditText
    lateinit var etpwd:EditText
    lateinit var btnLogin:Button
    lateinit var btnSignUp:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etEmail=findViewById(R.id.etEmailAddress)
        etpwd=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogin)
        btnSignUp=findViewById(R.id.btnSignup)

        //firebase database
        connectDatabase()

        //intilazing firebase auth
        auth=Firebase.auth

        btnLogin.setOnClickListener {
            val email= etEmail.text.toString()
            val pwd =etpwd.text.toString()
           auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(this){
               if(it.isSuccessful){

                   Log.d("success---->",  it.result.user.toString())
                   Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
               }
               else{
                   it.exception.toString()
                   Toast.makeText(this, "Log In failed:  "+ it.exception.toString(), Toast.LENGTH_SHORT).show()
               }
           }
        }

        btnSignUp.setOnClickListener {
            val email= etEmail.text.toString()
            val pwd =etpwd.text.toString()
        auth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                Log.d("success---->", user.toString())
                Toast.makeText(this, "successfully signed in" + task.result.toString(), Toast.LENGTH_LONG)
                    .show()

            } else {
                Log.d("faliure---->", task.exception?.message.toString()
                )
                Toast.makeText(
                    this,
                    "sign in failed",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        }

    }

    private fun connectDatabase() {
        //sample  FIREBASE connection
        Log.d("Connecting to DB--->","connected")
       // val database = Firebase.database
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("message")
        ref.setValue("hello")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Toast.makeText(this@MainActivity, "data changes", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error to DB--->","error in db")

                Toast.makeText(this@MainActivity, "data error", Toast.LENGTH_SHORT).show()

            }

        }

        )
    }
}