package com.example.ui.screens.auth

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.components.SapanaParkWelcomeBanner
import com.example.viewmodel.SocietyViewModel

enum class AuthMode {
    ONBOARDING, LOGIN, REGISTER, FORGOT_PASSWORD
}

@Composable
fun AuthScreen(
    viewModel: SocietyViewModel,
    onLoginSuccess: () -> Unit
) {
    var mode by remember { mutableStateOf(AuthMode.ONBOARDING) }

    // Login Form States
    var loginFlatOrShop by remember { mutableStateOf("A-304") }
    var loginPhone by remember { mutableStateOf("9876543210") }
    var loginPassword by remember { mutableStateOf("123456") }
    var isOtpLogin by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.MEMBER) }

    // Register Form States
    var regName by remember { mutableStateOf("") }
    var regRole by remember { mutableStateOf(UserRole.MEMBER) }
    var regFlatNo by remember { mutableStateOf("") }
    var regPhone by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }

    // Forgot Password States
    var forgotEmail by remember { mutableStateOf("rajbhansingh467@gmail.com") }
    var forgotFlat by remember { mutableStateOf("") }
    var forgotPhone by remember { mutableStateOf("") }
    var resetEmailSent by remember { mutableStateOf(false) }
    var isCreatingNewPassword by remember { mutableStateOf(false) }
    var newPasswordInput by remember { mutableStateOf("") }
    var confirmNewPasswordInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Logo Badge
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.15f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Apartment,
                        contentDescription = "Logo",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Sapana Park CHS",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = Color.White,
                letterSpacing = 0.5.sp
            )

            Text(
                text = "Co-operative Housing Society Management",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Main Card Container
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (mode) {
                        AuthMode.ONBOARDING -> {
                            SapanaParkWelcomeBanner(
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Text(
                                text = "Welcome to Your Digital Society Hub",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OnboardingFeatureRow("Instant Maintenance Dues & Digital Receipts")
                            OnboardingFeatureRow("1-Click Complaint & Maintenance Tracking")
                            OnboardingFeatureRow("Society Bye-Laws, AGM Minutes & NOC Requests")
                            OnboardingFeatureRow("Community Events, Health Camps & Emergency Contacts")

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { mode = AuthMode.LOGIN },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("onboarding_login_btn"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0F2027)
                                )
                            ) {
                                Text("Login to Your Flat / Shop", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = { mode = AuthMode.REGISTER },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("onboarding_register_btn"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Register New Member / Tenant", fontSize = 14.sp)
                            }
                        }

                        AuthMode.LOGIN -> {
                            Text(
                                text = "Resident & Shop Login",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Role Selection Chips
                            Text(
                                text = "I am a:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.align(Alignment.Start)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = selectedRole == UserRole.MEMBER,
                                    onClick = { selectedRole = UserRole.MEMBER },
                                    label = { Text("Member", fontSize = 12.sp) },
                                    leadingIcon = { Icon(Icons.Default.Home, null, modifier = Modifier.size(16.dp)) },
                                    modifier = Modifier.weight(1f)
                                )
                                FilterChip(
                                    selected = selectedRole == UserRole.TENANT,
                                    onClick = { selectedRole = UserRole.TENANT },
                                    label = { Text("Tenant", fontSize = 12.sp) },
                                    leadingIcon = { Icon(Icons.Default.Person, null, modifier = Modifier.size(16.dp)) },
                                    modifier = Modifier.weight(1f)
                                )
                                FilterChip(
                                    selected = selectedRole == UserRole.SHOP_OWNER,
                                    onClick = { selectedRole = UserRole.SHOP_OWNER },
                                    label = { Text("Shop", fontSize = 12.sp) },
                                    leadingIcon = { Icon(Icons.Default.Store, null, modifier = Modifier.size(16.dp)) },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = loginFlatOrShop,
                                onValueChange = { loginFlatOrShop = it },
                                label = { Text(if (selectedRole == UserRole.SHOP_OWNER) "Shop No (e.g. Shop-04)" else "Flat No (e.g. A-304)") },
                                leadingIcon = { Icon(if (selectedRole == UserRole.SHOP_OWNER) Icons.Default.Store else Icons.Default.Home, null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("flat_number_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = loginPhone,
                                onValueChange = { loginPhone = it },
                                label = { Text("Mobile Number") },
                                leadingIcon = { Icon(Icons.Default.Phone, null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("phone_number_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            if (!isOtpLogin) {
                                OutlinedTextField(
                                    value = loginPassword,
                                    onValueChange = { loginPassword = it },
                                    label = { Text("Password") },
                                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                                    visualTransformation = PasswordVisualTransformation(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("password_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = otpCode,
                                        onValueChange = { otpCode = it },
                                        label = { Text("Enter OTP") },
                                        leadingIcon = { Icon(Icons.Default.Security, null) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("otp_input"),
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    Button(
                                        onClick = {
                                            otpSent = true
                                            viewModel.showMessage("OTP 4829 sent to $loginPhone")
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(if (otpSent) "Resend" else "Get OTP", fontSize = 12.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { isOtpLogin = !isOtpLogin }) {
                                    Text(
                                        text = if (isOtpLogin) "Login with Password" else "Login via OTP",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                TextButton(onClick = { mode = AuthMode.FORGOT_PASSWORD }) {
                                    Text("Forgot Password?", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    viewModel.login(loginFlatOrShop, loginPhone, selectedRole)
                                    onLoginSuccess()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("submit_login_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0F2027)
                                )
                            ) {
                                Text("Login Now", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            TextButton(onClick = { mode = AuthMode.REGISTER }) {
                                Text("Don't have an account? Register Here", fontSize = 13.sp)
                            }
                        }

                        AuthMode.REGISTER -> {
                            var regName by remember { mutableStateOf("") }
                            var regEmail by remember { mutableStateOf("") }
                            var regPhone by remember { mutableStateOf("") }
                            var regRoomNo by remember { mutableStateOf("") }
                            var regFloorNo by remember { mutableStateOf("") }
                            var regPassword by remember { mutableStateOf("") }
                            var regPromotionAllowed by remember { mutableStateOf(true) }

                            // Sapana Park Colony Official Emblem Banner on Registration
                            SapanaParkWelcomeBanner(
                                modifier = Modifier.padding(bottom = 16.dp),
                                residentName = regName.ifBlank { null },
                                wingRoomInfo = if (regRoomNo.isNotBlank()) "Flat $regRoomNo" else null
                            )

                            Text(
                                text = "New Resident Registration (रजिस्ट्रेशन)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "ईमेल या मोबाइल नंबर और पासवर्ड से सीधे रजिस्टर करें (No OTP required)",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // 1. Name
                            OutlinedTextField(
                                value = regName,
                                onValueChange = { regName = it },
                                label = { Text("Full Name (नाम)") },
                                leadingIcon = { Icon(Icons.Default.Person, null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_name_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 2. Mobile Number
                            OutlinedTextField(
                                value = regPhone,
                                onValueChange = { regPhone = it },
                                label = { Text("Mobile Number (मोबाइल नंबर)") },
                                leadingIcon = { Icon(Icons.Default.Phone, null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_phone_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 3. Email
                            OutlinedTextField(
                                value = regEmail,
                                onValueChange = { regEmail = it },
                                label = { Text("Email Address (ईमेल ID)") },
                                leadingIcon = { Icon(Icons.Default.Email, null) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_email_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // 4. Room Number & 5. Floor Number
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = regRoomNo,
                                    onValueChange = { regRoomNo = it },
                                    label = { Text("Room No (रूम नं)") },
                                    leadingIcon = { Icon(Icons.Default.Home, null) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("reg_room_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                OutlinedTextField(
                                    value = regFloorNo,
                                    onValueChange = { regFloorNo = it },
                                    label = { Text("Floor No (फ्लोर नं)") },
                                    leadingIcon = { Icon(Icons.Default.Business, null) },
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("reg_floor_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = regPassword,
                                onValueChange = { regPassword = it },
                                label = { Text("Create Password (पासवर्ड बनाएं)") },
                                leadingIcon = { Icon(Icons.Default.Lock, null) },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reg_password_input"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Internet Promotional Sync Capability Permission Checkbox
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.Checkbox(
                                        checked = regPromotionAllowed,
                                        onCheckedChange = { regPromotionAllowed = it },
                                        modifier = Modifier.testTag("reg_promo_checkbox")
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "Allow Promotional & Society Announcements over Internet",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "इंटरनेट कनेक्शन से प्रमोशन एवं ऑफर अपडेट्स की अनुमति दें",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Direct Registration Button (No OTP!)
                            Button(
                                onClick = {
                                    if (regName.isNotBlank() || regPhone.isNotBlank() || regEmail.isNotBlank()) {
                                        viewModel.register(
                                            name = regName,
                                            email = regEmail,
                                            phone = regPhone,
                                            roomNo = regRoomNo,
                                            floorNo = regFloorNo
                                        )
                                        viewModel.togglePromotionPermission(regPromotionAllowed)
                                        onLoginSuccess()
                                    } else {
                                        viewModel.showMessage("Please enter registration details.")
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("submit_register_btn"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0F2027)
                                )
                            ) {
                                Text("Register Directly (बिना OTP रजिस्टर करें)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(onClick = { mode = AuthMode.LOGIN }) {
                                    Text("Already registered? Login", fontSize = 12.sp)
                                }
                                TextButton(onClick = { mode = AuthMode.FORGOT_PASSWORD }) {
                                    Text("Forgot Password? (पासवर्ड भूल गए?)", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }

                        AuthMode.FORGOT_PASSWORD -> {
                            if (!isCreatingNewPassword) {
                                Text(
                                    text = "Reset Password via Email (जीमेल से पासवर्ड रिसेट)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "अपना रजिस्टर्ड जीमेल या फोन नंबर दर्ज करके पासवर्ड रिसेट लिंक प्राप्त करें",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = forgotEmail,
                                    onValueChange = { forgotEmail = it },
                                    label = { Text("Registered Gmail / Email (जीमेल आईडी)*") },
                                    leadingIcon = { Icon(Icons.Default.Email, null) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("forgot_email_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = forgotPhone,
                                    onValueChange = { forgotPhone = it },
                                    label = { Text("Mobile Number or Flat No (ऑप्शनल)") },
                                    leadingIcon = { Icon(Icons.Default.Phone, null) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("forgot_phone_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (forgotEmail.isNotBlank()) {
                                            resetEmailSent = true
                                            viewModel.showMessage("Password reset link generated for $forgotEmail! Click the email notification below. ✓")
                                        } else {
                                            viewModel.showMessage("कृपया जीमेल आईडी भरें")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("send_reset_email_btn"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                                ) {
                                    Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Send Reset Link to Email (लिंक भेजें)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }

                                if (resetEmailSent) {
                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Simulated Gmail Reset Password Notification Inbox Card
                                    Card(
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("gmail_notification_card")
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Surface(
                                                        shape = CircleShape,
                                                        color = Color(0xFFEA4335),
                                                        modifier = Modifier.size(28.dp)
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Text("M", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                                        }
                                                    }
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text(
                                                        text = "New Gmail Notification",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.sp,
                                                        color = Color(0xFF1E293B)
                                                    )
                                                }

                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = Color(0xFF10B981)
                                                ) {
                                                    Text(
                                                        text = "INBOX",
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "Subject: Reset Password for Sapana Park CHS Account",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = Color(0xFF0F172A)
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "To: $forgotEmail\nClick below to create a new password for your account.",
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )

                                            Spacer(modifier = Modifier.height(12.dp))

                                            // Interactive Reset Link inside Gmail Notification
                                            Button(
                                                onClick = { isCreatingNewPassword = true },
                                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E40AF)),
                                                shape = RoundedCornerShape(10.dp),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .testTag("click_gmail_reset_link_btn")
                                            ) {
                                                Text("🔗 Click Here to Reset Password (नया पासवर्ड बनाएं)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                TextButton(onClick = {
                                    mode = AuthMode.LOGIN
                                    resetEmailSent = false
                                }) {
                                    Text("Back to Login (लॉगिन पर वापस जाएं)", fontSize = 13.sp)
                                }
                            } else {
                                // Create New Password Form
                                Text(
                                    text = "Create New Password (नया पासवर्ड बनाएं)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = "Account: $forgotEmail",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                OutlinedTextField(
                                    value = newPasswordInput,
                                    onValueChange = { newPasswordInput = it },
                                    label = { Text("New Password (नया पासवर्ड)*") },
                                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                                    visualTransformation = PasswordVisualTransformation(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("new_password_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedTextField(
                                    value = confirmNewPasswordInput,
                                    onValueChange = { confirmNewPasswordInput = it },
                                    label = { Text("Confirm New Password (पासवर्ड दोबारा दर्ज करें)*") },
                                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                                    visualTransformation = PasswordVisualTransformation(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("confirm_new_password_input"),
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp)
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        if (newPasswordInput.isNotBlank() && newPasswordInput == confirmNewPasswordInput) {
                                            viewModel.resetPasswordWithEmail(forgotEmail, newPasswordInput)
                                            loginPassword = newPasswordInput
                                            mode = AuthMode.LOGIN
                                            isCreatingNewPassword = false
                                            resetEmailSent = false
                                            newPasswordInput = ""
                                            confirmNewPasswordInput = ""
                                        } else if (newPasswordInput.isBlank()) {
                                            viewModel.showMessage("कृपया नया पासवर्ड दर्ज करें")
                                        } else {
                                            viewModel.showMessage("पासवर्ड मैच नहीं हो रहे हैं, कृपया दोबारा जांचें")
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("save_new_password_btn"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save & Update Password (पासवर्ड अपडेट करें)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                TextButton(onClick = {
                                    isCreatingNewPassword = false
                                    resetEmailSent = false
                                    mode = AuthMode.LOGIN
                                }) {
                                    Text("Cancel & Back to Login", fontSize = 13.sp)
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
private fun OnboardingFeatureRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF2E7D32),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
