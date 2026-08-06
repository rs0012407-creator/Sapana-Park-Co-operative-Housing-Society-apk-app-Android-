package com.example.ui.screens.community

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.AchievementItem
import com.example.data.model.GalleryPhotoItem
import com.example.data.model.SocietyEventEntity
import com.example.ui.components.AddEventSheet
import com.example.ui.components.EventDetailDialog
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.viewmodel.SocietyViewModel

@Composable
fun CommunityScreen(
    viewModel: SocietyViewModel
) {
    val events by viewModel.events.collectAsState()
    val achievements = viewModel.achievements
    val galleryPhotos = viewModel.galleryPhotos

    var selectedTab by remember { mutableStateOf(0) } // 0: Events, 1: Achievement Wall, 2: Photo Gallery
    var showAddEventSheet by remember { mutableStateOf(false) }
    var selectedEventForDetail by remember { mutableStateOf<SocietyEventEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddEventSheet = true },
                    containerColor = Navy900,
                    contentColor = AmberGold,
                    shape = CircleShape,
                    modifier = Modifier.testTag("add_event_fab")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Event")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Add Event", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                    }
                }
            }
        },
        modifier = Modifier.testTag("community_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Navy900)
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Community & Social Initiatives",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Events, Posters, GPS Location & Pride Wall",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }

                    if (selectedTab == 0) {
                        Button(
                            onClick = { showAddEventSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberGold),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("+ Create", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Navy900)
                        }
                    }
                }
            }

            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Events & Posters") },
                    icon = { Icon(Icons.Default.CalendarMonth, null) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Achievements") },
                    icon = { Icon(Icons.Default.EmojiEvents, null) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Gallery") },
                    icon = { Icon(Icons.Default.PhotoLibrary, null) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedTab) {
                0 -> {
                    if (events.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(56.dp), tint = Color.Gray)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "No society events found", fontWeight = FontWeight.Bold, color = Color.Gray)
                                Text(text = "Tap '+ Create' to add an event with poster & location", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(events) { event ->
                                EventCardItem(
                                    event = event,
                                    onClickDetails = { selectedEventForDetail = event },
                                    onRSVPToggle = { viewModel.toggleEventRSVP(event) },
                                    onStopToggle = { viewModel.toggleEventStopped(event) },
                                    onDelete = { viewModel.deleteEvent(event.id, event.title) }
                                )
                            }
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
                        items(achievements) { item ->
                            AchievementCardItem(item = item)
                        }
                    }
                }
                2 -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(galleryPhotos) { photo ->
                            GalleryPhotoCardItem(photo = photo)
                        }
                    }
                }
            }
        }
    }

    // Add Event BottomSheet
    if (showAddEventSheet) {
        AddEventSheet(
            onDismiss = { showAddEventSheet = false },
            onSubmit = { title, category, date, time, venue, description, organizer, contactPhone, posterUri, locationName, lat, lng ->
                viewModel.createEvent(
                    title = title,
                    category = category,
                    date = date,
                    time = time,
                    venue = venue,
                    description = description,
                    organizer = organizer,
                    contactPhone = contactPhone,
                    posterUri = posterUri,
                    locationName = locationName,
                    latitude = lat,
                    longitude = lng
                )
            }
        )
    }

    // Event Detail Dialog
    selectedEventForDetail?.let { event ->
        EventDetailDialog(
            event = event,
            onDismiss = { selectedEventForDetail = null },
            onRSVPToggle = {
                viewModel.toggleEventRSVP(event)
                selectedEventForDetail = event.copy(
                    isRegistered = !event.isRegistered,
                    registrationCount = event.registrationCount + if (!event.isRegistered) 1 else -1
                )
            },
            onStopToggle = {
                viewModel.toggleEventStopped(event)
                selectedEventForDetail = event.copy(isStopped = !event.isStopped)
            },
            onDelete = {
                viewModel.deleteEvent(event.id, event.title)
                selectedEventForDetail = null
            }
        )
    }
}

@Composable
private fun EventCardItem(
    event: SocietyEventEntity,
    onClickDetails: () -> Unit,
    onRSVPToggle: () -> Unit,
    onStopToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isStopped) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickDetails() }
    ) {
        Column {
            // Optional Poster Image Banner
            if (!event.posterUri.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                        .background(Navy900)
                ) {
                    AsyncImage(
                        model = event.posterUri,
                        contentDescription = "Event Poster",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    if (event.isStopped) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = CrimsonRed,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "STOPPED",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = event.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        if (event.isStopped && event.posterUri.isNullOrBlank()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CrimsonRed.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "STOPPED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonRed,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${event.registrationCount} Registered",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = event.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = event.description,
                    fontSize = 12.sp,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "${event.date} • ${event.time}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = event.venue, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                if (event.latitude != null && event.longitude != null) {
                    Text(
                        text = "📍 GPS Location Linked (${"%.4f".format(event.latitude)}, ${"%.4f".format(event.longitude)})",
                        fontSize = 10.sp,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 2.dp, start = 22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onClickDetails,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Poster & Details", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        IconButton(onClick = onStopToggle, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.Block, contentDescription = "Stop Event", tint = if (event.isStopped) EmeraldGreen else CrimsonRed, modifier = Modifier.size(18.dp))
                        }

                        IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Event", tint = CrimsonRed, modifier = Modifier.size(18.dp))
                        }

                        Button(
                            onClick = onRSVPToggle,
                            enabled = !event.isStopped,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (event.isRegistered) EmeraldGreen else MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("rsvp_event_btn")
                        ) {
                            Icon(imageVector = if (event.isRegistered) Icons.Default.CheckCircle else Icons.Default.HowToReg, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (event.isStopped) "Stopped" else if (event.isRegistered) "RSVP ✓" else "RSVP", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AchievementCardItem(item: AchievementItem) {
    Card(
        shape = RoundedCornerShape(18.dp),
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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFF8E1)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(28.dp))
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = item.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                Text(text = "${item.nomineeName} (${item.flatNo})", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
                Text(text = item.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun GalleryPhotoCardItem(photo: GalleryPhotoItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = photo.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
            Text(text = photo.eventName, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = photo.date, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}
