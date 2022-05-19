package com.bk.arjaal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun SplashScreen(jewelleriesList: MutableList<Jewellery>)
{
    // Remember a SystemUiController
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight

    SideEffect {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )

        // setStatusBarsColor() and setNavigationBarColor() also exist
    }


    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter = painterResource(id = R.drawable.iconjewel),
            contentDescription = "jewelly App Icon",
            modifier = Modifier
                .requiredSize(375.dp)
                .padding(top=250.dp)

            //.clip(RoundedCornerShape(corner = CornerSize(10.dp)))
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Text(modifier = Modifier,text="AR Jaal",
            style = TextStyle(fontSize =36.sp,
                fontFamily = FontFamily.SansSerif)
            , fontWeight = FontWeight.Black)

    }


}
