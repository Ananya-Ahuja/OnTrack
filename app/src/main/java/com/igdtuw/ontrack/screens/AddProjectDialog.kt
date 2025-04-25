package com.igdtuw.ontrack.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.igdtuw.ontrack.Milestone
import com.igdtuw.ontrack.MilestoneTemplate
import com.igdtuw.ontrack.MilestoneTemplateItem

@Composable
fun AddProjectDialog(
    onDismiss: () -> Unit,
    onSave: (String, List<Milestone>) -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    val initialMilestones = remember { mutableStateListOf(MilestoneTemplate()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Project") },
        text = {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = projectName,
                    onValueChange = { projectName = it },
                    label = { Text("Project Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    "Milestones",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                initialMilestones.forEachIndexed { index, template ->
                    MilestoneTemplateItem(
                        template = template,
                        onRemove = { if (initialMilestones.size > 1) initialMilestones.removeAt(index) },
                        onUpdate = { initialMilestones[index] = it }
                    )
                }

                Button(
                    onClick = { initialMilestones.add(MilestoneTemplate()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Add Milestone")
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (projectName.isNotBlank()) {
                        val milestones = initialMilestones.map {
                            Milestone(
                                projectId = 0,
                                title = it.name,
                                dueDate = it.date,
                                isCompleted = false
                            )
                        }
                        onSave(projectName, milestones)
                        onDismiss()
                    }
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
