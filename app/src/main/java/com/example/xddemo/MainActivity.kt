package com.example.xddemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.xddemo.ui.navigation.XDaoNavGraph
import com.example.xddemo.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme { // 根据你的项目名来设置
                XDaoNavGraph(navController = rememberNavController())
            }
        }
    }
}



@Preview
@Composable
fun MyPreview() {
    MyApplicationTheme {

    }
}