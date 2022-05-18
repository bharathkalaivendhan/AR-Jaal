package com.bk.arjaal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bk.arjaal.ui.theme.ARJaalTheme
import com.bk.arjaal.ui.theme.BuyActivity

class BillingDetails : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myIntent = intent
        val jewelleryInfo = myIntent.getSerializableExtra("jewelleryInfo") as Jewellery

        setContent {
            ARJaalTheme {
                Surface(color = MaterialTheme.colors.background) {
                    BillingScreen(jewelleryInfo)

                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun BillingScreen(jewelleryInfo: Jewellery) {
    val context = LocalContext.current
    val name = remember {

        mutableStateOf("")
    }
    val address = remember {

        mutableStateOf("")
    }
    val phone = remember {

        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(text = "Billing Details", fontSize = 20.sp)
        Spacer(modifier = Modifier.size(12.dp))
        TextField(
            value = name.value,
            placeholder = { Text("Name") },
            onValueChange = {
                name.value = it
            },
            colors = TextFieldDefaults.textFieldColors(cursorColor = Color.DarkGray),
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.size(12.dp))
        TextField(
            value = address.value,
            placeholder = { Text("Address") },
            onValueChange = {
                address.value = it
            },
            colors = TextFieldDefaults.textFieldColors(cursorColor = Color.DarkGray),
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.size(12.dp))
        TextField(
            value = phone.value,
            placeholder = { Text("Contact Number") },
            onValueChange = {
                phone.value = it
            },
            colors = TextFieldDefaults.textFieldColors(cursorColor = Color.DarkGray),
            keyboardOptions = KeyboardOptions(imeAction = androidx.compose.ui.text.input.ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() })
        )
        Spacer(modifier = Modifier.size(12.dp))
        Button(onClick = {

            if (name.value != "" && address.value != "" && phone.value != "") {
                if (phone.value.length == 10) {
                    val buyIntent = Intent(context, BuyActivity::class.java)
                    buyIntent.putExtra("jewelleryInfo", jewelleryInfo)
                    buyIntent.putExtra("name",name.value)
                    buyIntent.putExtra("phone",phone.value)
                    buyIntent.putExtra("address",address.value)
                    Log.d("Checking", jewelleryInfo.Price.toString())

                    context.startActivity(buyIntent)
                } else {
                    Toast.makeText(context, "Check Phone Number", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Fill All The Details", Toast.LENGTH_SHORT).show()
            }
        })
        {
            Text("Proceed to checkout", color = Color.Black)
        }


    }
}

