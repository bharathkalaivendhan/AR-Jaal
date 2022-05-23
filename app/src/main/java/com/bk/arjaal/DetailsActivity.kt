package com.bk.arjaal

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.bk.arjaal.ui.theme.ARJaalTheme
import com.bk.arjaal.ui.theme.ui.theme.LPrimary
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

    val buttoncolor = remember {
        mutableStateOf(Color.Black)
    }
    val buttonsize = remember {
        mutableStateOf(20.dp)
    }

    if (favourites.contains(jewelleryInfo)) {
            buttoncolor.value = LPrimary
            buttonsize.value = 30.dp
        }
    else {
        buttoncolor.value = Color.Black
        buttonsize.value = 20.dp
    }

    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContent(jewelleryInfo,viewModel)
        },
        scaffoldState = bottomSheetScaffoldState,
        //sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colors.primary,
        sheetPeekHeight = 200.dp,
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if(!favourites.contains(jewelleryInfo)) {
                    viewModel.onAddFavouriteList(jewelleryInfo)
                    buttoncolor.value = LPrimary
                    buttonsize.value = 30.dp
                }
                else {
                    viewModel.onDeleteFavouriteList(jewellery = jewelleryInfo)
                    buttoncolor.value = Color.Black
                    buttonsize.value = 20.dp
                }},
                backgroundColor = Ljust1
                ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "favourite icon",
                    tint = buttoncolor.value,
                    modifier = Modifier.size(buttonsize.value)
                )
            }
        }
        // scrimColor = Color.Red,  // Color for the fade background when you open/close the bottom sheet
    ) {
        val painter = rememberImagePainter(data = jewelleryInfo.ImageURL)
        Log.d("CHECKING", jewelleryInfo.ImageURL)

        Column(modifier = Modifier.padding(24.dp)) {

            Image(
                painter = painter,
                contentDescription = "jewelly image",
                //  contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(bottom = 10.dp, start = 15.dp, end = 15.dp, top = 15.dp)
                    .fillMaxWidth()
                    .fillMaxHeight(0.60f)
                    .clip(MaterialTheme.shapes.large)
            )
            Text(text = "Product Name: "+jewelleryInfo.Name.uppercase(), fontSize = 28.sp,
                modifier = Modifier.padding(start =15.dp)
                ,
                fontWeight = FontWeight.Medium)
            Text(text = "Rs." + jewelleryInfo.Price.toString(), fontSize = 28.sp
                , fontStyle = FontStyle.Normal, fontWeight = FontWeight.ExtraBold
                ,
                modifier = Modifier.padding(start= 15.dp))

        }
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
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp))
        {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    colors = buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant) ,
                    onClick = {
                        val tryIntent = Intent(context,TryOnActivityJ::class.java)
                        tryIntent.putExtra( "jewelleryInfo", jewelleryInfo)
                        context.startActivity(tryIntent)



                    },
                    modifier = Modifier.padding(10.dp)) {
                    Text(text = "Try On",color = black)
                }

                Spacer(modifier = Modifier.size(80.dp))

                Button( colors = buttonColors(backgroundColor = MaterialTheme.colors.primaryVariant),
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
            Text(text = jewelleryInfo.Discription,
                fontSize = 16.sp,fontFamily = FontFamily.Serif)
        }
    }

}
