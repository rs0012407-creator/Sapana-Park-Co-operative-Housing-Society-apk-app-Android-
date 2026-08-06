package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventSheet(
    onDismiss: () -> Unit,
    onSubmit: (
        title: String,
        category: String,
        date: String,
        time: String,
        venue: String,
        description: String,
        organizer: String,
        contactPhone: String,
        posterUri: String?,
        locationName: String,
        latitude: Double?,
        longitude: Double?
    ) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Festival") }
    var date by remember { mutableStateOf("15 Aug 2026") }
    var time by remember { mutableStateOf("05:00 PM - 08:00 PM") }
    var venue by remember { mutableStateOf("Society Clubhouse Main Hall") }
    var description by remember { mutableStateOf("") }
    var organizer by remember { mutableStateOf("Sapana Park Cultural Committee") }
    var contactPhone by remember { mutableStateOf("+91 98765 43210") }

    // Poster and Location States
    var posterUriString by remember { mutableStateOf<String?>(null) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var customLocationName by remember { mutableStateOf("Sapana Park CHS, Pune") }
    var deviceLat by remember { mutableStateOf<Double?>(18.5204) }
    var deviceLng by remember { mutableStateOf<Double?>(73.8567) }
    var isLocationFetched by remember { mutableStateOf(false) }

    // Permission States
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }
    var hasLocationPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }
    var isPromotionAllowed by remember { mutableStateOf(true) }

    // Activity Result Launchers
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap
            posterUriString = null // Use captured bitmap
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            posterUriString = uri.toString()
            capturedBitmap = null
        }
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        hasCameraPermission = permissionsMap[Manifest.permission.CAMERA] ?: hasCameraPermission
        hasLocationPermission = permissionsMap[Manifest.permission.ACCESS_FINE_LOCATION]
            ?: permissionsMap[Manifest.permission.ACCESS_COARSE_LOCATION]
            ?: hasLocationPermission
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            hasNotificationPermission = permissionsMap[Manifest.permission.POST_NOTIFICATIONS] ?: hasNotificationPermission
        }
    }

    val categories = listOf("Festival", "Health Camp", "Women's Empowerment", "Career Guidance", "Sports & Fitness", "General")

    val presetPosters = listOf(
        "https://picsum.photos/seed/festival/600/350" to "Festival Celebration",
        "https://picsum.photos/seed/health/600/350" to "Health & Wellness",
        "https://picsum.photos/seed/workshop/600/350" to "Workshop & Seminar",
        "https://picsum.photos/seed/sports/600/350" to "Sports Tournament"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
                .testTag("add_event_sheet")
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Navy900),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = AmberGold)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = "Create New Event", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(text = "Add event details, poster & device location", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Event Title (कार्यक्रम का नाम)") },
                placeholder = { Text("e.g., Blood Donation Camp 2026") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category Chips
            Text(text = "Event Category:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        leadingIcon = if (selectedCategory == cat) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date & Time
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // SECTION: EVENT POSTER ATTACHMENT
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Image, contentDescription = null, tint = Navy800, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Event Poster & Banner (पोस्टर जोड़ें)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray.copy(alpha = 0.3f))
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (capturedBitmap != null) {
                            Image(
                                bitmap = capturedBitmap!!.asImageBitmap(),
                                contentDescription = "Captured Poster",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (!posterUriString.isNullOrBlank()) {
                            AsyncImage(
                                model = posterUriString,
                                contentDescription = "Selected Poster",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(36.dp), tint = Color.Gray)
                                Text(text = "No poster attached", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Media Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (hasCameraPermission) {
                                    cameraLauncher.launch()
                                } else {
                                    permissionsLauncher.launch(
                                        arrayOf(Manifest.permission.CAMERA)
                                    )
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Camera", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                galleryLauncher.launch("image/*")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Gallery", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Or choose a preset poster banner:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(presetPosters) { (url, label) ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (posterUriString == url) AmberGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, if (posterUriString == url) AmberGold else Color.Transparent),
                                modifier = Modifier.clickable {
                                    posterUriString = url
                                    capturedBitmap = null
                                }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SECTION: LOCATION & DEVICE LINKING
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Location & GPS Linking", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Button(
                            onClick = {
                                if (hasLocationPermission) {
                                    try {
                                        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                                        val loc: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                                        if (loc != null) {
                                            deviceLat = loc.latitude
                                            deviceLng = loc.longitude
                                        } else {
                                            deviceLat = 18.5204
                                            deviceLng = 73.8567
                                        }
                                        isLocationFetched = true
                                        customLocationName = "Sapana Park CHS Premises (Lat: ${"%.4f".format(deviceLat)}, Lng: ${"%.4f".format(deviceLng)})"
                                    } catch (e: SecurityException) {
                                        deviceLat = 18.5204
                                        deviceLng = 73.8567
                                        isLocationFetched = true
                                    }
                                } else {
                                    permissionsLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isLocationFetched) "GPS Linked ✓" else "Link Device GPS", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = venue,
                        onValueChange = { venue = it },
                        label = { Text("Venue / Hall Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = customLocationName,
                        onValueChange = { customLocationName = it },
                        label = { Text("Detailed Location / Landmark") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    if (deviceLat != null && deviceLng != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "📍 Device Coordinates Linked: Latitude ${"%.4f".format(deviceLat)}, Longitude ${"%.4f".format(deviceLng)}",
                                fontSize = 11.sp,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // SECTION: PERMISSIONS & PROMOTIONS STATUS
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = Navy800, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "App & Device Permissions (डिवाइस अनुमति)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Camera & Gallery Permission Badge
                    PermissionRow(
                        title = "Camera & Media Access (कैमरा व गैलरी)",
                        isGranted = hasCameraPermission,
                        onRequest = {
                            permissionsLauncher.launch(
                                arrayOf(
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.READ_MEDIA_IMAGES
                                )
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Location Permission Badge
                    PermissionRow(
                        title = "Device Location GPS (लोकेशन एक्सेस)",
                        isGranted = hasLocationPermission,
                        onRequest = {
                            permissionsLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Promotion & Notifications Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = AmberGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(text = "Promotional Event Broadcast", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "Notify society residents via push alert", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Button(
                            onClick = { isPromotionAllowed = !isPromotionAllowed },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPromotionAllowed) EmeraldGreen else Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(if (isPromotionAllowed) "Allowed ✓" else "Off", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Organizer & Description
            OutlinedTextField(
                value = organizer,
                onValueChange = { organizer = it },
                label = { Text("Organizer Name") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = contactPhone,
                onValueChange = { contactPhone = it },
                label = { Text("Contact Phone") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Event Description & Details (कार्यक्रम का विवरण)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = {
                    if (title.isBlank()) return@Button
                    onSubmit(
                        title,
                        selectedCategory,
                        date,
                        time,
                        venue,
                        description,
                        organizer,
                        contactPhone,
                        posterUriString ?: capturedBitmap?.let { "captured_image" },
                        customLocationName,
                        deviceLat,
                        deviceLng
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_event_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Navy900)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = AmberGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Publish Event & Broadcast (इवेंट प्रकाशित करें)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PermissionRow(
    title: String,
    isGranted: Boolean,
    onRequest: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        if (isGranted) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = EmeraldGreen.copy(alpha = 0.15f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Allowed", fontSize = 11.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                }
            }
        } else {
            OutlinedButton(
                onClick = onRequest,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text(text = "Allow Permission", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
