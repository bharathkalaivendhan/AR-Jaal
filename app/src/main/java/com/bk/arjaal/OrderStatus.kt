package com.bk.arjaal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bk.arjaal.ui.theme.ARJaalTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private var TIME_OUT:Long = 3000
private lateinit var auth : FirebaseAuth

class OrderStatus : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val status=intent.getStringExtra("Status")
        val name=intent.getStringExtra("name")
        val phone=intent.getStringExtra("phone")
        val address=intent.getStringExtra("address")

        val jewelleryInfo = intent.getSerializableExtra("jewelleryInfo") as Jewellery

        loadSplashScreen()
        setContent {
            ARJaalTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                   OrderStatusScreen(status,jewelleryInfo,name,address,phone)
                }
            }
        }
    }

    private fun loadSplashScreen() {

        Handler().postDelayed({
                val mainintent = Intent(this, MainActivity::class.java)
                this.startActivity(mainintent)
        }, TIME_OUT)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderStatusScreen(
    status: String?,
    jewelleryInfo: Jewellery,
    name: String?,
    address: String?,
    phone: String?
) {

    auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val rootRef = FirebaseDatabase.getInstance().reference

    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
    val dateTime = current.format(formatter)

    if(status == "success")
    {
        val orderRef = rootRef.child("users").child(user?.uid.toString()).child("orders").child(dateTime)
        orderRef.setValue(jewelleryInfo)
        val nameRef = orderRef.child("name")
        val phoneRef = orderRef.child("phone")
        val addressRef = orderRef.child("address")
        nameRef.setValue(name)
        phoneRef.setValue(phone)
        addressRef.setValue(address)

    }

      var text ="Success"

       val  image :Painter = if(status == "success") {
             text = "Success!!! Product Will Be Delivered Soon"
           painterResource(id = R.drawable.success)
        }
        else{
            text="Failed,try again"
           painterResource(id = R.drawable.faied)
        }
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
        Image(painter = image,contentDescription = "Status of the order")
            Spacer(modifier = Modifier.size(20.dp))
            Text(text = text,modifier = Modifier.padding(12.dp))

    }

}

