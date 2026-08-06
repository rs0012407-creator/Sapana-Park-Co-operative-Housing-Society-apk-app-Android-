package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole(val label: String) {
    MEMBER("Flat Owner / Member"),
    TENANT("Registered Tenant"),
    SHOP_OWNER("Shop Owner")
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val flatNo: String, // Flat / Room No (e.g. A-304 / 304)
    val name: String,
    val role: String, // MEMBER, TENANT, SHOP_OWNER
    val wing: String = "A-Wing",
    val phone: String,
    val email: String = "",
    val floorNo: String = "3rd Floor",
    val roomNo: String = "304",
    val emergencyContact: String = "+91 98765 00000 (Ramesh Sharma)",
    val permanentAddress: String = "Katraj, Pune, Maharashtra - 411046",
    val bloodGroup: String = "O+ Positive",
    val policeVerificationStatus: String = "Completed & Uploaded",
    val moveInDate: String = "15 Jan 2021",
    val passwordHash: String = "123456",
    val isGoogleAccount: Boolean = false,
    val isPromotionAllowed: Boolean = true,
    val referralCode: String = "SAPANA50-RAJESH",
    val referralWalletBalance: Double = 150.0,
    val totalReferralsCount: Int = 3,
    val profilePhotoUri: String? = null,
    val alternatePhone: String = "+91 98123 45678",
    val aadhaarNumber: String = "XXXX-XXXX-4921",
    val panNumber: String = "ABCDE1234F",
    val voterIdNumber: String = "WZP9876543"
)

data class UserSession(
    val name: String = "Rajesh Sharma",
    val role: UserRole = UserRole.MEMBER,
    val flatNo: String = "A-304",
    val wing: String = "A-Wing",
    val phone: String = "+91 98765 43210",
    val email: String = "rajesh.sharma@sapanapark.org",
    val floorNo: String = "3rd Floor",
    val roomNo: String = "304",
    val emergencyContact: String = "+91 98765 00000 (Ramesh Sharma)",
    val permanentAddress: String = "Katraj, Pune, Maharashtra - 411046",
    val bloodGroup: String = "O+ Positive",
    val policeVerificationStatus: String = "Completed & Uploaded",
    val moveInDate: String = "15 Jan 2021",
    val isLoggedIn: Boolean = true,
    val isGoogleAccount: Boolean = false,
    val preferredLanguage: String = "English", // English, Marathi, Hindi
    val isPromotionAllowed: Boolean = true,
    val referralCode: String = "SAPANA50-RAJESH",
    val referralWalletBalance: Double = 150.0,
    val totalReferralsCount: Int = 3,
    val profilePhotoUri: String? = null,
    val alternatePhone: String = "+91 98123 45678",
    val aadhaarNumber: String = "XXXX-XXXX-4921",
    val panNumber: String = "ABCDE1234F",
    val voterIdNumber: String = "WZP9876543"
)

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val phone: String,
    val description: String,
    val category: String = "Colony Incharge & Society", // "Colony Incharge & Society" or "National Emergency"
    val iconName: String = "Security",
    val colorHex: String = "#0288D1",
    val isCustomAdded: Boolean = false
)

@Entity(tableName = "resident_documents")
data class ResidentDocumentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val documentType: String, // Aadhaar Card, PAN Card, Rent Agreement, Police Verification, Possession Letter, Voter ID
    val documentNumber: String,
    val holderName: String,
    val photoUri: String? = null,
    val status: String = "Pending Verification", // Verified, Pending Verification, Needs Re-upload
    val dateUploaded: String,
    val notes: String = ""
)

@Entity(tableName = "notices")
data class NoticeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // General, Maintenance, AGM, Emergency, Bye-Laws
    val date: String,
    val description: String,
    val isImportant: Boolean = false,
    val documentRef: String? = null
)

@Entity(tableName = "complaints")
data class ComplaintEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val complaintNumber: String,
    val category: String, // Plumbing, Electrical, Security, Cleanliness, Lift, Parking, Water
    val title: String,
    val description: String,
    val flatNo: String,
    val priority: String, // Low, Medium, High, Critical
    val status: String, // Submitted, In Progress, Resolved, Closed
    val dateCreated: String,
    val estimatedResolution: String = "24-48 Hours",
    val assignedTechnician: String = "Society Maintenance Staff",
    val feedbackRating: Int = 0,
    val feedbackComment: String = "",
    val photoUri: String? = null
)

@Entity(tableName = "bills")
data class MaintenanceBillEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val billNumber: String,
    val monthYear: String,
    val flatNo: String,
    val maintenanceCharge: Double,
    val waterCharges: Double,
    val parkingFee: Double,
    val festivalContribution: Double,
    val totalAmount: Double,
    val dueDate: String,
    val isPaid: Boolean = false,
    val paidDate: String? = null,
    val receiptNumber: String? = null,
    val paymentMethod: String? = null
)

@Entity(tableName = "events")
data class SocietyEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // Health Camp, Career Guidance, Women Empowerment, Festival, General
    val date: String,
    val time: String,
    val venue: String,
    val description: String,
    val organizer: String,
    val isRegistered: Boolean = false,
    val registrationCount: Int = 25,
    val posterUri: String? = null,
    val locationName: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val contactPhone: String = "+91 98765 43210",
    val isStopped: Boolean = false
)

data class DocumentItem(
    val id: Int,
    val title: String,
    val category: String, // Bye-Laws, AGM Minutes, Audit Reports, NOC Forms, Applications
    val date: String,
    val fileSize: String,
    val fileType: String = "PDF",
    val description: String = ""
)

data class CommitteeMember(
    val id: Int,
    val name: String,
    val designation: String, // Chairman, Secretary, Treasurer, Committee Member
    val wingFlat: String,
    val phone: String,
    val email: String,
    val availability: String = "Mon-Sat: 6 PM - 8 PM"
)

@Entity(tableName = "family_members")
data class FamilyMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val relation: String,
    val age: Int,
    val phone: String = ""
)

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleType: String, // 4-Wheeler, 2-Wheeler, EV
    val registrationNo: String,
    val makeModel: String,
    val parkingBay: String
)

@Entity(tableName = "suggestions")
data class SuggestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val date: String,
    val status: String = "Under Review"
)

data class AchievementItem(
    val id: Int,
    val title: String,
    val nomineeName: String,
    val flatNo: String,
    val category: String,
    val description: String,
    val date: String
)

data class GalleryPhotoItem(
    val id: Int,
    val title: String,
    val eventName: String,
    val date: String,
    val tag: String
)

@Entity(tableName = "utility_payments")
data class UtilityPaymentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val transactionId: String,
    val userFlatNo: String,
    val userName: String,
    val billType: String, // Electricity Bill, House Rent, Water Charges, Society Maintenance, Gas/LPG, Broadband, Other Bill
    val billerName: String,
    val consumerNumber: String,
    val amount: Double,
    val paymentDate: String,
    val paymentMode: String, // e.g. PhonePe UPI, GPay UPI, SBI NetBanking, Card
    val status: String = "SUCCESSFUL",
    val receiptNumber: String
)

@Entity(tableName = "meetings")
data class SocietyMeetingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val purposeReason: String, // Meeting kis liye li ja rahi hai (Objective / Problem statement)
    val category: String = "General Body", // Water & Utility, Security, Emergency, Financial, Celebration
    val date: String,
    val time: String,
    val venue: String,
    val organizer: String = "Managing Committee",
    val agendaDetails: String = "",
    val isAttending: Boolean = false,
    val attendeeCount: Int = 18
)

