package com.bk.arjaal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.bk.arjaal.ui.theme.ARJaalTheme
import com.bk.arjaal.ui.theme.ui.theme.Ljust1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DetailsActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {

        val myIntent = intent
        val jewelleryInfo = myIntent.getSerializableExtra("jewelleryInfo") as Jewellery

        val viewModel = JewelleryViewModel()

        /*
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val rootRef = FirebaseDatabase.getInstance().reference

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        val dateTime = current.format(formatter)

            val orderRef = rootRef.child("users").child(user?.uid.toString()).child("orders").child(dateTime)
            orderRef.setValue(jewelleryInfo)

         */


        super.onCreate(savedInstanceState)
        setContent {
            ARJaalTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = Ljust1) {
                    MainView(jewelleryInfo = jewelleryInfo,viewModel)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MainView(jewelleryInfo: Jewellery, viewModel: JewelleryViewModel)
{

    BottomSheetScaffoldVeiw(jewelleryInfo,viewModel)

}

@ExperimentalMaterialApi
@Composable
fun BottomSheetScaffoldVeiw(jewelleryInfo: Jewellery, viewModel: JewelleryViewModel) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    //val scope = rememberCoroutineScope()
    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContent(jewelleryInfo,viewModel)
        },
        scaffoldState = bottomSheetScaffoldState,
        //sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colors.secondary,
        sheetPeekHeight = 250.dp,
        modifier = Modifier.fillMaxWidth()
        // scrimColor = Color.Red,  // Color for the fade background when you open/close the bottom sheet
    ) {
        val painter = rememberImagePainter(data = jewelleryInfo.ImageURL)
        Log.d("CHECKING",jewelleryInfo.ImageURL)

        Image(
            painter = painter,
            contentDescription = "jewelly image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(bottom = 265.dp, start = 15.dp, end = 15.dp, top = 15.dp)
                .fillMaxSize()
                .clip(MaterialTheme.shapes.large)
        )
    }
}


@ExperimentalMaterialApi
@Composable
fun BottomSheetContent(jewelleryInfo: Jewellery, viewModel: JewelleryViewModel) {
    val black = Color.Black
val context = LocalContext.current
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .fillMaxSize(1f)
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally,modifier = Modifier.fillMaxSize())
        {
            Text(text = jewelleryInfo.Name.uppercase(),fontSize = 32.sp)
            Text(text = "Rs."+jewelleryInfo.Price.toString(),fontSize = 28.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val tryIntent = Intent(context,TryOnActivityJ::class.java)
                tryIntent.putExtra( "jewelleryInfo", jewelleryInfo)
                context.startActivity(tryIntent)


            },
                modifier = Modifier.padding(10.dp)) {
                Text(text = "Try On",color = black)
            }

                Spacer(modifier = Modifier.size(80.dp))

                Button(
                    onClick = {

                        val billIntent = Intent(context, BillingDetails::class.java)
                        billIntent.putExtra("jewelleryInfo", jewelleryInfo)
                        Log.d("Checking", jewelleryInfo.Price.toString())

                        context.startActivity(billIntent)
                    },
                    modifier = Modifier.padding(10.dp)
                ) {
                    Text(text = "Buy",color = black)
                }
            }
            Text(text = jewelleryInfo.Discription, fontSize = 24.sp,fontFamily = FontFamily.Cursive)
        }
    }

}
