package com.example.ui.screens.dashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SmartToy
import com.example.ui.components.AiAutoFixDialog
import com.example.ui.components.SapanaParkWelcomeBanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NoticeEntity
import com.example.data.model.SocietyEventEntity
import com.example.data.model.SocietyMeetingEntity
import com.example.ui.components.EmergencyBottomSheet
import com.example.ui.theme.AmberGold
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.viewmodel.SocietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: SocietyViewModel,
    onNavigateToMeetings: () -> Unit,
    onNavigateToComplaints: () -> Unit,
    onNavigateToDocuments: () -> Unit,
    onNavigateToCommunity: () -> Unit,
    onNavigateToCommunication: () -> Unit
) {
    val userSession by viewModel.userSession.collectAsState()
    val notices by viewModel.notices.collectAsState()
    val bills by viewModel.bills.collectAsState()
    val complaints by viewModel.complaints.collectAsState()
    val events by viewModel.events.collectAsState()
    val meetings by viewModel.meetings.collectAsState()

    val context = LocalContext.current
    var showEmergencySheet by remember { mutableStateOf(false) }
    var showInviteSheet by remember { mutableStateOf(false) }
    var showAiAutoFixDialog by remember { mutableStateOf(false) }
    var showScheduleMeetingSheet by remember { mutableStateOf(false) }

    // Schedule Meeting Form State
    var meetTitle by remember { mutableStateOf("") }
    var meetPurpose by remember { mutableStateOf("") }
    var meetCategory by remember { mutableStateOf("Water & Utility") }
    var meetDate by remember { mutableStateOf("") }
    var meetTime by remember { mutableStateOf("") }
    var meetVenue by remember { mutableStateOf("") }
    var meetOrganizer by remember { mutableStateOf(userSession.name) }
    var meetAgenda by remember { mutableStateOf("") }

    if (showAiAutoFixDialog) {
        AiAutoFixDialog(
            viewModel = viewModel,
            onDismiss = { showAiAutoFixDialog = false }
        )
    }

    // Simulation Form State
    var simFriendName by remember { mutableStateOf("") }
    var simFriendPhone by remember { mutableStateOf("") }

    val pendingBill = bills.firstOrNull { !it.isPaid }

    if (showEmergencySheet) {
        EmergencyBottomSheet(viewModel = viewModel, onDismiss = { showEmergencySheet = false })
    }

    // Invite & Share Bottom Sheet
    if (showInviteSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showInviteSheet = false },
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Invite Friends & Earn ₹50 Bonus",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "सोसाइटी सदस्यों को इन्वाइट करें और ₹50 बोनस पाएँ",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = { showInviteSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Wallet & Earnings Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2027)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Referral Bonus Wallet", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
                            Text(
                                "₹%.2f".format(userSession.referralWalletBalance),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFFD700)
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Total Friends Joined", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                            Text("${userSession.totalReferralsCount} Members", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Referral Code Copy Box
                Text("Your Referral Code (आपका रेफ़रल कोड):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = userSession.referralCode,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Referral Code", userSession.referralCode)
                                clipboard.setPrimaryClip(clip)
                                viewModel.showMessage("Referral Code copied to clipboard ✓")
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy Code", fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Share Across All Platforms Button
                Button(
                    onClick = {
                        val inviteMsg = "Sapana Park Society App Join Karein! Mere referral code '${userSession.referralCode}' se register karein aur ₹50 Instant Bonus paayein! App Link: https://sapanapark.org/invite?ref=${userSession.referralCode}"
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, inviteMsg)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Invite via")
                        context.startActivity(shareIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("share_all_platforms_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share Invite Link (सारे ऐप्स पर शेयर करें)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Simulate Friend Registration & Instant ₹50 Credit
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Simulate New Registration (नया रजिस्ट्रेशन टेस्ट करें)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "जब कोई मित्र आपकी रेफ़रल कोड दर्ज करके रजिस्टर करता है, तो ₹50 तुरंत मिलता है:",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = simFriendName,
                            onValueChange = { simFriendName = it },
                            label = { Text("Friend / Resident Name (मित्र का नाम)") },
                            leadingIcon = { Icon(Icons.Default.GroupAdd, null) },
                            modifier = Modifier.fillMaxWidth().testTag("sim_friend_name_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = simFriendPhone,
                            onValueChange = { simFriendPhone = it },
                            label = { Text("Mobile Number (मोबाइल नंबर)") },
                            modifier = Modifier.fillMaxWidth().testTag("sim_friend_phone_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.claimReferralBonus(simFriendName, simFriendPhone)
                                simFriendName = ""
                                simFriendPhone = ""
                            },
                            modifier = Modifier.fillMaxWidth().testTag("claim_sim_bonus_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00))
                        ) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simulate Registration & Earn ₹50 Bonus", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Schedule Colony Meeting Bottom Sheet
    if (showScheduleMeetingSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showScheduleMeetingSheet = false },
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
                            text = "Schedule Colony Meeting (मीटिंग रखें)",
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
                    IconButton(onClick = { showScheduleMeetingSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Meeting Title
                OutlinedTextField(
                    value = meetTitle,
                    onValueChange = { meetTitle = it },
                    label = { Text("Meeting Title (मीटिंग का शीर्षक)*") },
                    leadingIcon = { Icon(Icons.Default.Groups, null) },
                    placeholder = { Text("e.g. पानी की समस्या एवं ओवरहेड टैंक सफाई बैठक") },
                    modifier = Modifier.fillMaxWidth().testTag("meet_title_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Meeting Purpose / Objective ("kis liye li ja rahi hai")
                OutlinedTextField(
                    value = meetPurpose,
                    onValueChange = { meetPurpose = it },
                    label = { Text("Meeting Reason / Purpose (मीटिंग किस लिए ली जा रही है)*") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.HelpOutline, null) },
                    placeholder = { Text("जैसे: नए समरसेबल पंप मोटर खरीदने एवं सीसीटीवी बजट स्वीकृति हेतु") },
                    modifier = Modifier.fillMaxWidth().testTag("meet_purpose_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Category (मीटिंग का प्रकार):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(4.dp))
                val meetingCategories = listOf("Water & Utility", "Security & Safety", "General Body", "Financial Audit", "Emergency", "Celebration")
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    meetingCategories.chunked(3).forEach { chunk ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                            chunk.forEach { cat ->
                                FilterChip(
                                    selected = meetCategory == cat,
                                    onClick = { meetCategory = cat },
                                    label = { Text(cat, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Date & Time
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = meetDate,
                        onValueChange = { meetDate = it },
                        label = { Text("Date (तारीख)") },
                        placeholder = { Text("05 Aug 2026") },
                        leadingIcon = { Icon(Icons.Default.CalendarMonth, null) },
                        modifier = Modifier.weight(1f).testTag("meet_date_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = meetTime,
                        onValueChange = { meetTime = it },
                        label = { Text("Time (समय)") },
                        placeholder = { Text("07:30 PM") },
                        leadingIcon = { Icon(Icons.Default.AccessTime, null) },
                        modifier = Modifier.weight(1f).testTag("meet_time_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Venue & Organizer
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = meetVenue,
                        onValueChange = { meetVenue = it },
                        label = { Text("Venue (स्थान)") },
                        placeholder = { Text("Clubhouse") },
                        leadingIcon = { Icon(Icons.Default.Place, null) },
                        modifier = Modifier.weight(1f).testTag("meet_venue_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = meetOrganizer,
                        onValueChange = { meetOrganizer = it },
                        label = { Text("Organizer (आयोजक)") },
                        placeholder = { Text("Secretary") },
                        leadingIcon = { Icon(Icons.Default.Person, null) },
                        modifier = Modifier.weight(1f).testTag("meet_organizer_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Agenda Details
                OutlinedTextField(
                    value = meetAgenda,
                    onValueChange = { meetAgenda = it },
                    label = { Text("Agenda Details (एजेंडा बिंदु)") },
                    placeholder = { Text("1. बजट पारित करना\n2. काम की समय-सीमा तय करना") },
                    modifier = Modifier.fillMaxWidth().testTag("meet_agenda_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (meetTitle.isNotBlank() || meetPurpose.isNotBlank()) {
                            viewModel.scheduleMeeting(
                                title = meetTitle,
                                purposeReason = meetPurpose,
                                category = meetCategory,
                                date = meetDate,
                                time = meetTime,
                                venue = meetVenue,
                                organizer = meetOrganizer,
                                agendaDetails = meetAgenda
                            )
                            showScheduleMeetingSheet = false
                            meetTitle = ""
                            meetPurpose = ""
                            meetDate = ""
                            meetTime = ""
                            meetVenue = ""
                            meetAgenda = ""
                        } else {
                            viewModel.showMessage("कृपया मीटिंग का शीर्षक या कारण दर्ज करें")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("submit_schedule_meet_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027))
                ) {
                    Icon(Icons.Default.Groups, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Schedule Meeting on Dashboard (मीटिंग पोस्ट करें)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .testTag("home_screen_container")
    ) {
        // Hero Header Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Navy900,
                            Navy800,
                            Navy700
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Apartment,
                                contentDescription = null,
                                tint = AmberGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Sapana Park CHS Ltd.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                        Text(
                            text = "Hello, ${userSession.name} (${userSession.role.label})",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "${userSession.wing} • Room: ${userSession.roomNo} • Floor: ${userSession.floorNo}",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (userSession.isPromotionAllowed) EmeraldGreen.copy(alpha = 0.3f) else CrimsonRed.copy(alpha = 0.3f),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = if (userSession.isPromotionAllowed) "🌐 Internet Promotion Active ✓" else "🌐 Internet Promotion Disabled ✕",
                                fontSize = 10.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF8B5CF6).copy(alpha = 0.35f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(
                                onClick = { showAiAutoFixDialog = true },
                                modifier = Modifier.testTag("ai_autofix_header_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = "AI Auto-Fix Assistant",
                                    tint = Color.White
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = {
                                viewModel.showMessage("No new unread system notifications")
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))



                // Invite & Earn ₹50 Bonus Prominent Banner Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showInviteSheet = true }
                        .testTag("invite_bonus_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF2E7D32),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CardGiftcard,
                                        contentDescription = null,
                                        tint = Color(0xFFFFD700),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Invite & Earn ₹50 Bonus",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFFFD700)
                                    ) {
                                        Text(
                                            text = "₹50",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Code: ${userSession.referralCode} • Wallet: ₹%.2f".format(userSession.referralWalletBalance),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2E7D32)
                                )
                                Text(
                                    text = "सारे ऐप्स (WhatsApp, SMS) पर शेयर करके बोनस पाएँ",
                                    fontSize = 10.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Button(
                            onClick = { showInviteSheet = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("invite_share_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Invite", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // AI Auto-Fix Assistant Prominent Banner Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1E1B4B)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAiAutoFixDialog = true }
                        .testTag("ai_autofix_banner")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF8B5CF6).copy(alpha = 0.25f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.SmartToy,
                                        contentDescription = null,
                                        tint = Color(0xFFA78BFA),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "AI Auto-Fix Assistant",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFF7C3AED)
                                    ) {
                                        Text(
                                            text = "AUTO-RESOLVE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "App error or bill issue? Click to auto-resolve via AI (समस्या हल करें)",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Actions Section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Quick Dashboard Actions",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionItem(
                    title = "Meetings",
                    icon = Icons.Default.Groups,
                    color = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMeetings
                )
                QuickActionItem(
                    title = "Invite & ₹50",
                    icon = Icons.Default.Share,
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.weight(1f),
                    onClick = { showInviteSheet = true }
                )
                QuickActionItem(
                    title = "Complaints",
                    icon = Icons.Default.Build,
                    color = Color(0xFFFB8C00),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToComplaints
                )
                QuickActionItem(
                    title = "Emergency",
                    icon = Icons.Default.PhoneInTalk,
                    color = Color(0xFFE53935),
                    modifier = Modifier.weight(1f),
                    onClick = { showEmergencySheet = true }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionItem(
                    title = "Committee",
                    icon = Icons.Default.Group,
                    color = Color(0xFF8E24AA),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToCommunication
                )
                QuickActionItem(
                    title = "Events",
                    icon = Icons.Default.CalendarMonth,
                    color = Color(0xFF00ACC1),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToCommunity
                )
                QuickActionItem(
                    title = "WhatsApp",
                    icon = Icons.AutoMirrored.Filled.Chat,
                    color = Color(0xFF25D366),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://chat.whatsapp.com/sapanaparkchs"))
                        context.startActivity(intent)
                    }
                )
                QuickActionItem(
                    title = "Feedback",
                    icon = Icons.Default.Feedback,
                    color = Color(0xFF3949AB),
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToCommunication
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Colony Meetings & Decisions Section
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Colony Meetings & Decisions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = { showScheduleMeetingSheet = true },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027)),
                    modifier = Modifier.testTag("call_meeting_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Call Meeting (मीटिंग रखें)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (meetings.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("No active meetings scheduled. Tap 'Call Meeting' to post a meeting.", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    meetings.forEach { meeting ->
                        ColonyMeetingCard(
                            meeting = meeting,
                            onToggleAttendance = { viewModel.toggleMeetingAttendance(meeting) },
                            onDeleteMeeting = { viewModel.deleteMeeting(meeting.id) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Important Society Notices Section
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Announcement,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Society Notice Board",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                TextButton(onClick = onNavigateToDocuments) {
                    Text("View All", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notices) { notice ->
                    NoticeCard(notice = notice)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Upcoming Community Initiatives Highlight
        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Upcoming Community Events",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            val featuredEvent = events.firstOrNull()
            if (featuredEvent != null) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = featuredEvent.category,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "${featuredEvent.date} • ${featuredEvent.time}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = featuredEvent.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = featuredEvent.description,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Organizer: ${featuredEvent.organizer}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Button(
                                onClick = { viewModel.toggleEventRSVP(featuredEvent) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (featuredEvent.isRegistered) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text(if (featuredEvent.isRegistered) "Registered ✓" else "Register RSVP", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun NoticeCard(notice: NoticeEntity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notice.isImportant) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        modifier = Modifier.width(260.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (notice.isImportant) Color(0xFFE65100) else MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = notice.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = notice.date,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = notice.title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = notice.description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ColonyMeetingCard(
    meeting: SocietyMeetingEntity,
    onToggleAttendance: () -> Unit,
    onDeleteMeeting: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth().testTag("colony_meeting_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF0284C7)
                ) {
                    Text(
                        text = meeting.category,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${meeting.date} • ${meeting.time}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(onClick = onDeleteMeeting, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = meeting.title,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Reason / Purpose Highlight Box ("Meeting kis liye li ja rahi hai")
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFF7ED),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎯 मीटिंग का उद्देश्य / कारण (Meeting Objective):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC2410C)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = meeting.purposeReason,
                        fontSize = 12.sp,
                        color = Color(0xFF7C2D12),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Venue: ${meeting.venue}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Text(text = "By: ${meeting.organizer}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            }

            if (meeting.agendaDetails.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Agenda: ${meeting.agendaDetails}", fontSize = 11.sp, color = Color.Gray, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Groups, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "${meeting.attendeeCount} Residents Attending", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                }

                Button(
                    onClick = onToggleAttendance,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (meeting.isAttending) Color(0xFF059669) else Color(0xFF2563EB)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("toggle_attend_btn")
                ) {
                    if (meeting.isAttending) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Attending ✓", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text("Confirm Attendance", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
