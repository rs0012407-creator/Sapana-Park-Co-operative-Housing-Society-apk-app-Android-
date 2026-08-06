package com.example.ui.screens.profile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.material.icons.filled.SmartToy
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.ResidentDocumentEntity
import com.example.data.model.VehicleEntity
import com.example.ui.components.AiAutoFixDialog
import com.example.ui.components.UserDataStorageCard
import com.example.viewmodel.SocietyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: SocietyViewModel
) {
    val userSession by viewModel.userSession.collectAsState()
    val familyMembers by viewModel.familyMembers.collectAsState()
    val vehicles by viewModel.vehicles.collectAsState()
    val residentDocuments by viewModel.residentDocuments.collectAsState()

    var selectedTab by remember { mutableStateOf(0) } // 0: Colony Info, 1: Documents, 2: Family, 3: Vehicles
    var showAddFamilySheet by remember { mutableStateOf(false) }
    var showAddVehicleSheet by remember { mutableStateOf(false) }
    var showAddDocumentSheet by remember { mutableStateOf(false) }
    var showEditColonyInfoSheet by remember { mutableStateOf(false) }
    var showAiAutoFixDialog by remember { mutableStateOf(false) }
    var showProfilePhotoSheet by remember { mutableStateOf(false) }

    var profileBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = androidx.compose.ui.platform.LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateProfilePhoto(it.toString())
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let {
            profileBitmap = it
            viewModel.showMessage("Profile picture captured & set! ✓")
        }
    }

    if (showAiAutoFixDialog) {
        AiAutoFixDialog(viewModel = viewModel, onDismiss = { showAiAutoFixDialog = false })
    }

    // Add Document Form State
    var docType by remember { mutableStateOf("Aadhaar Card") }
    var customDocTypeName by remember { mutableStateOf("") }
    var docNumber by remember { mutableStateOf("") }
    var docHolder by remember { mutableStateOf(userSession.name) }
    var docNotes by remember { mutableStateOf("") }
    var docPhotoUri by remember { mutableStateOf<String?>(null) }
    var docBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Edit Colony Info & Identity Proof Form State
    var editName by remember { mutableStateOf(userSession.name) }
    var editPhone by remember { mutableStateOf(userSession.phone) }
    var editAlternatePhone by remember { mutableStateOf(userSession.alternatePhone) }
    var editEmail by remember { mutableStateOf(userSession.email) }
    var editEmergencyContact by remember { mutableStateOf(userSession.emergencyContact) }
    var editPermanentAddress by remember { mutableStateOf(userSession.permanentAddress) }
    var editBloodGroup by remember { mutableStateOf(userSession.bloodGroup) }
    var editPoliceStatus by remember { mutableStateOf(userSession.policeVerificationStatus) }
    var editAadhaarNumber by remember { mutableStateOf(userSession.aadhaarNumber) }
    var editPanNumber by remember { mutableStateOf(userSession.panNumber) }
    var editVoterIdNumber by remember { mutableStateOf(userSession.voterIdNumber) }

    // Add Family Form
    var famName by remember { mutableStateOf("") }
    var famRelation by remember { mutableStateOf("Spouse") }
    var famAge by remember { mutableStateOf("") }
    var famPhone by remember { mutableStateOf("") }

    // Add Vehicle Form
    var vehType by remember { mutableStateOf("4-Wheeler") }
    var vehRegNo by remember { mutableStateOf("") }
    var vehMakeModel by remember { mutableStateOf("") }
    var vehBay by remember { mutableStateOf("Slot A-14") }

    // Camera & Gallery Launchers for Document Upload
    val docCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            docBitmap = bitmap
            docPhotoUri = "captured_doc_bitmap"
            viewModel.showMessage("Document photo captured via Camera ✓")
        }
    }

    val docGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            docPhotoUri = uri.toString()
            docBitmap = null
            viewModel.showMessage("Document photo attached from Gallery ✓")
        }
    }

    val documentTypes = listOf(
        "Aadhaar Card",
        "PAN Card",
        "Rent Agreement",
        "Police Verification",
        "Possession Letter",
        "Voter ID / DL",
        "Society NOC / Passbook",
        "Other Document"
    )

    // Add Document Sheet
    if (showAddDocumentSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showAddDocumentSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Add Colony Identity Document",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "कॉलोनी सत्यापन हेतु अपना पहचान पत्र एवं अन्य दस्तावेज़ अपलोड करें",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "Document Type (दस्तावेज़ का प्रकार):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    documentTypes.chunked(3).forEach { chunk ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            chunk.forEach { item ->
                                FilterChip(
                                    selected = docType == item,
                                    onClick = { docType = item },
                                    label = { Text(item, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                if (docType == "Other Document") {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = customDocTypeName,
                        onValueChange = { customDocTypeName = it },
                        label = { Text("Specify Other Document Name (अन्य दस्तावेज़ का नाम)") },
                        leadingIcon = { Icon(Icons.Default.Description, null) },
                        modifier = Modifier.fillMaxWidth().testTag("custom_doc_type_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = docNumber,
                    onValueChange = { docNumber = it },
                    label = { Text("Document ID / Card Number (संख्या)") },
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    modifier = Modifier.fillMaxWidth().testTag("doc_number_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = docHolder,
                    onValueChange = { docHolder = it },
                    label = { Text("Holder Name (दस्तावेज़ धारक का नाम)") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth().testTag("doc_holder_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = docNotes,
                    onValueChange = { docNotes = it },
                    label = { Text("Notes / Issuing Authority (वैकल्पिक विवरण)") },
                    modifier = Modifier.fillMaxWidth().testTag("doc_notes_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Attach Document Photo / Scan (कैमरा या गैलरी से फोटो जोड़ें):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { docCameraLauncher.launch() },
                        modifier = Modifier.weight(1f).testTag("doc_camera_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Camera", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { docGalleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f).testTag("doc_gallery_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gallery", fontSize = 12.sp)
                    }
                }

                if (docPhotoUri != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black.copy(alpha = 0.05f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (docBitmap != null) {
                            Image(
                                bitmap = docBitmap!!.asImageBitmap(),
                                contentDescription = "Captured Doc",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (docPhotoUri != "captured_doc_bitmap") {
                            AsyncImage(
                                model = docPhotoUri,
                                contentDescription = "Gallery Doc",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        IconButton(
                            onClick = {
                                docPhotoUri = null
                                docBitmap = null
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

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        val finalType = if (docType == "Other Document" && customDocTypeName.isNotBlank()) customDocTypeName else docType
                        if (finalType.isNotBlank()) {
                            viewModel.addResidentDocument(
                                type = finalType,
                                number = docNumber,
                                holder = docHolder,
                                photoUri = docPhotoUri,
                                notes = docNotes
                            )
                            showAddDocumentSheet = false
                            docNumber = ""
                            docNotes = ""
                            customDocTypeName = ""
                            docPhotoUri = null
                            docBitmap = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027))
                ) {
                    Text("Submit Document for Verification", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Edit Colony Info & ID Numbers Sheet
    if (showEditColonyInfoSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showEditColonyInfoSheet = false },
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
                            text = "Edit Resident Details & Government IDs",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "नंबर, अल्टरनेट मोबाइल, आधार, पैन एवं पहचान पत्र प्रबंधित करें",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { showEditColonyInfoSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text("Personal & Contact Numbers (संपर्क नंबर)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editName,
                    onValueChange = { editName = it },
                    label = { Text("Resident Full Name (नाम)") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    modifier = Modifier.fillMaxWidth().testTag("edit_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editPhone,
                    onValueChange = { editPhone = it },
                    label = { Text("Primary Phone / Mobile Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    trailingIcon = {
                        if (editPhone.isNotEmpty()) {
                            IconButton(onClick = {
                                editPhone = ""
                                viewModel.deleteProfileField("phone", "Primary Phone Number")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_phone_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editAlternatePhone,
                    onValueChange = { editAlternatePhone = it },
                    label = { Text("Alternate / WhatsApp Mobile Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    trailingIcon = {
                        if (editAlternatePhone.isNotEmpty()) {
                            IconButton(onClick = {
                                editAlternatePhone = ""
                                viewModel.deleteProfileField("alternatePhone", "Alternate Phone Number")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_alt_phone_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editEmail,
                    onValueChange = { editEmail = it },
                    label = { Text("Email Address (ईमेल)") },
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    trailingIcon = {
                        if (editEmail.isNotEmpty()) {
                            IconButton(onClick = {
                                editEmail = ""
                                viewModel.deleteProfileField("email", "Email Address")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_email_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editEmergencyContact,
                    onValueChange = { editEmergencyContact = it },
                    label = { Text("Emergency Contact Person & Phone") },
                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                    trailingIcon = {
                        if (editEmergencyContact.isNotEmpty()) {
                            IconButton(onClick = {
                                editEmergencyContact = ""
                                viewModel.deleteProfileField("emergencyContact", "Emergency Contact")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_emergency_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editPermanentAddress,
                    onValueChange = { editPermanentAddress = it },
                    label = { Text("Permanent Native Address (स्थाई पता)") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                    trailingIcon = {
                        if (editPermanentAddress.isNotEmpty()) {
                            IconButton(onClick = {
                                editPermanentAddress = ""
                                viewModel.deleteProfileField("permanentAddress", "Permanent Address")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_address_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(14.dp))

                Text("Government Identity Cards & Numbers (आधार, पैन, वोटर ID)", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editAadhaarNumber,
                    onValueChange = { editAadhaarNumber = it },
                    label = { Text("Aadhaar Card Number (आधार नंबर)") },
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    trailingIcon = {
                        if (editAadhaarNumber.isNotEmpty()) {
                            IconButton(onClick = {
                                editAadhaarNumber = ""
                                viewModel.deleteProfileField("aadhaarNumber", "Aadhaar Card Number")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_aadhaar_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editPanNumber,
                    onValueChange = { editPanNumber = it },
                    label = { Text("PAN Card Number (पैन कार्ड नंबर)") },
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    trailingIcon = {
                        if (editPanNumber.isNotEmpty()) {
                            IconButton(onClick = {
                                editPanNumber = ""
                                viewModel.deleteProfileField("panNumber", "PAN Card Number")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_pan_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editVoterIdNumber,
                    onValueChange = { editVoterIdNumber = it },
                    label = { Text("Voter ID / Driving License Number") },
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    trailingIcon = {
                        if (editVoterIdNumber.isNotEmpty()) {
                            IconButton(onClick = {
                                editVoterIdNumber = ""
                                viewModel.deleteProfileField("voterIdNumber", "Voter ID / DL Number")
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("edit_voter_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editBloodGroup,
                    onValueChange = { editBloodGroup = it },
                    label = { Text("Blood Group (ब्लड ग्रुप)") },
                    leadingIcon = { Icon(Icons.Default.MedicalInformation, null) },
                    modifier = Modifier.fillMaxWidth().testTag("edit_blood_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = editPoliceStatus,
                    onValueChange = { editPoliceStatus = it },
                    label = { Text("Police Verification Status") },
                    leadingIcon = { Icon(Icons.Default.Policy, null) },
                    modifier = Modifier.fillMaxWidth().testTag("edit_police_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        viewModel.updateFullProfileDetails(
                            name = editName,
                            phone = editPhone,
                            alternatePhone = editAlternatePhone,
                            email = editEmail,
                            emergencyContact = editEmergencyContact,
                            permanentAddress = editPermanentAddress,
                            bloodGroup = editBloodGroup,
                            policeStatus = editPoliceStatus,
                            aadhaarNumber = editAadhaarNumber,
                            panNumber = editPanNumber,
                            voterIdNumber = editVoterIdNumber
                        )
                        showEditColonyInfoSheet = false
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027))
                ) {
                    Text("Save All Details & ID Numbers", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showAddFamilySheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showAddFamilySheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "Add Family Member", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = famName, onValueChange = { famName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = famRelation, onValueChange = { famRelation = it }, label = { Text("Relation (Spouse, Son, Daughter, Parent)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = famAge, onValueChange = { famAge = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = famPhone, onValueChange = { famPhone = it }, label = { Text("Mobile Number (Optional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (famName.isNotBlank()) {
                            viewModel.addFamilyMember(famName, famRelation, famAge, famPhone)
                            showAddFamilySheet = false
                            famName = ""
                            famPhone = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add Member", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showAddVehicleSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showAddVehicleSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(text = "Register Vehicle & Parking Bay", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(value = vehType, onValueChange = { vehType = it }, label = { Text("Vehicle Type (4-Wheeler / 2-Wheeler / EV)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = vehRegNo, onValueChange = { vehRegNo = it }, label = { Text("Registration No (e.g. MH-12-AB-1234)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = vehMakeModel, onValueChange = { vehMakeModel = it }, label = { Text("Make & Color (e.g. Creta White)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = vehBay, onValueChange = { vehBay = it }, label = { Text("Allotted Parking Bay Slot") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (vehRegNo.isNotBlank()) {
                            viewModel.addVehicle(vehType, vehRegNo, vehMakeModel, vehBay)
                            showAddVehicleSheet = false
                            vehRegNo = ""
                            vehMakeModel = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Register Vehicle", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Profile Photo Picker Options Sheet
    if (showProfilePhotoSheet) {
        val sheetState = rememberModalBottomSheetState()
        ModalBottomSheet(
            onDismissRequest = { showProfilePhotoSheet = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Update Profile Photo (प्रोफाइल फोटो बदलें)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "अपनी फोटो गैलरी से चुनें या कैमरे से फोटो खींचें",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        onClick = {
                            showProfilePhotoSheet = false
                            galleryLauncher.launch("image/*")
                        },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier.weight(1f).testTag("pick_gallery_photo_btn")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Gallery (गैलरी)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Card(
                        onClick = {
                            showProfilePhotoSheet = false
                            cameraLauncher.launch()
                        },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.weight(1f).testTag("take_camera_photo_btn")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Camera (कैमरा)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen")
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0F2027))
                .padding(20.dp)
        ) {
            Column {
                // Top row with Title
                Text(
                    text = "My Resident Profile",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar Box with photo or custom photo or fallback icon + camera overlay
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { showProfilePhotoSheet = true }
                            .testTag("profile_photo_avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileBitmap != null) {
                            Image(
                                bitmap = profileBitmap!!.asImageBitmap(),
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (!userSession.profilePhotoUri.isNullOrBlank()) {
                            AsyncImage(
                                model = userSession.profilePhotoUri,
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        // Camera Badge Overlay
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2563EB))
                                .align(Alignment.BottomEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Add Photo",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = userSession.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Text(
                            text = "${userSession.wing} • Room ${userSession.roomNo} (${userSession.floorNo})",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFFFD700)
                            ) {
                                Text(
                                    text = userSession.role.label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF2E7D32)
                            ) {
                                Text(
                                    text = "Verified Resident ✓",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        ScrollableTabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Colony Info") })
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Identity Docs (${residentDocuments.size})") })
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Family (${familyMembers.size})") })
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Vehicles (${vehicles.size})") })
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> {
                // Tab 0: Colony & Flat Info
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Colony Resident Records", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        OutlinedButton(
                            onClick = {
                                editName = userSession.name
                                editPhone = userSession.phone
                                editAlternatePhone = userSession.alternatePhone
                                editEmail = userSession.email
                                editEmergencyContact = userSession.emergencyContact
                                editPermanentAddress = userSession.permanentAddress
                                editBloodGroup = userSession.bloodGroup
                                editPoliceStatus = userSession.policeVerificationStatus
                                editAadhaarNumber = userSession.aadhaarNumber
                                editPanNumber = userSession.panNumber
                                editVoterIdNumber = userSession.voterIdNumber
                                showEditColonyInfoSheet = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("edit_colony_info_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit All Details", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Card 1: Flat & Contact Details
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(text = "Flat & Contact Numbers", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(10.dp))

                            ProfileInfoRow(Icons.Default.Home, "Wing, Floor & Room No:", "${userSession.wing}, ${userSession.floorNo}, Room ${userSession.roomNo}")
                            ProfileInfoRow(
                                icon = Icons.Default.Phone,
                                label = "Primary Mobile Number:",
                                value = userSession.phone,
                                onDeleteClick = { viewModel.deleteProfileField("phone", "Primary Mobile Number") }
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.Phone,
                                label = "Alternate / WhatsApp Number:",
                                value = userSession.alternatePhone,
                                onDeleteClick = { viewModel.deleteProfileField("alternatePhone", "Alternate Mobile Number") }
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.Email,
                                label = "Email Address:",
                                value = userSession.email,
                                onDeleteClick = { viewModel.deleteProfileField("email", "Email Address") }
                            )
                            ProfileInfoRow(Icons.Default.Apartment, "Colony / Society Name:", "Sapana Park CHS Ltd., Katraj, Pune")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Card 2: Government Identity Proof Cards & Numbers
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Government Identity Proof Numbers",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "Aadhaar / PAN / ID",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            ProfileInfoRow(
                                icon = Icons.Default.Badge,
                                label = "Aadhaar Card Number (आधार नंबर):",
                                value = userSession.aadhaarNumber,
                                onDeleteClick = { viewModel.deleteProfileField("aadhaarNumber", "Aadhaar Card Number") }
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.Badge,
                                label = "PAN Card Number (पैन कार्ड नंबर):",
                                value = userSession.panNumber,
                                onDeleteClick = { viewModel.deleteProfileField("panNumber", "PAN Card Number") }
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.Badge,
                                label = "Voter ID / Driving License (वोटर ID / DL):",
                                value = userSession.voterIdNumber,
                                onDeleteClick = { viewModel.deleteProfileField("voterIdNumber", "Voter ID / DL") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Card 3: Required Colony Profile Information
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(text = "Colony Verification & Emergency Details", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(10.dp))

                            ProfileInfoRow(
                                icon = Icons.Default.Phone,
                                label = "Emergency Contact:",
                                value = userSession.emergencyContact,
                                onDeleteClick = { viewModel.deleteProfileField("emergencyContact", "Emergency Contact") }
                            )
                            ProfileInfoRow(
                                icon = Icons.Default.LocationOn,
                                label = "Permanent Native Address:",
                                value = userSession.permanentAddress,
                                onDeleteClick = { viewModel.deleteProfileField("permanentAddress", "Permanent Address") }
                            )
                            ProfileInfoRow(Icons.Default.MedicalInformation, "Blood Group:", userSession.bloodGroup)
                            ProfileInfoRow(Icons.Default.Policy, "Police Verification Status:", userSession.policeVerificationStatus)
                            ProfileInfoRow(Icons.Default.Security, "Colony Move-In Date:", userSession.moveInDate)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Persistent Data Storage Metrics & Status
                    UserDataStorageCard(viewModel = viewModel)

                    Spacer(modifier = Modifier.height(14.dp))

                    // AI Auto-Fix Assistant Action Card
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAiAutoFixDialog = true }
                            .testTag("ai_autofix_profile_card")
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF8B5CF6).copy(alpha = 0.25f),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color(0xFFA78BFA), modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("AI Auto-Fix & Issue Resolver", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                                    Text("Report app issue or bill error for instant AI fix", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                                }
                            }
                            Button(
                                onClick = { showAiAutoFixDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Auto-Fix", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            1 -> {
                // Tab 1: Identity Documents & Verification
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Identity & Colony Documents", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "पहचान पत्र एवं सोसाइटी रिकॉर्ड्स", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            onClick = { showAddDocumentSheet = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2027)),
                            modifier = Modifier.testTag("add_doc_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Document", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (residentDocuments.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("No Identity Documents Added Yet", fontWeight = FontWeight.Bold, color = Color.Gray)
                                Text("Tap 'Add Document' to upload Aadhaar, PAN, or Rent Agreement", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(residentDocuments) { doc ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Surface(
                                                    shape = CircleShape,
                                                    color = MaterialTheme.colorScheme.primaryContainer,
                                                    modifier = Modifier.size(36.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(text = doc.documentType, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                                    Text(text = "ID: ${doc.documentNumber}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                                }
                                            }

                                            IconButton(onClick = { viewModel.deleteResidentDocument(doc.id) }) {
                                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "Holder: ${doc.holderName}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)

                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (doc.status.contains("Verified")) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = if (doc.status.contains("Verified")) Icons.Default.Verified else Icons.Default.Policy,
                                                        contentDescription = null,
                                                        tint = if (doc.status.contains("Verified")) Color(0xFF2E7D32) else Color(0xFFE65100),
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = doc.status,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (doc.status.contains("Verified")) Color(0xFF2E7D32) else Color(0xFFE65100)
                                                    )
                                                }
                                            }
                                        }

                                        if (doc.notes.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(text = "Notes: ${doc.notes}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }

                                        // Photo Evidence Attached
                                        if (!doc.photoUri.isNullOrBlank()) {
                                            Spacer(modifier = Modifier.height(10.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(130.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color.Black.copy(alpha = 0.04f))
                                            ) {
                                                AsyncImage(
                                                    model = doc.photoUri,
                                                    contentDescription = "Document Scan",
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentScale = ContentScale.Crop
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(bottomEnd = 8.dp),
                                                    color = Color.Black.copy(alpha = 0.65f),
                                                    modifier = Modifier.align(Alignment.TopStart)
                                                ) {
                                                    Text(
                                                        text = "Document Scan Attached ✓",
                                                        fontSize = 10.sp,
                                                        color = Color.White,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "Uploaded: ${doc.dateUploaded}", fontSize = 10.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Tab 2: Family Members
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Registered Family Members", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Button(
                            onClick = { showAddFamilySheet = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Member", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(familyMembers) { member ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = member.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = "${member.relation} • Age: ${member.age} yrs", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        if (member.phone.isNotBlank()) {
                                            Text(text = member.phone, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deleteFamilyMember(member.id) }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Tab 3: Vehicles
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Vehicles & Allotted Parking", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Button(
                            onClick = { showAddVehicleSheet = true },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Vehicle", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(vehicles) { veh ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (veh.vehicleType.contains("2")) Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(28.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(text = veh.registrationNo, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                            Text(text = "${veh.makeModel} • ${veh.vehicleType}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text(text = "Allotted Bay: ${veh.parkingBay}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deleteVehicle(veh.id) }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onDeleteClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = if (value.isNotBlank()) value else "Not Added (जोड़ें)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (value.isNotBlank()) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
        }
        if (onDeleteClick != null && value.isNotBlank()) {
            IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Detail",
                    tint = Color.Red.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
