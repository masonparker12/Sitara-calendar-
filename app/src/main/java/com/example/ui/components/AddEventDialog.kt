package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AvailableEventColors
import com.example.ui.theme.SitaraPurple
import com.example.util.CalendarUtils
import java.time.LocalDate

@Composable
fun AddEventDialog(
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (title: String, time: String, description: String, colorHex: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("09:00 AM") }
    var description by remember { mutableStateOf("") }
    var selectedColorHex by remember { mutableStateOf(AvailableEventColors[0].hexString) }

    val quickTimes = listOf("09:00 AM", "12:00 PM", "03:00 PM", "07:00 PM", "All Day")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "New Event",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = CalendarUtils.formatFullDate(selectedDate),
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SitaraPurple,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event Title") },
                    placeholder = { Text("e.g. Design Sync, Doctor Appointment") },
                    singleLine = true,
                    modifier = Modifier
                        .testTag("event_title_input")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Time Input & Quick chips
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time") },
                        placeholder = { Text("e.g. 10:30 AM or All Day") },
                        singleLine = true,
                        modifier = Modifier
                            .testTag("event_time_input")
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Quick time suggestions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickTimes.take(4).forEach { qTime ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (time == qTime) SitaraPurple.copy(alpha = 0.2f)
                                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                    )
                                    .clickable { time = qTime }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = qTime,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (time == qTime) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (time == qTime) SitaraPurple else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    placeholder = { Text("Add notes, location, or agenda") },
                    maxLines = 3,
                    modifier = Modifier
                        .testTag("event_desc_input")
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                // Color Selection
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Event Color Tag",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AvailableEventColors.forEach { colorItem ->
                            val isSelected = selectedColorHex == colorItem.hexString
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(colorItem.color)
                                    .clickable { selectedColorHex = colorItem.hexString }
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(2.5.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                        } else {
                                            Modifier
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onConfirm(title, time, description, selectedColorHex)
                            }
                        },
                        enabled = title.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("save_event_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SitaraPurple
                        )
                    ) {
                        Text("Save Event", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
