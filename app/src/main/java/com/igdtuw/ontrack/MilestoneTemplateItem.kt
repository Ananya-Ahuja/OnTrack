package com.igdtuw.ontrack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.igdtuw.ontrack.MilestoneTemplate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilestoneTemplateItem(
    template: MilestoneTemplate,
    onRemove: () -> Unit,
    onUpdate: (MilestoneTemplate) -> Unit
) {
    var name by remember { mutableStateOf(template.name) }
    var date by remember { mutableStateOf(template.date) }

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                onUpdate(template.copy(name = name))
            },
            label = { Text("Milestone Name") },
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(
            onClick = { /* Date picker implementation */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(date.toString())
        }

        if (name.isNotEmpty()) {
            TextButton(
                onClick = onRemove,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remove", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
