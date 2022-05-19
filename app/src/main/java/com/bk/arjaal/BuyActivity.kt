package com.bk.arjaal.ui.theme

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.bk.arjaal.Jewellery
import com.bk.arjaal.OrderStatus
import com.bk.arjaal.R
import com.bk.arjaal.ui.theme.ui.theme.ARJaalTheme
val gPayPackage= "com.google.android.apps.nbu.paisa.user"
val GPAY_REQ_CODE = 100
lateinit var status : String
class BuyActivity : ComponentActivity() {

    var jewelleryInfo = Jewellery()
    var name =""
    var phone =""
    var address = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myIntent = intent
        jewelleryInfo = myIntent.getSerializableExtra("jewelleryInfo") as Jewellery
        name = myIntent.getStringExtra("name") as String
        phone = myIntent.getStringExtra("phone") as String
        address = myIntent.getStringExtra("address") as String
        setContent {
            ARJaalTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.surface) {
                    mainLayout(activity = this,jewelleryInfo.Price,jewelleryInfo,phone,address,name)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data !=null){

            status = data.getStringExtra("Status").toString().toLowerCase()
        }

        if((RESULT_OK == resultCode) && status.equals("success")){

            Toast.makeText(applicationContext, "Transaction Successful", Toast.LENGTH_SHORT).show()

        }
        else{
            Toast.makeText(applicationContext, "Transaction Failed", Toast.LENGTH_SHORT).show()
        }
        val statusIntent = Intent(applicationContext, OrderStatus::class.java)
        statusIntent.putExtra("Status", status)
        statusIntent.putExtra("name", name)
        statusIntent.putExtra("phone", phone)
        statusIntent.putExtra("address", address)
        statusIntent.putExtra("jewelleryInfo",jewelleryInfo)
        applicationContext.startActivity(statusIntent)
    }


}
@Composable
fun mainLayout(
    activity: Activity,
    amount: Long,
    jewelleryInfo: Jewellery,
    phone: String,
    address: String,
    name: String
    ,
) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize())
    {
        val painter = rememberImagePainter(data = jewelleryInfo.ImageURL)
        Log.d("CHECKING",jewelleryInfo.ImageURL)
        Text(
            text = "Checkout Process", modifier = Modifier
                .requiredWidth(400.dp)
                .requiredHeight(50.dp)
                .padding(start= 16.dp,top=12.dp,bottom=10.dp),
            fontSize = 25.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Black
        )
        Image(
            painter = painter,
            contentDescription = "jewelly image",

            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .clip(MaterialTheme.shapes.large)
                .shadow(4.dp)
        )

        Text(
            text = "Total order price Rs.$amount",
            modifier = Modifier
                .padding(12.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold
            //fontFamily = FontFamily.Cursive
        )
        Text(
            text = "Hi $name !!" +
                    "\nThanks for choosing us",
            modifier = Modifier
                .padding(12.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
            //fontFamily = FontFamily.Cursive
        )
        Text(
            text = "The order will be deliverd at \n${address}" +
                    "\nOnce you complete the payment process, the order will be confirmed." ,
            modifier = Modifier
                .padding(10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black
            ,fontFamily = FontFamily.SansSerif
        )

        Button(onClick = {
            payWithGPay(amount = amount.toString().toInt(), activity = activity,phone,name,address)
        },
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Logo" , Modifier.size(35.dp))

                Text(
                    text = "Proceed With Google Pay",
                    modifier = Modifier
                        //.requiredWidth()
                        .padding(12.dp),
                    color = Color.Yellow                )
            }
        }
    }
}

fun payWithGPay(amount: Int, activity: Activity, phone: String, name: String, address: String) {
    val upiId = "kjbharath26@okaxis"
    val name ="Bharath kalaivendhan"
    val transactionNote = "D"
    val uri = Uri.Builder()
        .scheme("upi")
        .authority("pay")
        .appendQueryParameter("pa",upiId)
        .appendQueryParameter("pn",name)
        .appendQueryParameter("tn",transactionNote)
        .appendQueryParameter("am", amount.toString())
        .appendQueryParameter("cu","INR")
        .build()



    val payIntent = Intent(Intent.ACTION_VIEW)
    payIntent.setData(uri)
        .setPackage(gPayPackage)
    activity.startActivityForResult(payIntent, GPAY_REQ_CODE)

}

