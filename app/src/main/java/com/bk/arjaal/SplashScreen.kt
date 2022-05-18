package com.bk.arjaal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp


@Composable
fun SplashScreen(jewelleriesList: MutableList<Jewellery>)
{


Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {


    Text(modifier = Modifier,text="AR Jaal", style = TextStyle(fontSize =30.sp,fontFamily = FontFamily.SansSerif))

}


}
