package com.example.ui.screens.meetings

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.data.model.SocietyMeetingEntity
import com.example.viewmodel.SocietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingsScreen(
    viewModel: SocietyViewModel
) {
    val meetings by viewModel.meetings.collectAsState()
    val userSession by viewModel.userSession.collectAsState()
    val context = LocalContext.current

    var selectedCategoryFilter by remember { mutableStateOf("All") }
    var showScheduleSheet by remember { mutableStateOf(false) }

    // Schedule Meeting Form State
    var meetTitle by remember { mutableStateOf("") }
    var meetPurpose by remember { mutableStateOf("") }
    var meetCategory by remember { mutableStateOf("General Body") }
    var meetDate by remember { mutableStateOf("") }
    var meetTime by remember { mutableStateOf("") }
    var meetVenue by remember { mutableStateOf("") }
    var meetOrganizer by remember { mutableStateOf(userSession.name) }
    var meetAgenda by remember { mutableStateOf("") }

    val categories = listOf("All", "General Body", "Water & Utility", "Security & Maintenance", "Festival & Funds")

    val filteredMeetings = if (selectedCategoryFilter == "All") {
        meetings
    } else {
        meetings.filter { it.category.contains(selectedCategoryFilter, ignoreCase = true) }
    }

    if (showScheduleSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showScheduleSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Schedule Colony Meeting",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "सोसाइटी में होने वाली मीटिंग का विवरण एवं उद्देश्य भरें",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { showScheduleSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = meetTitle,
                    onValueChange = { meetTitle = it },
                    label = { Text("Meeting Title (मीटिंग का शीर्षक)*") },
                    leadingIcon = { Icon(Icons.Default.Groups, null) },
                    placeholder = { Text("जैसे: इमरजेंसी वाटर सप्लाई एवं बोरवेल मीटिंग") },
                    modifier = Modifier.fillMaxWidth().testTag("meet_title_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = meetPurpose,
                    onValueChange = { meetPurpose = it },
                    label = { Text("Meeting Reason / Objective (मीटिंग का मुख्य उद्देश्य)*") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.HelpOutline, null) },
                    placeholder = { Text("जैसे: नए समरसेबल पंप मोटर खरीदने एवं सीसीटीवी बजट स्वीकृति हेतु") },
                    modifier = Modifier.fillMaxWidth().testTag("meet_purpose_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = meetDate,
                        onValueChange = { meetDate = it },
                        label = { Text("Date (दिनांक)") },
                        leadingIcon = { Icon(Icons.Default.CalendarMonth, null) },
                        placeholder = { Text("10 Aug 2026") },
                        modifier = Modifier.weight(1f).testTag("meet_date_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = meetTime,
                        onValueChange = { meetTime = it },
                        label = { Text("Time (समय)") },
                        leadingIcon = { Icon(Icons.Default.Schedule, null) },
                        placeholder = { Text("10:00 AM") },
                        modifier = Modifier.weight(1f).testTag("meet_time_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = meetVenue,
                    onValueChange = { meetVenue = it },
                    label = { Text("Venue / Location (स्थान)") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                    placeholder = { Text("Society Clubhouse / Main Gate Garden") },
                    modifier = Modifier.fillMaxWidth().testTag("meet_venue_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = meetOrganizer,
                    onValueChange = { meetOrganizer = it },
                    label = { Text("Organizer / Calling Body (आयोजक)") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth().testTag("meet_organizer_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = meetAgenda,
                    onValueChange = { meetAgenda = it },
                    label = { Text("Agenda Details (एजेंडा बिंदु)") },
                    placeholder = { Text("1. Budget Approval\n2. Vendor Selection\n3. Contribution per Flat") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("meet_agenda_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (meetTitle.isNotBlank() && meetPurpose.isNotBlank()) {
                            viewModel.scheduleMeeting(
                                title = meetTitle,
                                purposeReason = meetPurpose,
                                category = meetCategory,
                                date = if (meetDate.isBlank()) "Upcoming Sunday, 10:00 AM" else meetDate,
                                time = if (meetTime.isBlank()) "10:00 AM" else meetTime,
                                venue = if (meetVenue.isBlank()) "Society Clubhouse" else meetVenue,
                                organizer = if (meetOrganizer.isBlank()) "Managing Committee" else meetOrganizer,
                                agendaDetails = if (meetAgenda.isBlank()) "1. General Body Discussion\n2. Maintenance updates" else meetAgenda
                            )
                            showScheduleSheet = false
                            meetTitle = ""
                            meetPurpose = ""
                            meetAgenda = ""
                        } else {
                            viewModel.showMessage("Please fill Meeting Title and Purpose")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp).testTag("save_meeting_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Publish & Notify All Residents", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showScheduleSheet = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_schedule_meeting")
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Add, contentDescription = "Call Meeting")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Call Meeting", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Hero Banner Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2027)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Society Meetings",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "सोसायटी सभाएँ एवं निर्णय मंच",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = "${meetings.size} Scheduled",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Participate in society decision-making, RSVP for upcoming meetings, and view agendas & discussions.",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f),
                            lineHeight = 17.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { showScheduleSheet = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("schedule_meeting_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Call New Meeting", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val msg = "Sapana Park CHS Society Meetings & Noticeboard. Stay informed about key discussions!"
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, msg)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share"))
                                },
                                modifier = Modifier.height(44.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }

            // Category Filter Row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategoryFilter == category,
                            onClick = { selectedCategoryFilter = category },
                            label = { Text(category, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                            leadingIcon = if (selectedCategoryFilter == category) {
                                { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            } else null,
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            if (filteredMeetings.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.EventAvailable, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("No Meetings Found", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("No meetings scheduled under this category. Click 'Call Meeting' to schedule one.", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            } else {
                items(filteredMeetings, key = { it.id }) { meeting ->
                    MeetingCardItem(
                        meeting = meeting,
                        onRsvpToggle = { viewModel.toggleMeetingAttendance(meeting) },
                        onDelete = { viewModel.deleteMeeting(meeting.id) },
                        onShare = {
                            val shareText = "📍 *Sapana Park CHS Meeting Notice*\n\n📌 *Title:* ${meeting.title}\n🎯 *Purpose:* ${meeting.purposeReason}\n📅 *Date & Time:* ${meeting.date} at ${meeting.time}\n📍 *Venue:* ${meeting.venue}\n👤 *Organizer:* ${meeting.organizer}\n\n*Agenda:* ${meeting.agendaDetails}\n\nPlease attend without fail!"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?text=" + Uri.encode(shareText)))
                            context.startActivity(intent)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun MeetingCardItem(
    meeting: SocietyMeetingEntity,
    onRsvpToggle: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().testTag("meeting_card_${meeting.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AssistChip(
                        onClick = {},
                        label = { Text(meeting.category, fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "By ${meeting.organizer}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row {
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Chat,
                            contentDescription = "Share WhatsApp",
                            tint = Color(0xFF25D366),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Meeting",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = meeting.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Meeting Objective / Purpose (उद्देश्य):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = meeting.purposeReason,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(meeting.date, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(meeting.time, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFE53935), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(meeting.venue, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            if (meeting.agendaDetails.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Agenda Points:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text(
                    text = meeting.agendaDetails,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${meeting.attendeeCount} Residents Attending",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }

                Button(
                    onClick = onRsvpToggle,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (meeting.isAttending) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("rsvp_btn_${meeting.id}")
                ) {
                    Icon(
                        imageVector = if (meeting.isAttending) Icons.Default.CheckCircle else Icons.Default.EventAvailable,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (meeting.isAttending) "Attending ✓" else "RSVP Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
