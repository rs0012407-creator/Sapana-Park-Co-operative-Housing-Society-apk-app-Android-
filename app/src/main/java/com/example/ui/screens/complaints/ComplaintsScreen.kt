package com.example.ui.screens.complaints

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Timer
import com.example.ui.components.AiAutoFixDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ComplaintEntity
import com.example.viewmodel.SocietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintsScreen(
    viewModel: SocietyViewModel
) {
    val complaints by viewModel.complaints.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0: All, 1: Pending/Active, 2: Resolved
    var showNewComplaintModal by remember { mutableStateOf(false) }
    var showAiAutoFixDialog by remember { mutableStateOf(false) }

    if (showAiAutoFixDialog) {
        AiAutoFixDialog(viewModel = viewModel, onDismiss = { showAiAutoFixDialog = false })
    }

    // New Complaint Form State
    var newTitle by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Plumbing") }
    var newPriority by remember { mutableStateOf("Medium") }
    var newDescription by remember { mutableStateOf("") }
    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var categoryExpanded by remember { mutableStateOf(false) }

    // Camera & Gallery Launchers
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            selectedPhotoUri = "captured_bitmap"
            viewModel.showMessage("Captured camera photo attached ✓")
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedPhotoUri = uri.toString()
            capturedBitmap = null
            viewModel.showMessage("Gallery image attached ✓")
        }
    }

    val categories = listOf("Plumbing", "Electrical", "Security", "Cleanliness", "Lift / Elevator", "Parking", "Water Supply", "Noise & Disturbances")
    val priorities = listOf("Low", "Medium", "High", "Critical")

    val filteredComplaints = when (selectedTab) {
        1 -> complaints.filter { it.status == "Submitted" || it.status == "In Progress" }
        2 -> complaints.filter { it.status == "Resolved" || it.status == "Closed" }
        else -> complaints
    }

    if (showNewComplaintModal) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showNewComplaintModal = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Log Maintenance Complaint",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Society caretaker will assign a technician immediately",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Complaint Summary / Title") },
                    placeholder = { Text("e.g. Water leak in balcony pipe") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("complaint_title_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = newCategory,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    newCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Priority Level:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    priorities.forEach { p ->
                        FilterChip(
                            selected = newPriority == p,
                            onClick = { newPriority = p },
                            label = { Text(p, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Detailed Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("complaint_desc_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Photo Evidence Picker with Camera & Gallery Permission Buttons
                Text(
                    text = "Upload Photo Evidence (Camera / Gallery Allowed):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { cameraLauncher.launch() },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("camera_photo_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Camera", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("gallery_photo_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gallery", fontSize = 12.sp)
                    }
                }

                // Photo Preview if selected
                if (selectedPhotoUri != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black.copy(alpha = 0.05f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (capturedBitmap != null) {
                            Image(
                                bitmap = capturedBitmap!!.asImageBitmap(),
                                contentDescription = "Camera Evidence",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (selectedPhotoUri != "captured_bitmap") {
                            AsyncImage(
                                model = selectedPhotoUri,
                                contentDescription = "Gallery Evidence",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        IconButton(
                            onClick = {
                                selectedPhotoUri = null
                                capturedBitmap = null
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Remove", tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            val finalUri = if (selectedPhotoUri != null) {
                                selectedPhotoUri
                            } else null

                            viewModel.createComplaint(newTitle, newCategory, newPriority, newDescription, finalUri)
                            showNewComplaintModal = false
                            newTitle = ""
                            newDescription = ""
                            selectedPhotoUri = null
                            capturedBitmap = null
                        } else {
                            viewModel.showMessage("Please enter a complaint title.")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_complaint_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027))
                ) {
                    Text("Submit Complaint", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewComplaintModal = true },
                containerColor = Color(0xFF0F2027),
                contentColor = Color.White,
                modifier = Modifier.testTag("raise_complaint_fab")
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("New Complaint", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("complaints_screen")
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F2027))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Complaints & Maintenance Desk",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Track Repair Progress & Rate Resolution",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    Button(
                        onClick = { showAiAutoFixDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("ai_autofix_complaint_btn")
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("AI Fix", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("All (${complaints.size})") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Active") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Resolved") })
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredComplaints.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No complaints found in this section.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredComplaints) { complaint ->
                        ComplaintCardItem(
                            complaint = complaint,
                            onRateSubmit = { rating, comment ->
                                viewModel.rateComplaint(complaint, rating, comment)
                            },
                            onStatusChange = { newStatus ->
                                viewModel.updateComplaintStatus(complaint, newStatus)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComplaintCardItem(
    complaint: ComplaintEntity,
    onRateSubmit: (Int, String) -> Unit,
    onStatusChange: (String) -> Unit
) {
    var userRating by remember { mutableStateOf(complaint.feedbackRating) }
    var feedbackText by remember { mutableStateOf(complaint.feedbackComment) }

    val priorityColor = when (complaint.priority) {
        "Critical" -> Color(0xFFD32F2F)
        "High" -> Color(0xFFE65100)
        "Medium" -> Color(0xFFF57C00)
        else -> Color(0xFF388E3C)
    }

    val statusColor = when (complaint.status) {
        "Resolved", "Closed" -> Color(0xFF2E7D32)
        "In Progress" -> Color(0xFF0288D1)
        else -> Color(0xFFE65100)
    }

    val progressValue = when (complaint.status) {
        "Submitted" -> 0.33f
        "In Progress" -> 0.66f
        else -> 1.0f
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = complaint.complaintNumber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = priorityColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${complaint.priority} Priority",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = priorityColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = statusColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = complaint.status,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = complaint.title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = complaint.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Photo Evidence Display if attached
            if (!complaint.photoUri.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.04f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = complaint.photoUri,
                            contentDescription = "Complaint Photo Evidence",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Surface(
                            shape = RoundedCornerShape(bottomEnd = 8.dp),
                            color = Color.Black.copy(alpha = 0.65f),
                            modifier = Modifier.align(Alignment.TopStart)
                        ) {
                            Text(
                                text = "Photo Evidence Attached ✓",
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Resolution Progress", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = complaint.dateCreated, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progressValue },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = statusColor,
                    trackColor = statusColor.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Assigned Technician Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Assigned: ${complaint.assignedTechnician}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Est. Resolution: ${complaint.estimatedResolution}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            // Action to Mark Resolved / Submit Completed Complaint
            if (complaint.status != "Resolved" && complaint.status != "Closed") {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { onStatusChange("Resolved") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF2E7D32))
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Submit Completed / Mark as Resolved (शिकायत पूर्ण)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Rating Section if Resolved
            if (complaint.status == "Resolved" || complaint.status == "Closed") {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Rate Resolution Service:", fontSize = 12.sp, fontWeight = FontWeight.Bold)

                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    (1..5).forEach { star ->
                        IconButton(
                            onClick = {
                                userRating = star
                                onRateSubmit(star, feedbackText)
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (star <= userRating) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = "$star Stars",
                                tint = if (star <= userRating) Color(0xFFFFB300) else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
