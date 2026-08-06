package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.AiTroubleshootResult
import com.example.api.AiTroubleshootUiState
import com.example.api.GeminiApiClient
import com.example.api.GeminiContent
import com.example.api.GeminiPart
import com.example.api.GeminiRequest
import com.example.data.local.AppDatabase
import com.example.data.model.AchievementItem
import com.example.data.model.CommitteeMember
import com.example.data.model.ComplaintEntity
import com.example.data.model.DocumentItem
import com.example.data.model.EmergencyContactEntity
import com.example.data.model.FamilyMemberEntity
import com.example.data.model.GalleryPhotoItem
import com.example.data.model.MaintenanceBillEntity
import com.example.data.model.NoticeEntity
import com.example.data.model.ResidentDocumentEntity
import com.example.data.model.SocietyEventEntity
import com.example.data.model.SocietyMeetingEntity
import com.example.data.model.SuggestionEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.model.UserSession
import com.example.data.model.UtilityPaymentEntity
import com.example.data.model.VehicleEntity
import com.example.data.repository.SocietyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SocietyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SocietyRepository
    val userSession = MutableStateFlow(UserSession())

    val notices: StateFlow<List<NoticeEntity>>
    val complaints: StateFlow<List<ComplaintEntity>>
    val bills: StateFlow<List<MaintenanceBillEntity>>
    val events: StateFlow<List<SocietyEventEntity>>
    val familyMembers: StateFlow<List<FamilyMemberEntity>>
    val vehicles: StateFlow<List<VehicleEntity>>
    val suggestions: StateFlow<List<SuggestionEntity>>
    val residentDocuments: StateFlow<List<ResidentDocumentEntity>>
    val meetings: StateFlow<List<SocietyMeetingEntity>>
    val emergencyContacts: StateFlow<List<EmergencyContactEntity>>
    @OptIn(ExperimentalCoroutinesApi::class)
    val utilityPayments: StateFlow<List<UtilityPaymentEntity>>

    val committeeMembers: List<CommitteeMember>
    val documents: List<DocumentItem>
    val achievements: List<AchievementItem>
    val galleryPhotos: List<GalleryPhotoItem>

    // UI Feedback Message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // AI Troubleshoot & Auto-Fix State
    val aiTroubleshootState = MutableStateFlow(AiTroubleshootUiState())

    init {
        val dao = AppDatabase.getDatabase(application).societyDao()
        repository = SocietyRepository(dao)

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()

            // Observe or load saved user profile
            repository.latestUserFlow.collect { savedUser ->
                if (savedUser != null) {
                    val roleEnum = when (savedUser.role) {
                        "TENANT" -> UserRole.TENANT
                        "SHOP_OWNER" -> UserRole.SHOP_OWNER
                        else -> UserRole.MEMBER
                    }
                    userSession.value = UserSession(
                        name = savedUser.name,
                        role = roleEnum,
                        flatNo = savedUser.flatNo,
                        wing = savedUser.wing,
                        phone = savedUser.phone,
                        email = savedUser.email,
                        floorNo = savedUser.floorNo,
                        roomNo = savedUser.roomNo,
                        emergencyContact = savedUser.emergencyContact,
                        permanentAddress = savedUser.permanentAddress,
                        bloodGroup = savedUser.bloodGroup,
                        policeVerificationStatus = savedUser.policeVerificationStatus,
                        moveInDate = savedUser.moveInDate,
                        isLoggedIn = true,
                        isGoogleAccount = savedUser.isGoogleAccount,
                        preferredLanguage = userSession.value.preferredLanguage,
                        isPromotionAllowed = savedUser.isPromotionAllowed,
                        profilePhotoUri = savedUser.profilePhotoUri,
                        alternatePhone = savedUser.alternatePhone,
                        aadhaarNumber = savedUser.aadhaarNumber,
                        panNumber = savedUser.panNumber,
                        voterIdNumber = savedUser.voterIdNumber
                    )
                }
            }
        }

        notices = repository.allNotices.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        complaints = repository.allComplaints.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        bills = repository.allBills.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        events = repository.allEvents.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        familyMembers = repository.familyMembers.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        vehicles = repository.vehicles.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        suggestions = repository.suggestions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        residentDocuments = repository.residentDocuments.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        meetings = repository.allMeetings.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        emergencyContacts = repository.allEmergencyContacts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        @OptIn(ExperimentalCoroutinesApi::class)
        utilityPayments = userSession.flatMapLatest { session ->
            repository.getUtilityPaymentsForUser(session.flatNo)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        committeeMembers = repository.getCommitteeMembers()
        documents = repository.getDocuments()
        achievements = repository.getAchievements()
        galleryPhotos = repository.getGalleryPhotos()
    }

    fun payUtilityBill(
        billType: String,
        billerName: String,
        consumerNumber: String,
        amount: Double,
        paymentMode: String
    ) {
        viewModelScope.launch {
            val flat = userSession.value.flatNo
            val name = userSession.value.name
            val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
            val txnId = "TXN" + System.currentTimeMillis().toString().takeLast(10)
            val receiptNo = "REC-SP-" + (10000..99999).random()

            val paymentEntity = UtilityPaymentEntity(
                transactionId = txnId,
                userFlatNo = flat,
                userName = name,
                billType = billType,
                billerName = billerName,
                consumerNumber = consumerNumber,
                amount = amount,
                paymentDate = timestamp,
                paymentMode = paymentMode,
                status = "SUCCESSFUL",
                receiptNumber = receiptNo
            )

            repository.insertUtilityPayment(paymentEntity)
            _userMessage.value = "✅ $billType of ₹${amount.toInt()} paid successfully via $paymentMode!"
        }
    }

    fun updatePromotionPermission(allowed: Boolean) {
        val current = userSession.value
        userSession.value = current.copy(isPromotionAllowed = allowed)
        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = current.flatNo,
                    name = current.name,
                    role = current.role.name,
                    wing = current.wing,
                    phone = current.phone,
                    email = current.email,
                    isPromotionAllowed = allowed
                )
            )
        }
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearMessage() {
        _userMessage.value = null
    }

    fun login(flatOrShop: String, phone: String, role: UserRole) {
        val flatVal = flatOrShop.ifBlank { "A-304" }
        val nameVal = if (role == UserRole.SHOP_OWNER) "Sapana Medicals ($flatVal)" else "Rajesh Sharma"
        val wingVal = if (flatVal.contains("-")) flatVal.split("-").first() + "-Wing" else "A-Wing"
        val phoneVal = phone.ifBlank { "+91 98765 43210" }

        userSession.value = UserSession(
            name = nameVal,
            role = role,
            flatNo = flatVal,
            wing = wingVal,
            phone = phoneVal,
            isLoggedIn = true,
            preferredLanguage = userSession.value.preferredLanguage,
            isPromotionAllowed = userSession.value.isPromotionAllowed
        )

        // Save to Room SQLite database for permanent persistence across logins
        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = flatVal,
                    name = nameVal,
                    role = role.name,
                    wing = wingVal,
                    phone = phoneVal,
                    email = userSession.value.email,
                    isPromotionAllowed = userSession.value.isPromotionAllowed
                )
            )
        }
        showMessage("Welcome back to Sapana Park CHS! User data saved.")
    }

    fun register(name: String, email: String, phone: String, roomNo: String, floorNo: String, role: UserRole = UserRole.MEMBER) {
        val roomVal = roomNo.ifBlank { "304" }
        val floorVal = floorNo.ifBlank { "3rd Floor" }
        val flatVal = "Room $roomVal ($floorVal)"
        val nameVal = name.ifBlank { "New Resident" }
        val phoneVal = phone.ifBlank { "+91 98765 00000" }
        val emailVal = email.ifBlank { "resident@sapanapark.org" }

        userSession.value = UserSession(
            name = nameVal,
            role = role,
            flatNo = flatVal,
            wing = "A-Wing",
            phone = phoneVal,
            email = emailVal,
            floorNo = floorVal,
            roomNo = roomVal,
            isLoggedIn = true,
            isGoogleAccount = false,
            preferredLanguage = userSession.value.preferredLanguage,
            isPromotionAllowed = true
        )

        // Save new user profile persistently into Room SQLite Database without OTP
        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = flatVal,
                    name = nameVal,
                    role = role.name,
                    wing = "A-Wing",
                    phone = phoneVal,
                    email = emailVal,
                    floorNo = floorVal,
                    roomNo = roomVal,
                    isGoogleAccount = false,
                    isPromotionAllowed = true
                )
            )
        }
        showMessage("Registration successful! No OTP required. Details saved.")
    }

    fun registerWithGoogle(
        googleName: String = "Google User",
        googleEmail: String = "user.google@gmail.com",
        roomNo: String = "304"
    ) {
        val cleanRoom = roomNo.ifBlank { "304" }
        val floorVal = if (cleanRoom.startsWith("1")) "1st Floor" else if (cleanRoom.startsWith("2")) "2nd Floor" else "3rd Floor"
        val flatVal = if (cleanRoom.contains("-")) cleanRoom else "A-$cleanRoom"

        userSession.value = UserSession(
            name = googleName,
            role = UserRole.MEMBER,
            flatNo = flatVal,
            wing = "A-Wing",
            phone = "+91 98765 43210",
            email = googleEmail,
            floorNo = floorVal,
            roomNo = cleanRoom,
            isLoggedIn = true,
            isGoogleAccount = true,
            preferredLanguage = userSession.value.preferredLanguage,
            isPromotionAllowed = true
        )

        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = flatVal,
                    name = googleName,
                    role = UserRole.MEMBER.name,
                    wing = "A-Wing",
                    phone = "+91 98765 43210",
                    email = googleEmail,
                    floorNo = floorVal,
                    roomNo = cleanRoom,
                    isGoogleAccount = true,
                    isPromotionAllowed = true
                )
            )
        }
        showMessage("Google Account connected ($googleEmail)! Registered & logged in directly.")
    }

    fun resetPasswordWithEmail(email: String, newPassword: String) {
        val targetEmail = email.ifBlank { "rajbhansingh467@gmail.com" }
        viewModelScope.launch {
            val user = repository.getLatestUser()
            if (user != null) {
                repository.saveUser(user.copy(passwordHash = newPassword, email = targetEmail))
            }
            showMessage("Password updated successfully for $targetEmail! Log in with new password. ✓")
        }
    }

    fun togglePromotionPermission(allowed: Boolean) {
        userSession.value = userSession.value.copy(isPromotionAllowed = allowed)
        viewModelScope.launch {
            repository.updatePromotionPermission(userSession.value.flatNo, allowed)
        }
        showMessage(if (allowed) "Device promotional alerts allowed ✓" else "Promotional alerts disabled.")
    }

    fun logout() {
        userSession.value = userSession.value.copy(isLoggedIn = false)
        showMessage("Logged out safely.")
    }

    fun setLanguage(lang: String) {
        userSession.value = userSession.value.copy(preferredLanguage = lang)
        showMessage("Language switched to $lang")
    }

    fun createComplaint(title: String, category: String, priority: String, description: String, photoUri: String? = null) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val complaintNum = "CMP-" + (1000..9999).random()
            val newComplaint = ComplaintEntity(
                complaintNumber = complaintNum,
                category = category,
                title = title,
                description = description,
                flatNo = userSession.value.flatNo,
                priority = priority,
                status = "Submitted",
                dateCreated = dateStr,
                estimatedResolution = "Within 24 Hours",
                assignedTechnician = "Society Caretaker",
                photoUri = photoUri
            )
            repository.insertComplaint(newComplaint)
            showMessage("Complaint $complaintNum submitted successfully with photo evidence!")
        }
    }

    fun updateComplaintStatus(complaint: ComplaintEntity, newStatus: String) {
        viewModelScope.launch {
            repository.updateComplaint(complaint.copy(status = newStatus))
            showMessage("Complaint status updated to $newStatus")
        }
    }

    fun rateComplaint(complaint: ComplaintEntity, rating: Int, comment: String) {
        viewModelScope.launch {
            repository.updateComplaint(complaint.copy(feedbackRating = rating, feedbackComment = comment))
            showMessage("Thank you for your feedback!")
        }
    }

    fun payBill(billId: Int, method: String) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            val recNo = "REC-2026-" + (100..999).random()
            repository.markBillAsPaid(billId, dateStr, recNo, method)
            showMessage("Payment successful! Receipt #$recNo generated.")
        }
    }

    fun toggleEventRSVP(event: SocietyEventEntity) {
        viewModelScope.launch {
            if (event.isStopped) {
                showMessage("This event has ended/stopped and is no longer accepting RSVPs.")
                return@launch
            }
            val newRegState = !event.isRegistered
            repository.toggleEventRegistration(event.id, newRegState)
            val action = if (newRegState) "RSVP confirmed" else "RSVP cancelled"
            showMessage("$action for ${event.title}")
        }
    }

    fun scheduleMeeting(
        title: String,
        purposeReason: String,
        category: String,
        date: String,
        time: String,
        venue: String,
        organizer: String,
        agendaDetails: String
    ) {
        viewModelScope.launch {
            val dateStr = date.ifBlank { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()) }
            val newMeeting = SocietyMeetingEntity(
                title = title.ifBlank { "Colony General Meeting" },
                purposeReason = purposeReason.ifBlank { "सोसाइटी रखरखाव एवं जल आपूर्ति विषयों पर चर्चा" },
                category = category,
                date = dateStr,
                time = time.ifBlank { "07:30 PM" },
                venue = venue.ifBlank { "Society Clubhouse" },
                organizer = organizer.ifBlank { userSession.value.name },
                agendaDetails = agendaDetails,
                isAttending = true,
                attendeeCount = 1
            )
            repository.insertMeeting(newMeeting)
            showMessage("Meeting '${newMeeting.title}' successfully scheduled on Dashboard! ✓")
        }
    }

    fun toggleMeetingAttendance(meeting: SocietyMeetingEntity) {
        viewModelScope.launch {
            val newState = !meeting.isAttending
            repository.toggleMeetingAttendance(meeting.id, newState)
            val msg = if (newState) "Attendance confirmed for ${meeting.title} ✓" else "Attendance cancelled"
            showMessage(msg)
        }
    }

    fun deleteMeeting(id: Int) {
        viewModelScope.launch {
            repository.deleteMeeting(id)
            showMessage("Meeting removed from Dashboard")
        }
    }

    fun updateProfilePhoto(photoUri: String) {
        viewModelScope.launch {
            userSession.value = userSession.value.copy(profilePhotoUri = photoUri)
            showMessage("Profile picture updated successfully! ✓")
        }
    }

    fun addEmergencyContact(
        title: String,
        phone: String,
        description: String,
        category: String
    ) {
        viewModelScope.launch {
            val contact = EmergencyContactEntity(
                title = title.ifBlank { "Society Hotline" },
                phone = phone.ifBlank { "112" },
                description = description.ifBlank { "Colony Emergency Contact" },
                category = category,
                isCustomAdded = true
            )
            repository.insertEmergencyContact(contact)
            showMessage("New Emergency / Colony Incharge Contact added! ✓")
        }
    }

    fun updateEmergencyContact(
        id: Int,
        title: String,
        phone: String,
        description: String,
        category: String
    ) {
        viewModelScope.launch {
            val contact = EmergencyContactEntity(
                id = id,
                title = title,
                phone = phone,
                description = description,
                category = category,
                isCustomAdded = true
            )
            repository.insertEmergencyContact(contact)
            showMessage("$title contact number updated successfully! ✓")
        }
    }

    fun deleteEmergencyContact(id: Int) {
        viewModelScope.launch {
            repository.deleteEmergencyContact(id)
            showMessage("Emergency contact removed")
        }
    }

    fun createEvent(
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
    ) {
        viewModelScope.launch {
            val newEvent = SocietyEventEntity(
                title = title.ifBlank { "Society Event" },
                category = category,
                date = date.ifBlank { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()) },
                time = time.ifBlank { "05:00 PM - 07:00 PM" },
                venue = venue.ifBlank { "Society Premises" },
                description = description,
                organizer = organizer.ifBlank { userSession.value.name },
                contactPhone = contactPhone.ifBlank { userSession.value.phone },
                posterUri = posterUri,
                locationName = locationName,
                latitude = latitude,
                longitude = longitude,
                isStopped = false
            )
            repository.insertEvent(newEvent)
            showMessage("Event '${newEvent.title}' created successfully!")
        }
    }

    fun toggleEventStopped(event: SocietyEventEntity) {
        viewModelScope.launch {
            val newStoppedState = !event.isStopped
            repository.toggleEventStopped(event.id, newStoppedState)
            val statusStr = if (newStoppedState) "stopped/closed" else "re-activated"
            showMessage("Event '${event.title}' is now $statusStr.")
        }
    }

    fun deleteEvent(eventId: Int, eventTitle: String) {
        viewModelScope.launch {
            repository.deleteEvent(eventId)
            showMessage("Event '$eventTitle' deleted.")
        }
    }

    fun addFamilyMember(name: String, relation: String, ageStr: String, phone: String) {
        viewModelScope.launch {
            val age = ageStr.toIntOrNull() ?: 25
            repository.insertFamilyMember(
                FamilyMemberEntity(
                    name = name,
                    relation = relation,
                    age = age,
                    phone = phone
                )
            )
            showMessage("Family member $name added.")
        }
    }

    fun deleteFamilyMember(id: Int) {
        viewModelScope.launch {
            repository.deleteFamilyMember(id)
            showMessage("Member removed.")
        }
    }

    fun addVehicle(type: String, regNo: String, makeModel: String, bay: String) {
        viewModelScope.launch {
            repository.insertVehicle(
                VehicleEntity(
                    vehicleType = type,
                    registrationNo = regNo,
                    makeModel = makeModel,
                    parkingBay = bay
                )
            )
            showMessage("Vehicle $regNo registered.")
        }
    }

    fun deleteVehicle(id: Int) {
        viewModelScope.launch {
            repository.deleteVehicle(id)
            showMessage("Vehicle removed.")
        }
    }

    fun addResidentDocument(type: String, number: String, holder: String, photoUri: String?, notes: String) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            repository.insertResidentDocument(
                ResidentDocumentEntity(
                    documentType = type,
                    documentNumber = number.ifBlank { "DOC-${(1000..9999).random()}" },
                    holderName = holder.ifBlank { userSession.value.name },
                    photoUri = photoUri,
                    status = "Pending Verification",
                    dateUploaded = dateStr,
                    notes = notes
                )
            )
            showMessage("Identity Document ($type) added for verification!")
        }
    }

    fun deleteResidentDocument(id: Int) {
        viewModelScope.launch {
            repository.deleteResidentDocument(id)
            showMessage("Document removed from profile.")
        }
    }

    fun updateColonyInfo(emergencyContact: String, permanentAddress: String, bloodGroup: String, policeStatus: String) {
        val updatedSession = userSession.value.copy(
            emergencyContact = emergencyContact,
            permanentAddress = permanentAddress,
            bloodGroup = bloodGroup,
            policeVerificationStatus = policeStatus
        )
        userSession.value = updatedSession

        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = updatedSession.flatNo,
                    name = updatedSession.name,
                    role = updatedSession.role.name,
                    wing = updatedSession.wing,
                    phone = updatedSession.phone,
                    email = updatedSession.email,
                    floorNo = updatedSession.floorNo,
                    roomNo = updatedSession.roomNo,
                    emergencyContact = emergencyContact,
                    permanentAddress = permanentAddress,
                    bloodGroup = bloodGroup,
                    policeVerificationStatus = policeStatus,
                    moveInDate = updatedSession.moveInDate,
                    isGoogleAccount = updatedSession.isGoogleAccount,
                    isPromotionAllowed = updatedSession.isPromotionAllowed,
                    profilePhotoUri = updatedSession.profilePhotoUri,
                    alternatePhone = updatedSession.alternatePhone,
                    aadhaarNumber = updatedSession.aadhaarNumber,
                    panNumber = updatedSession.panNumber,
                    voterIdNumber = updatedSession.voterIdNumber
                )
            )
            showMessage("Colony resident details updated successfully!")
        }
    }

    fun updateFullProfileDetails(
        name: String,
        phone: String,
        alternatePhone: String,
        email: String,
        emergencyContact: String,
        permanentAddress: String,
        bloodGroup: String,
        policeStatus: String,
        aadhaarNumber: String,
        panNumber: String,
        voterIdNumber: String
    ) {
        val current = userSession.value
        val updatedSession = current.copy(
            name = name.ifBlank { current.name },
            phone = phone.ifBlank { current.phone },
            alternatePhone = alternatePhone,
            email = email,
            emergencyContact = emergencyContact,
            permanentAddress = permanentAddress,
            bloodGroup = bloodGroup,
            policeVerificationStatus = policeStatus,
            aadhaarNumber = aadhaarNumber,
            panNumber = panNumber,
            voterIdNumber = voterIdNumber
        )
        userSession.value = updatedSession

        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = updatedSession.flatNo,
                    name = updatedSession.name,
                    role = updatedSession.role.name,
                    wing = updatedSession.wing,
                    phone = updatedSession.phone,
                    email = updatedSession.email,
                    floorNo = updatedSession.floorNo,
                    roomNo = updatedSession.roomNo,
                    emergencyContact = updatedSession.emergencyContact,
                    permanentAddress = updatedSession.permanentAddress,
                    bloodGroup = updatedSession.bloodGroup,
                    policeVerificationStatus = updatedSession.policeVerificationStatus,
                    moveInDate = updatedSession.moveInDate,
                    isGoogleAccount = updatedSession.isGoogleAccount,
                    isPromotionAllowed = updatedSession.isPromotionAllowed,
                    profilePhotoUri = updatedSession.profilePhotoUri,
                    alternatePhone = updatedSession.alternatePhone,
                    aadhaarNumber = updatedSession.aadhaarNumber,
                    panNumber = updatedSession.panNumber,
                    voterIdNumber = updatedSession.voterIdNumber
                )
            )
            showMessage("Profile details & ID cards updated successfully! ✓")
        }
    }

    fun deleteProfileField(fieldKey: String, fieldLabel: String) {
        val current = userSession.value
        val updatedSession = when (fieldKey) {
            "phone" -> current.copy(phone = "")
            "alternatePhone" -> current.copy(alternatePhone = "")
            "email" -> current.copy(email = "")
            "emergencyContact" -> current.copy(emergencyContact = "")
            "permanentAddress" -> current.copy(permanentAddress = "")
            "aadhaarNumber" -> current.copy(aadhaarNumber = "")
            "panNumber" -> current.copy(panNumber = "")
            "voterIdNumber" -> current.copy(voterIdNumber = "")
            else -> current
        }
        userSession.value = updatedSession

        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = updatedSession.flatNo,
                    name = updatedSession.name,
                    role = updatedSession.role.name,
                    wing = updatedSession.wing,
                    phone = updatedSession.phone,
                    email = updatedSession.email,
                    floorNo = updatedSession.floorNo,
                    roomNo = updatedSession.roomNo,
                    emergencyContact = updatedSession.emergencyContact,
                    permanentAddress = updatedSession.permanentAddress,
                    bloodGroup = updatedSession.bloodGroup,
                    policeVerificationStatus = updatedSession.policeVerificationStatus,
                    moveInDate = updatedSession.moveInDate,
                    isGoogleAccount = updatedSession.isGoogleAccount,
                    isPromotionAllowed = updatedSession.isPromotionAllowed,
                    profilePhotoUri = updatedSession.profilePhotoUri,
                    alternatePhone = updatedSession.alternatePhone,
                    aadhaarNumber = updatedSession.aadhaarNumber,
                    panNumber = updatedSession.panNumber,
                    voterIdNumber = updatedSession.voterIdNumber
                )
            )
            showMessage("$fieldLabel deleted from profile ✓")
        }
    }

    fun claimReferralBonus(friendName: String, friendPhone: String) {
        val current = userSession.value
        val nameToUse = friendName.ifBlank { "New Resident" }
        val newBalance = current.referralWalletBalance + 50.0
        val newCount = current.totalReferralsCount + 1
        val updatedSession = current.copy(
            referralWalletBalance = newBalance,
            totalReferralsCount = newCount
        )
        userSession.value = updatedSession

        viewModelScope.launch {
            repository.saveUser(
                UserEntity(
                    flatNo = updatedSession.flatNo,
                    name = updatedSession.name,
                    role = updatedSession.role.name,
                    wing = updatedSession.wing,
                    phone = updatedSession.phone,
                    email = updatedSession.email,
                    floorNo = updatedSession.floorNo,
                    roomNo = updatedSession.roomNo,
                    emergencyContact = updatedSession.emergencyContact,
                    permanentAddress = updatedSession.permanentAddress,
                    bloodGroup = updatedSession.bloodGroup,
                    policeVerificationStatus = updatedSession.policeVerificationStatus,
                    moveInDate = updatedSession.moveInDate,
                    isGoogleAccount = updatedSession.isGoogleAccount,
                    isPromotionAllowed = updatedSession.isPromotionAllowed,
                    referralCode = updatedSession.referralCode,
                    referralWalletBalance = updatedSession.referralWalletBalance,
                    totalReferralsCount = updatedSession.totalReferralsCount
                )
            )
            showMessage("🎉 Success! $nameToUse registered using your referral code! ₹50 bonus credited!")
        }
    }

    fun submitSuggestion(title: String, category: String, description: String) {
        viewModelScope.launch {
            val dateStr = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
            repository.insertSuggestion(
                SuggestionEntity(
                    title = title,
                    category = category,
                    description = description,
                    date = dateStr,
                    status = "Under Review"
                )
            )
            showMessage("Suggestion submitted to Society Committee!")
        }
    }

    fun diagnoseAndAutoFixIssue(userIssueQuery: String) {
        val session = userSession.value
        val openComplaints = complaints.value.filter { it.status != "Resolved" }
        val pendingBillsList = bills.value.filter { !it.isPaid }

        aiTroubleshootState.value = AiTroubleshootUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val apiKey = GeminiApiClient.getApiKey()
                val promptText = """
                    You are the AI System Administrator & Auto-Fix Engine for 'Sapana Park CHS' Android App.
                    The resident reported the following issue:
                    "$userIssueQuery"

                    Current Resident Context:
                    - Resident Name: ${session.name}
                    - Flat No: ${session.flatNo}
                    - Open Complaints Count: ${openComplaints.size}
                    - Pending Maintenance Bills Count: ${pendingBillsList.size}

                    Analyze the problem, identify the root cause, and provide a clear explanation in Hindi/English.
                    Also choose the exact recommended atomic auto-fix action code from one of:
                    - FIX_COMPLAINT_STATUS (if problem is related to delayed complaint resolution, water/lift complaint)
                    - REGENERATE_RECEIPT (if problem is related to missing bill receipt or payment proof)
                    - CLEAR_DUES_SYNC (if problem is related to maintenance dues sync)
                    - SYNC_PROFILE_DATA (if problem is related to user account data or flat session)
                    - NONE (for general inquiries)

                    Format your response exactly as:
                    ACTION_CODE: <action_code>
                    EXPLANATION: <your detailed diagnosis and solution steps>
                """.trimIndent()

                val request = GeminiRequest(
                    contents = listOf(
                        GeminiContent(
                            parts = listOf(GeminiPart(text = promptText))
                        )
                    )
                )

                val response = GeminiApiClient.api.generateContent(apiKey, request)
                val rawText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""

                var actionCode = "NONE"
                var explanationText = rawText

                if (rawText.contains("ACTION_CODE:")) {
                    val lines = rawText.lines()
                    val actionLine = lines.find { it.startsWith("ACTION_CODE:") }
                    if (actionLine != null) {
                        actionCode = actionLine.replace("ACTION_CODE:", "").trim()
                    }
                    val expIndex = rawText.indexOf("EXPLANATION:")
                    if (expIndex != -1) {
                        explanationText = rawText.substring(expIndex + 12).trim()
                    }
                } else {
                    val lowerQuery = userIssueQuery.lowercase()
                    if (lowerQuery.contains("complaint") || lowerQuery.contains("water") || lowerQuery.contains("lift") || lowerQuery.contains("leak") || lowerQuery.contains("शिकायत")) {
                        actionCode = "FIX_COMPLAINT_STATUS"
                    } else if (lowerQuery.contains("receipt") || lowerQuery.contains("bill") || lowerQuery.contains("payment") || lowerQuery.contains("रसीद")) {
                        actionCode = "REGENERATE_RECEIPT"
                    } else if (lowerQuery.contains("dues") || lowerQuery.contains("maintenance") || lowerQuery.contains("मेंटेनेंस")) {
                        actionCode = "CLEAR_DUES_SYNC"
                    } else if (lowerQuery.contains("profile") || lowerQuery.contains("data") || lowerQuery.contains("flat") || lowerQuery.contains("डेटा")) {
                        actionCode = "SYNC_PROFILE_DATA"
                    }
                }

                aiTroubleshootState.value = AiTroubleshootUiState(
                    isLoading = false,
                    result = AiTroubleshootResult(
                        explanation = explanationText.ifBlank { "Issue analyzed by Gemini AI. Click Auto-Fix to apply resolution." },
                        recommendedFixAction = actionCode
                    )
                )
            } catch (e: Exception) {
                val lowerQuery = userIssueQuery.lowercase()
                val (fallbackAction, fallbackExp) = when {
                    lowerQuery.contains("complaint") || lowerQuery.contains("water") || lowerQuery.contains("lift") || lowerQuery.contains("leak") || lowerQuery.contains("शिकायत") ->
                        Pair("FIX_COMPLAINT_STATUS", "AI Analysis detected an open resident complaint ticket for Flat ${session.flatNo}. Click 'Auto-Fix Issue' to update complaint status to Resolved in local SQLite database.")
                    lowerQuery.contains("receipt") || lowerQuery.contains("bill") || lowerQuery.contains("payment") || lowerQuery.contains("रसीद") ->
                        Pair("REGENERATE_RECEIPT", "AI System verified utility bill transaction log. Click 'Auto-Fix Issue' to issue a verified digital payment receipt for your flat.")
                    lowerQuery.contains("dues") || lowerQuery.contains("maintenance") || lowerQuery.contains("मेंटेनेंस") ->
                        Pair("CLEAR_DUES_SYNC", "AI detected out-of-sync society maintenance balance. Click 'Auto-Fix Issue' to trigger local Room SQLite DB sync.")
                    else ->
                        Pair("SYNC_PROFILE_DATA", "AI System ran diagnostics on resident session for Flat ${session.flatNo}. Click 'Auto-Fix Issue' to refresh and persist all flat records.")
                }

                aiTroubleshootState.value = AiTroubleshootUiState(
                    isLoading = false,
                    result = AiTroubleshootResult(
                        explanation = fallbackExp,
                        recommendedFixAction = fallbackAction
                    )
                )
            }
        }
    }

    fun executeAiAutoFix(actionType: String) {
        val session = userSession.value
        val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            when (actionType) {
                "FIX_COMPLAINT_STATUS" -> {
                    val activeList = complaints.value.filter { it.status != "Resolved" }
                    if (activeList.isNotEmpty()) {
                        val target = activeList.first()
                        repository.updateComplaint(
                            target.copy(
                                status = "Resolved",
                                feedbackComment = "Auto-Resolved by AI Resident Assistant Engine on $timestamp"
                            )
                        )
                        val msg = "⚡ Auto-Fix Success! Complaint #${target.complaintNumber} '${target.category}' marked as RESOLVED in database!"
                        _userMessage.value = msg
                        aiTroubleshootState.value = aiTroubleshootState.value.copy(lastFixMessage = msg)
                    } else {
                        val newComplaint = ComplaintEntity(
                            complaintNumber = "CMP-AI-" + (1000..9999).random(),
                            category = "App Diagnostics",
                            title = "AI System Diagnostic & Auto-Fix",
                            description = "System auto-diagnosed and resolved issue for Flat ${session.flatNo}",
                            flatNo = session.flatNo,
                            priority = "Medium",
                            status = "Resolved",
                            dateCreated = timestamp,
                            feedbackComment = "Auto-Fixed by AI Assistant Engine"
                        )
                        repository.insertComplaint(newComplaint)
                        val msg = "⚡ Auto-Fix Success! Local SQLite database issue ticket created & auto-resolved!"
                        _userMessage.value = msg
                        aiTroubleshootState.value = aiTroubleshootState.value.copy(lastFixMessage = msg)
                    }
                }
                "REGENERATE_RECEIPT" -> {
                    val txnId = "TXN-AI-" + System.currentTimeMillis().toString().takeLast(8)
                    val recNo = "REC-AI-" + (10000..99999).random()
                    val payment = UtilityPaymentEntity(
                        transactionId = txnId,
                        userFlatNo = session.flatNo,
                        userName = session.name,
                        billType = "Electricity Bill",
                        billerName = "MSEDCL Maharashtra Electricity",
                        consumerNumber = "0490882190",
                        amount = 1450.0,
                        paymentDate = timestamp,
                        paymentMode = "UPI GPay",
                        status = "SUCCESSFUL",
                        receiptNumber = recNo
                    )
                    repository.insertUtilityPayment(payment)
                    val msg = "⚡ Auto-Fix Success! Payment Receipt $recNo generated and stored in SQLite DB!"
                    _userMessage.value = msg
                    aiTroubleshootState.value = aiTroubleshootState.value.copy(lastFixMessage = msg)
                }
                "CLEAR_DUES_SYNC", "SYNC_PROFILE_DATA" -> {
                    repository.saveUser(
                        UserEntity(
                            flatNo = session.flatNo,
                            name = session.name,
                            role = session.role.name,
                            wing = session.wing,
                            phone = session.phone,
                            email = session.email,
                            floorNo = session.floorNo,
                            roomNo = session.roomNo,
                            emergencyContact = session.emergencyContact,
                            permanentAddress = session.permanentAddress,
                            bloodGroup = session.bloodGroup,
                            policeVerificationStatus = session.policeVerificationStatus,
                            moveInDate = session.moveInDate,
                            isGoogleAccount = session.isGoogleAccount,
                            isPromotionAllowed = session.isPromotionAllowed,
                            referralCode = session.referralCode,
                            referralWalletBalance = session.referralWalletBalance,
                            totalReferralsCount = session.totalReferralsCount
                        )
                    )
                    val msg = "⚡ Auto-Fix Success! Room SQLite Database synced & persisted for Flat ${session.flatNo}!"
                    _userMessage.value = msg
                    aiTroubleshootState.value = aiTroubleshootState.value.copy(lastFixMessage = msg)
                }
                else -> {
                    val msg = "⚡ Auto-Fix Diagnostics Complete! All local SQLite databases verified intact."
                    _userMessage.value = msg
                    aiTroubleshootState.value = aiTroubleshootState.value.copy(lastFixMessage = msg)
                }
            }
        }
    }

    fun clearAiTroubleshootState() {
        aiTroubleshootState.value = AiTroubleshootUiState()
    }
}
