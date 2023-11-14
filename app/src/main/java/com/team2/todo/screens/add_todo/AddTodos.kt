package com.team2.todo.screens.add_todo

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.team2.todo.R
import com.team2.todo.screens.add_todo.ui_components.AddEditAppBar
import com.team2.todo.screens.add_todo.ui_components.DatePickerComponent
import com.team2.todo.screens.add_todo.ui_components.DropDownMenuComponent
import com.team2.todo.screens.add_todo.ui_components.PickImageFromGallery
import com.team2.todo.screens.add_todo.ui_components.TimePickerComponent
import com.team2.todo.ui.theme.PrimaryColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodos() {
    val OutLineTextColor = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = PrimaryColor,
        focusedLabelColor = Color.Black,
        disabledLabelColor = PrimaryColor
    )
    val OutLinedTextModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 5.dp)

    var ctx = LocalContext.current.applicationContext
    var enteredTitle by remember {
        mutableStateOf("")
    }
    var enteredDescription by remember {
        mutableStateOf("")
    }

    var enteredPrice by remember {
        mutableStateOf("")
    }

    var isTitleEmpty by remember { mutableStateOf(false) }
    var isDescriptionEmpty by remember { mutableStateOf(false) }

    var (calendarState, dateselected) = DatePickerComponent()
    var (timeState, timeselected) = TimePickerComponent()

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(it)
        ) {
            AddEditAppBar()
            Column(
                modifier = Modifier
                    .weight(weight = 1.0F)
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                //Title
                OutlinedTextField(
                    modifier = OutLinedTextModifier,
                    value = enteredTitle,
                    onValueChange = {
                        enteredTitle = it
                        isTitleEmpty = it.isEmpty()
                    },
                    label = { Text(text = "Title") },
                    colors = OutLineTextColor,
                    isError = isTitleEmpty,
                )
                // Description
                OutlinedTextField(
                    value = enteredDescription,
                    modifier = OutLinedTextModifier
                        .height(200.dp),
                    onValueChange = {
                        enteredDescription = it
                        isDescriptionEmpty = it.isEmpty()
                    },
                    label = { Text(text = "Description") },
                    colors = OutLineTextColor,
                    isError = isDescriptionEmpty,
                )

                OutlinedTextField(
                    value = enteredPrice, onValueChange = { newText -> enteredPrice = newText },
                    label = { Text(text = "Price: ") },
                    placeholder = { Text(text = "Enter price: ") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutLineTextColor,
                    modifier = OutLinedTextModifier,
                )

                PickImageFromGallery(activity = ComponentActivity())
            }
            val ctx = LocalContext.current
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
                elevation = ButtonDefaults.buttonElevation(6.dp),
                onClick = {
                    if (enteredTitle.isEmpty()) {
                        Toast.makeText(ctx, "Please fill the title", Toast.LENGTH_SHORT).show()
                        isTitleEmpty = true
                    } else if (enteredDescription.isEmpty()) {
                        Toast.makeText(ctx, "Please fill the description", Toast.LENGTH_SHORT)
                            .show()
                        isDescriptionEmpty = true
                    } else if (dateselected.value == "" || timeselected.value == "") {
                        Toast.makeText(ctx, "Please select the due date", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(ctx, "Entries are added!!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.Black),
                shape = MaterialTheme.shapes.small.copy(all = CornerSize(10.dp))
            ) {
                Text(text = "ADD", color = Color.White)
            }


//
//            PickImageFromGallery(activity = ComponentActivity())
//
//
//            OutlinedTextField(
//                value = enteredPrice, onValueChange = { newText -> enteredPrice = newText },
//                label = { Text(text = "Price: ") },
//                placeholder = { Text(text = "Enter price: ") },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//                ),
//                colors = TextFieldDefaults.colors(
//                    //                        textColor = Color.Black
//                )
//
//            )
//
//            DropDownMenuComponent()
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(text = "Due Date")
//                Spacer(modifier = Modifier.padding(4.dp))
//                Button(
//                    modifier = Modifier.padding(start = 6.dp),
//                    colors = ButtonDefaults.buttonColors(Color.Black),
//                    onClick = { calendarState.show() }) {
//                    Icon(imageVector = Icons.Filled.DateRange, contentDescription = "DateTime")
//
//                }
//                Spacer(modifier = Modifier.padding(6.dp))
//                Button(
//                    colors = ButtonDefaults.buttonColors(Color.Black),
//                    onClick = { timeState.show() }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_outline_alarm_24),
//                        contentDescription = "TimeClock"
//                    )
//
//                }
//
//            }
//            Row {
//                Text(text = "Due on  ${dateselected.value}", fontWeight = FontWeight.Bold)
//                Text(text = " at ${timeselected.value}", fontWeight = FontWeight.Bold)
//            }


            // fetching local context

        }
    }
}








