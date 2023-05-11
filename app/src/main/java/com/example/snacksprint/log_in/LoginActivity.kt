package com.example.snacksprint.log_in

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.example.snacksprint.MainActivity
import com.example.snacksprint.R
import com.example.snacksprint.SignupActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Find All 5 Edittexts and 1 Button
        val  phonenumber = findViewById<TextInputEditText>(R.id.phone_number)
        val password = findViewById<TextInputEditText>(R.id.password)
        val progress=findViewById<ProgressBar>(R.id.progress)

       val login = findViewById<Button>(R.id.btnlogin)
        val signup=findViewById<Button>(R.id.btnsignup)
        signup.setOnClickListener {
            val i = Intent(applicationContext, SignupActivity::class.java)
            startActivity(i)
            finish() //kills the previous activity
            //find email,password
        }
        login.setOnClickListener {
            progress.visibility= View.VISIBLE //show progress bar

             val client = AsyncHttpClient(true,80,443)
             val body = JSONObject()
             //access the details inserted by user -values from the edittexts
             //put them details inside a body of json object
             body.put("phonenumber",phonenumber.text.toString())
             body.put("password",password.text.toString())
             val con_body = StringEntity(body.toString())
             // https://musau.pythonanywhere.com/signup
             client.post(this,"https://patience64.pythonanywhere.com/login",con_body,
                 "application/json",
                 object : JsonHttpResponseHandler() {
                     //create a function for onsuccess
                     override fun onSuccess(
                         statusCode: Int,
                         headers: Array<out Header>?,
                         response: JSONObject?
                     ) {
                         //check if status code is success (200)
                         if (statusCode == 200){

                             Toast.makeText(applicationContext,"You have signed in successfully",
                                 Toast.LENGTH_LONG).show()
                             val i = Intent(applicationContext,MainActivity::class.java)
                             startActivity(i)
                         } //end of if
                         else{
                             Toast.makeText(applicationContext,"Wrong credentials: Please try again "+ statusCode,
                                 Toast.LENGTH_LONG).show()
                         }
                         //super.onSuccess(statusCode, headers, response)
                     } //end of onsuccess

                     override fun onFailure(
                         statusCode: Int,
                         headers: Array<out Header>?,
                         throwable: Throwable?,
                         errorResponse: JSONObject?
                     ) {
                         progress.visibility = View.GONE
                         Toast.makeText(applicationContext,"Something went wrong from the Application side"
                                 + " " + statusCode,
                             Toast.LENGTH_LONG).show()

                         //super.onFailure(statusCode, headers, throwable, errorResponse)
                     }


                 }
             )

         }



        }
}