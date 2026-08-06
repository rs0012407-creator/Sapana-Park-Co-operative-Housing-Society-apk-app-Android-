package com.example.ui.screens.communication

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CommitteeMember
import com.example.viewmodel.SocietyViewModel

@Composable
fun CommunicationScreen(
    viewModel: SocietyViewModel
) {
    val committee = viewModel.committeeMembers
    val suggestions by viewModel.suggestions.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(0) } // 0: Committee Directory, 1: Suggestions & Feedback, 2: Office Info

    // Suggestion Form State
    var suggTitle by remember { mutableStateOf("") }
    var suggCategory by remember { mutableStateOf("Infrastructure") }
    var suggDesc by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("communication_screen")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F2027))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Communication & Management",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(
                    text = "Managing Committee, Office Contacts & Suggestion Box",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Committee") },
                icon = { Icon(Icons.Default.Group, null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Suggestions") },
                icon = { Icon(Icons.Default.Feedback, null) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Office Hours") },
                icon = { Icon(Icons.Default.Schedule, null) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        // WhatsApp Community Banner
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFF25D366)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = Icons.AutoMirrored.Filled.Chat, contentDescription = null, tint = Color.White)
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = "Sapana Park WhatsApp Broadcast", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFF1B5E20))
                                        Text(text = "Get real-time gate alerts & notices", fontSize = 11.sp, color = Color(0xFF2E7D32))
                                    }
                                }
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.whatsapp.com/sapanaparkchs"))
                                        context.startActivity(intent)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Join Group", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    items(committee) { member ->
                        CommitteeMemberCard(member = member)
                    }
                }
            }
            1 -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Submit Idea or Feedback to Committee", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = suggTitle,
                                    onValueChange = { suggTitle = it },
                                    label = { Text("Suggestion Title") },
                                    placeholder = { Text("e.g. EV Charging setup in basement") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("sugg_title_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                OutlinedTextField(
                                    value = suggDesc,
                                    onValueChange = { suggDesc = it },
                                    label = { Text("Detailed Description & Benefit") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(90.dp)
                                        .testTag("sugg_desc_input"),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (suggTitle.isNotBlank()) {
                                            viewModel.submitSuggestion(suggTitle, suggCategory, suggDesc)
                                            suggTitle = ""
                                            suggDesc = ""
                                        } else {
                                            viewModel.showMessage("Please enter suggestion title.")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .testTag("submit_sugg_btn"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027))
                                ) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Submit to Committee", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }

                    item {
                        Text(text = "Recent Community Suggestions", fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(top = 8.dp))
                    }

                    items(suggestions) { sugg ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = sugg.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(text = sugg.status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = sugg.description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "Submitted on ${sugg.date}", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
            2 -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(text = "Sapana Park CHS Office & Manager", fontWeight = FontWeight.Bold, fontSize = 17.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(12.dp))

                            OfficeDetailRow(Icons.Default.LocationOn, "Location:", "Ground Floor, Clubhouse Block, Sapana Park CHS, Katraj, Pune - 411046")
                            OfficeDetailRow(Icons.Default.Schedule, "Office Hours:", "Monday to Saturday: 10:00 AM - 01:00 PM & 05:00 PM - 08:00 PM\nSunday: 10:00 AM - 12:30 PM (AGM / Maintenance Desk)")
                            OfficeDetailRow(Icons.Default.Call, "Office Desk Phone:", "+91 020 27400999 / +91 98220 11223")
                            OfficeDetailRow(Icons.Default.Mail, "Official Email:", "manager@sapanapark.org")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CommitteeMemberCard(member: CommitteeMember) {
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = member.designation,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "Flat ${member.wingFlat} • ${member.phone}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Row {
                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${member.phone}"))
                    context.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun OfficeDetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, detail: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = detail, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
