package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneInTalk
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContactEntity
import com.example.viewmodel.SocietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyBottomSheet(
    viewModel: SocietyViewModel? = null,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    val contactsList = viewModel?.emergencyContacts?.collectAsState()?.value ?: emptyList()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryTab by remember { mutableStateOf("All") } // "All", "Colony Incharge & Society", "National Emergency"
    var showAddContactSheet by remember { mutableStateOf(false) }

    // Add / Edit Contact Form State
    var editingContactId by remember { mutableStateOf<Int?>(null) }
    var newTitle by remember { mutableStateOf("") }
    var newPhone by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var newCategory by remember { mutableStateOf("Colony Incharge & Society") }

    // Find specific Colony Incharge & Guard contacts from list
    val inchargeContact = contactsList.find { it.title.contains("Colony Incharge", ignoreCase = true) || it.title.contains("इंचार्ज", ignoreCase = true) }
    val guardContact = contactsList.find { it.title.contains("Guard", ignoreCase = true) || it.title.contains("Security", ignoreCase = true) || it.title.contains("गार्ड", ignoreCase = true) }

    val filteredContacts = contactsList.filter { contact ->
        val matchesCategory = when (selectedCategoryTab) {
            "Colony Incharge & Society" -> contact.category == "Colony Incharge & Society"
            "National Emergency" -> contact.category == "National Emergency"
            else -> true
        }
        val matchesSearch = searchQuery.isBlank() ||
                contact.title.contains(searchQuery, ignoreCase = true) ||
                contact.description.contains(searchQuery, ignoreCase = true) ||
                contact.phone.contains(searchQuery, ignoreCase = true)

        matchesCategory && matchesSearch
    }

    // Add / Edit Contact Bottom Sheet
    if (showAddContactSheet) {
        val addSheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = {
                showAddContactSheet = false
                editingContactId = null
            },
            sheetState = addSheetState
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
                            text = if (editingContactId != null) "Edit Contact / Number" else "Add Colony Incharge / Guard Number",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "कॉलोनी प्रभारी या सिक्योरिटी गार्ड का नंबर अपडेट/दर्ज करें",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = {
                        showAddContactSheet = false
                        editingContactId = null
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Quick Preset Selection Chips
                Text(text = "Quick Presets (त्वरित विकल्प):", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                    AssistChip(
                        onClick = {
                            newTitle = "Colony Incharge (कॉलोनी इंचार्ज)"
                            newDescription = "Sapana Park Overall Colony Incharge & Administrator"
                            newCategory = "Colony Incharge & Society"
                        },
                        label = { Text("Colony Incharge", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Shield, null, modifier = Modifier.size(14.dp)) }
                    )
                    AssistChip(
                        onClick = {
                            newTitle = "Society Guard (सोसाइटी गार्ड)"
                            newDescription = "24x7 Main Gate Security Guard & Visitor Check"
                            newCategory = "Colony Incharge & Society"
                        },
                        label = { Text("Society Guard", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Security, null, modifier = Modifier.size(14.dp)) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    label = { Text("Contact Name / Title (नाम / पद)*") },
                    placeholder = { Text("e.g. Colony Incharge - Gaurav Sharma") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth().testTag("add_emergency_title_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = newPhone,
                    onValueChange = { newPhone = it },
                    label = { Text("Mobile / Telephone Number (फोन नंबर)*") },
                    placeholder = { Text("e.g. +91 98220 00005") },
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    modifier = Modifier.fillMaxWidth().testTag("add_emergency_phone_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Role / Department Description (विवरण)") },
                    placeholder = { Text("e.g. 24x7 Main Gate Security & Maintenance") },
                    leadingIcon = { Icon(Icons.Default.Business, null) },
                    modifier = Modifier.fillMaxWidth().testTag("add_emergency_desc_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Category (प्रकार):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        selected = newCategory == "Colony Incharge & Society",
                        onClick = { newCategory = "Colony Incharge & Society" },
                        label = { Text("Colony Incharge & Society", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = newCategory == "National Emergency",
                        onClick = { newCategory = "National Emergency" },
                        label = { Text("National Emergency", fontSize = 11.sp) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (newTitle.isNotBlank() && newPhone.isNotBlank()) {
                            if (editingContactId != null) {
                                viewModel?.updateEmergencyContact(
                                    id = editingContactId!!,
                                    title = newTitle,
                                    phone = newPhone,
                                    description = newDescription,
                                    category = newCategory
                                )
                            } else {
                                viewModel?.addEmergencyContact(
                                    title = newTitle,
                                    phone = newPhone,
                                    description = newDescription,
                                    category = newCategory
                                )
                            }
                            showAddContactSheet = false
                            editingContactId = null
                            newTitle = ""
                            newPhone = ""
                            newDescription = ""
                        } else {
                            viewModel?.showMessage("कृपया नाम और मोबाइल नंबर भरें")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp).testTag("save_emergency_contact_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Icon(Icons.Default.Phone, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (editingContactId != null) "Update Number (अपडेट करें)" else "Save Contact (नंबर सेव करें)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("emergency_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFEBEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Emergency",
                            tint = Color(0xFFD32F2F)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Emergency & Colony Hotline",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "कॉलोनी इंचार्ज व सोसाइटी गार्ड डायरेक्ट फोन नंबर",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Prominent Featured Cards for Colony Incharge & Guard
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1: Colony Incharge
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("colony_incharge_card")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF047857),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = inchargeContact?.title ?: "Colony Incharge (कॉलोनी इंचार्ज)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF064E3B)
                                )
                                Text(
                                    text = inchargeContact?.description ?: "Sapana Park Colony Overall Incharge",
                                    fontSize = 11.sp,
                                    color = Color(0xFF047857)
                                )
                                Text(
                                    text = inchargeContact?.phone ?: "+91 98220 00005",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF065F46)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    editingContactId = inchargeContact?.id ?: 1
                                    newTitle = inchargeContact?.title ?: "Colony Incharge (कॉलोनी इंचार्ज)"
                                    newPhone = inchargeContact?.phone ?: "+91 98220 00005"
                                    newDescription = inchargeContact?.description ?: "Sapana Park Overall Colony Incharge"
                                    newCategory = "Colony Incharge & Society"
                                    showAddContactSheet = true
                                },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Number", tint = Color(0xFF047857), modifier = Modifier.size(18.dp))
                            }

                            Button(
                                onClick = {
                                    val phone = inchargeContact?.phone ?: "+919822000005"
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(36.dp).testTag("call_colony_incharge_btn")
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Now", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Card 2: Society Guard
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().testTag("society_guard_card")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFB91C1C),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Security, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = guardContact?.title ?: "Society Guard (सोसाइटी गार्ड)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF7F1D1D)
                                )
                                Text(
                                    text = guardContact?.description ?: "24x7 Main Gate Security Guard Desk",
                                    fontSize = 11.sp,
                                    color = Color(0xFF991B1B)
                                )
                                Text(
                                    text = guardContact?.phone ?: "+91 98220 00001",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFB91C1C)
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    editingContactId = guardContact?.id ?: 2
                                    newTitle = guardContact?.title ?: "Society Guard (सोसाइटी गार्ड)"
                                    newPhone = guardContact?.phone ?: "+91 98220 00001"
                                    newDescription = guardContact?.description ?: "24x7 Main Gate Security Guard Desk"
                                    newCategory = "Colony Incharge & Society"
                                    showAddContactSheet = true
                                },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Number", tint = Color(0xFFB91C1C), modifier = Modifier.size(18.dp))
                            }

                            Button(
                                onClick = {
                                    val phone = guardContact?.phone ?: "+919822000001"
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB91C1C)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(36.dp).testTag("call_society_guard_btn")
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Call Guard", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search & Add Number Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search contact / department...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("search_emergency_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                if (viewModel != null) {
                    Button(
                        onClick = {
                            editingContactId = null
                            newTitle = ""
                            newPhone = ""
                            newDescription = ""
                            showAddContactSheet = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(48.dp).testTag("open_add_emergency_contact_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Number", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Filter Tabs
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = selectedCategoryTab == "All",
                    onClick = { selectedCategoryTab = "All" },
                    label = { Text("All (${contactsList.size})", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )

                FilterChip(
                    selected = selectedCategoryTab == "Colony Incharge & Society",
                    onClick = { selectedCategoryTab = "Colony Incharge & Society" },
                    label = { Text("Colony Incharges", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )

                FilterChip(
                    selected = selectedCategoryTab == "National Emergency",
                    onClick = { selectedCategoryTab = "National Emergency" },
                    label = { Text("Helpline (112)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Contacts List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredContacts, key = { it.id }) { contact ->
                    val color = try {
                        Color(android.graphics.Color.parseColor(contact.colorHex))
                    } catch (e: Exception) {
                        Color(0xFFD32F2F)
                    }

                    val icon = getIconForContact(contact.iconName, contact.category)

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                                context.startActivity(intent)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(color.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = contact.title,
                                        tint = color,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = contact.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (contact.isCustomAdded) {
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFF0F2027)
                                            ) {
                                                Text(
                                                    text = "Added",
                                                    fontSize = 8.sp,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = contact.description,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = contact.phone,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = color
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        editingContactId = contact.id
                                        newTitle = contact.title
                                        newPhone = contact.phone
                                        newDescription = contact.description
                                        newCategory = contact.category
                                        showAddContactSheet = true
                                    },
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Surface(
                                    shape = CircleShape,
                                    color = color,
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Call,
                                            contentDescription = "Call",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                if (contact.isCustomAdded && viewModel != null) {
                                    Spacer(modifier = Modifier.width(2.dp))
                                    IconButton(
                                        onClick = { viewModel.deleteEmergencyContact(contact.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Red,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun getIconForContact(iconName: String, category: String): ImageVector {
    return when (iconName) {
        "Person" -> Icons.Default.Person
        "Security" -> Icons.Default.Security
        "Business" -> Icons.Default.Business
        "Plumbing" -> Icons.Default.Plumbing
        "FlashOn" -> Icons.Default.FlashOn
        "PhoneInTalk" -> Icons.Default.PhoneInTalk
        "LocalPolice" -> Icons.Default.LocalPolice
        "LocalHospital" -> Icons.Default.LocalHospital
        "LocalFireDepartment" -> Icons.Default.LocalFireDepartment
        "Shield" -> Icons.Default.Shield
        else -> if (category == "Colony Incharge & Society") Icons.Default.Person else Icons.Default.Warning
    }
}
