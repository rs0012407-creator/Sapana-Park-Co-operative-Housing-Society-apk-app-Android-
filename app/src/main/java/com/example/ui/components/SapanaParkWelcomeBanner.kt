package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SapanaParkWelcomeBanner(
    modifier: Modifier = Modifier,
    residentName: String? = null,
    wingRoomInfo: String? = null,
    showSubtext: Boolean = true
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("sapana_park_welcome_banner")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF4FBF7), // Warm soft cream green
                            Color(0xFFE8F5E9), // Light leaf tint
                            Color(0xFFE0F2F1)  // Soft teal tint
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    color = Color(0xFF00695C).copy(alpha = 0.3f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 20.dp, vertical = 22.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Top Script Header: "Welcome to"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(1.dp)
                            .background(Color(0xFF004D40))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Welcome to",
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        color = Color(0xFF004D40),
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(1.dp)
                            .background(Color(0xFF004D40))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Circular Emblem / Logo Graphic Box
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFFFFFFF),
                                    Color(0xFFE0F2F1),
                                    Color(0xFF004D40)
                                )
                            )
                        )
                        .border(3.dp, Color(0xFFFFB300), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Apartment,
                                contentDescription = "Buildings",
                                tint = Color(0xFFFF8F00), // Golden Buildings
                                modifier = Modifier.size(38.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = "SAPANA",
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            color = Color(0xFF004D40),
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Center Bold Brand Name: "SAPANA PARK"
                Text(
                    text = "SAPANA PARK",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = Color(0xFF004D40),
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center
                )

                // Subtitle divider tagline: "TOGETHER WE BUILD BETTER LIVING"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(1.dp)
                            .background(Color(0xFFFF8F00))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "TOGETHER WE BUILD BETTER LIVING",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .width(20.dp)
                            .height(1.dp)
                            .background(Color(0xFFFF8F00))
                    )
                }

                Text(
                    text = "साथ मिलकर बनाएं बेहतर जीवन",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Dark Teal Ribbon Badge: "COLONY"
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF004D40),
                    shadowElevation = 4.dp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COLONY",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 4.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Optional Resident Personalized Greeting inside Banner
                if (!residentName.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.9f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF00695C).copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.HomeWork,
                                contentDescription = null,
                                tint = Color(0xFF00695C),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Namaste, $residentName!",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF004D40)
                                )
                                if (!wingRoomInfo.isNullOrBlank()) {
                                    Text(
                                        text = wingRoomInfo,
                                        fontSize = 11.sp,
                                        color = Color(0xFF00695C)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
