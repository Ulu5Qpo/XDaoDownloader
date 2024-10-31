package com.example.xddemo.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.xddemo.R
import com.example.xddemo.data.Message
import com.example.xddemo.ui.theme.MyApplicationTheme


@Composable
fun MessageCard(msg: Message) {

    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 3.dp,
        modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
            }
    ) {
        Row(
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Spacer(Modifier.padding(horizontal = 8.dp))
            Column {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(Modifier.padding(vertical = 4.dp))
                Text(
                    text = msg.body,
                    style = MaterialTheme.typography.body2,
                    maxLines = 10,
                    modifier = Modifier.animateContentSize()
                )
            }
        }
    }
}

