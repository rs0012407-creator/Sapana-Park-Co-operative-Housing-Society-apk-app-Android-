package com.example.data.repository

import com.example.data.local.SocietyDao
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
import com.example.data.model.UtilityPaymentEntity
import com.example.data.model.VehicleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class SocietyRepository(private val societyDao: SocietyDao) {

    val allNotices: Flow<List<NoticeEntity>> = societyDao.getAllNotices()
    val allComplaints: Flow<List<ComplaintEntity>> = societyDao.getAllComplaints()
    val allBills: Flow<List<MaintenanceBillEntity>> = societyDao.getAllBills()
    val allEvents: Flow<List<SocietyEventEntity>> = societyDao.getAllEvents()
    val familyMembers: Flow<List<FamilyMemberEntity>> = societyDao.getAllFamilyMembers()
    val vehicles: Flow<List<VehicleEntity>> = societyDao.getAllVehicles()
    val suggestions: Flow<List<SuggestionEntity>> = societyDao.getAllSuggestions()
    val residentDocuments: Flow<List<ResidentDocumentEntity>> = societyDao.getAllResidentDocuments()
    val allMeetings: Flow<List<SocietyMeetingEntity>> = societyDao.getAllMeetings()
    val allEmergencyContacts: Flow<List<EmergencyContactEntity>> = societyDao.getAllEmergencyContacts()
    val latestUserFlow: Flow<UserEntity?> = societyDao.getLatestUserFlow()

    suspend fun seedInitialDataIfEmpty() {
        if (allNotices.first().isEmpty()) {
            societyDao.insertAllNotices(initialNotices)
        }
        if (allBills.first().isEmpty()) {
            societyDao.insertAllBills(initialBills)
        }
        if (allComplaints.first().isEmpty()) {
            initialComplaints.forEach { societyDao.insertComplaint(it) }
        }
        if (allEvents.first().isEmpty()) {
            societyDao.insertAllEvents(initialEvents)
        }
        if (allMeetings.first().isEmpty()) {
            societyDao.insertAllMeetings(initialMeetings)
        }
        if (allEmergencyContacts.first().isEmpty()) {
            societyDao.insertAllEmergencyContacts(initialEmergencyContacts)
        }
        if (familyMembers.first().isEmpty()) {
            initialFamilyMembers.forEach { societyDao.insertFamilyMember(it) }
        }
        if (vehicles.first().isEmpty()) {
            initialVehicles.forEach { societyDao.insertVehicle(it) }
        }
        if (residentDocuments.first().isEmpty()) {
            initialResidentDocuments.forEach { societyDao.insertResidentDocument(it) }
        }
        if (societyDao.getLatestUser() == null) {
            societyDao.insertOrUpdateUser(initialUser)
        }
        if (societyDao.getUtilityPaymentsForUser("A-304").first().isEmpty()) {
            initialUtilityPayments.forEach { societyDao.insertUtilityPayment(it) }
        }
    }

    suspend fun saveUser(user: UserEntity) = societyDao.insertOrUpdateUser(user)
    suspend fun getUserByFlat(flatNo: String) = societyDao.getUserByFlat(flatNo)
    suspend fun getUserByPhone(phone: String) = societyDao.getUserByPhone(phone)
    suspend fun getLatestUser() = societyDao.getLatestUser()
    suspend fun updatePromotionPermission(flatNo: String, isAllowed: Boolean) =
        societyDao.updatePromotionPermission(flatNo, isAllowed)

    suspend fun insertComplaint(complaint: ComplaintEntity) = societyDao.insertComplaint(complaint)
    suspend fun updateComplaint(complaint: ComplaintEntity) = societyDao.updateComplaint(complaint)
    suspend fun markBillAsPaid(billId: Int, paidDate: String, receiptNo: String, method: String) =
        societyDao.markBillAsPaid(billId, paidDate, receiptNo, method)

    suspend fun insertEvent(event: SocietyEventEntity) = societyDao.insertEvent(event)
    suspend fun updateEvent(event: SocietyEventEntity) = societyDao.updateEvent(event)
    suspend fun toggleEventRegistration(eventId: Int, isRegistered: Boolean) =
        societyDao.toggleEventRegistration(eventId, isRegistered)
    suspend fun toggleEventStopped(eventId: Int, isStopped: Boolean) =
        societyDao.toggleEventStopped(eventId, isStopped)
    suspend fun deleteEvent(eventId: Int) = societyDao.deleteEventById(eventId)

    suspend fun insertFamilyMember(member: FamilyMemberEntity) = societyDao.insertFamilyMember(member)
    suspend fun deleteFamilyMember(id: Int) = societyDao.deleteFamilyMemberById(id)

    suspend fun insertVehicle(vehicle: VehicleEntity) = societyDao.insertVehicle(vehicle)
    suspend fun deleteVehicle(id: Int) = societyDao.deleteVehicleById(id)

    suspend fun insertSuggestion(suggestion: SuggestionEntity) = societyDao.insertSuggestion(suggestion)

    fun getCommitteeMembers(): List<CommitteeMember> = initialCommitteeMembers
    fun getDocuments(): List<DocumentItem> = initialDocuments
    fun getAchievements(): List<AchievementItem> = initialAchievements
    fun getGalleryPhotos(): List<GalleryPhotoItem> = initialGalleryPhotos

    suspend fun insertResidentDocument(doc: ResidentDocumentEntity) = societyDao.insertResidentDocument(doc)
    suspend fun deleteResidentDocument(id: Int) = societyDao.deleteResidentDocumentById(id)

    fun getUtilityPaymentsForUser(flatNo: String): Flow<List<UtilityPaymentEntity>> =
        societyDao.getUtilityPaymentsForUser(flatNo)

    suspend fun insertUtilityPayment(payment: UtilityPaymentEntity) =
        societyDao.insertUtilityPayment(payment)

    suspend fun insertMeeting(meeting: SocietyMeetingEntity) = societyDao.insertMeeting(meeting)
    suspend fun toggleMeetingAttendance(meetingId: Int, isAttending: Boolean) = societyDao.toggleMeetingAttendance(meetingId, isAttending)
    suspend fun deleteMeeting(id: Int) = societyDao.deleteMeetingById(id)

    suspend fun insertEmergencyContact(contact: EmergencyContactEntity) = societyDao.insertEmergencyContact(contact)
    suspend fun deleteEmergencyContact(id: Int) = societyDao.deleteEmergencyContactById(id)

    companion object {
        val initialEmergencyContacts = listOf(
            // Colony Incharges & Society Officials
            EmergencyContactEntity(
                id = 1,
                title = "Colony Incharge (कॉलोनी इंचार्ज)",
                phone = "+91 98220 00005",
                description = "Sapana Park Overall Colony Incharge & Admin",
                category = "Colony Incharge & Society",
                iconName = "Person",
                colorHex = "#15803D"
            ),
            EmergencyContactEntity(
                id = 2,
                title = "Society Guard (सोसाइटी गार्ड)",
                phone = "+91 98220 00001",
                description = "24x7 Main Gate Security Guard Desk & Gatekeeper",
                category = "Colony Incharge & Society",
                iconName = "Security",
                colorHex = "#B91C1C"
            ),
            EmergencyContactEntity(
                id = 3,
                title = "Colony President (Prakash Deshmukh)",
                phone = "+91 98220 00010",
                description = "Sapana Park Colony President",
                category = "Colony Incharge & Society",
                iconName = "Person",
                colorHex = "#047857"
            ),
            EmergencyContactEntity(
                id = 4,
                title = "Society Secretary (Rajesh Kulkarni)",
                phone = "+91 98220 00011",
                description = "General Administration & Society Records",
                category = "Colony Incharge & Society",
                iconName = "Person",
                colorHex = "#0369A1"
            ),
            EmergencyContactEntity(
                id = 5,
                title = "Society Office Manager",
                phone = "+91 98220 11223",
                description = "Office Timings: 10 AM to 6 PM (Mon-Sat)",
                category = "Colony Incharge & Society",
                iconName = "Business",
                colorHex = "#6D28D9"
            ),
            EmergencyContactEntity(
                id = 6,
                title = "Water Supply & Tank Incharge (Mahesh)",
                phone = "+91 98220 22334",
                description = "Borewell Motor, Overhead Tank & Valve Control",
                category = "Colony Incharge & Society",
                iconName = "Plumbing",
                colorHex = "#0284C7"
            ),
            EmergencyContactEntity(
                id = 7,
                title = "Society Electrician (Ramesh)",
                phone = "+91 98220 33445",
                description = "Common Lighting, Meter Box & Generator Support",
                category = "Colony Incharge & Society",
                iconName = "FlashOn",
                colorHex = "#D97706"
            ),
            EmergencyContactEntity(
                id = 8,
                title = "Elevator / Lift 24x7 AMC Emergency",
                phone = "1800 222 333",
                description = "KONE Elevator Passenger Trap Rescue Helpline",
                category = "Colony Incharge & Society",
                iconName = "PhoneInTalk",
                colorHex = "#4338CA"
            ),

            // National Emergency Helpline Numbers
            EmergencyContactEntity(
                id = 8,
                title = "National Emergency Helpline",
                phone = "112",
                description = "Single All-India Police, Fire & Medical Hotline",
                category = "National Emergency",
                iconName = "Warning",
                colorHex = "#DC2626"
            ),
            EmergencyContactEntity(
                id = 9,
                title = "Police Control Room",
                phone = "100",
                description = "City Police Precinct Emergency Dispatch",
                category = "National Emergency",
                iconName = "LocalPolice",
                colorHex = "#1E40AF"
            ),
            EmergencyContactEntity(
                id = 10,
                title = "Ambulance & Trauma Care",
                phone = "108",
                description = "24x7 Free Govt Emergency Ambulance Service",
                category = "National Emergency",
                iconName = "LocalHospital",
                colorHex = "#BE123C"
            ),
            EmergencyContactEntity(
                id = 11,
                title = "Fire Brigade Control",
                phone = "101",
                description = "City Fire & Rescue Station Station Master",
                category = "National Emergency",
                iconName = "LocalFireDepartment",
                colorHex = "#C2410C"
            ),
            EmergencyContactEntity(
                id = 12,
                title = "Women Safety Helpline",
                phone = "1091",
                description = "24x7 Women in Distress & Legal Assistance",
                category = "National Emergency",
                iconName = "Security",
                colorHex = "#9333EA"
            ),
            EmergencyContactEntity(
                id = 13,
                title = "National Cyber Crime Helpline",
                phone = "1930",
                description = "Financial Fraud & Online Crime Immediate Hold",
                category = "National Emergency",
                iconName = "Shield",
                colorHex = "#0F766E"
            )
        )
        val initialMeetings = listOf(
            SocietyMeetingEntity(
                id = 1,
                title = "Urgent Water Supply & Tank Cleaning Meeting",
                purposeReason = "कॉलोनी में पानी की भारी समस्या के समाधान, नए समरसेबल पंप मोटर खरीदने एवं ओवरहेड वाटर टैंक की सफाई की तारीख तय करने हेतु अति-आवश्यक बैठक।",
                category = "Water & Utility",
                date = "05 Aug 2026",
                time = "07:30 PM",
                venue = "Society Clubhouse & Courtyard",
                organizer = "Chairman & Water Committee",
                agendaDetails = "1. Borewell Pump Replacement budget approval.\n2. Overhead Water Tank Cleaning schedule for A & B Wings.\n3. Water Tariff collection discuss.",
                isAttending = true,
                attendeeCount = 28
            ),
            SocietyMeetingEntity(
                id = 2,
                title = "CCTV Installation & Night Security Guard Review",
                purposeReason = "सोसाइटी में नए 16 HD CCTV कैमरे लगवाने एवं नाइट सिक्योरिटी गार्ड की शिफ्ट बदलने पर सभी निवासियों की सहमति हेतु।",
                category = "Security & Safety",
                date = "10 Aug 2026",
                time = "08:00 PM",
                venue = "Wing-A Lobby / Clubhouse",
                organizer = "Security In-Charge (Ramesh Shinde)",
                agendaDetails = "1. Quotations review for Hikvision 16-channel CCTV System.\n2. Security Guard Vendor contract renewal.\n3. Visitor App compulsory entry rule.",
                isAttending = false,
                attendeeCount = 19
            ),
            SocietyMeetingEntity(
                id = 3,
                title = "Ganesh Utsav Festival & Building Painting Budget",
                purposeReason = "आगामी गणेशोत्सव त्योहार चंदा संग्रह, सांस्कृतिक कार्यक्रम आयोजन एवं इमारत के बाहरी पेंटिंग के बजट की स्वीकृति हेतु।",
                category = "General & Celebration",
                date = "15 Aug 2026",
                time = "06:30 PM",
                venue = "Central Garden Pavilion",
                organizer = "Managing Committee & Youth Club",
                agendaDetails = "1. Festival contribution per flat fixation.\n2. Cultural programs & prasad distribution.\n3. Building External Asian Paints quotation vote.",
                isAttending = true,
                attendeeCount = 35
            )
        )

        val initialNotices = listOf(
            NoticeEntity(
                title = "Annual General Body Meeting (AGM 2026)",
                category = "AGM",
                date = "15 Aug 2026",
                description = "Notice is hereby given for the 33rd AGM of Sapana Park CHS Ltd. on Sunday 24th August 2026 at 10:00 AM in the Society Clubhouse. Agenda includes Audit Approval, Managing Committee Elections & Maintenance Fee Review.",
                isImportant = true
            ),
            NoticeEntity(
                title = "Elevator Maintenance & AMC Inspection",
                category = "Maintenance",
                date = "08 Aug 2026",
                description = "A-Wing Lift #2 will be under quarterly AMC service on Tuesday between 11:00 AM and 3:00 PM. Please use A-Wing Lift #1 during this duration. Regret the temporary inconvenience.",
                isImportant = false
            ),
            NoticeEntity(
                title = "Monsoon Terrace Waterproofing & Cleanliness Drive",
                category = "General",
                date = "01 Aug 2026",
                description = "Residents are requested not to stack heavy items on terrace access paths. Terrace cleaning and drain clearance will take place this weekend.",
                isImportant = false
            ),
            NoticeEntity(
                title = "Revised Gate Pass & Visitor Security System",
                category = "Security",
                date = "25 Jul 2026",
                description = "All delivery agents and visitors must register via the Guard App. Digital OTP verification is mandatory for late night entries after 10:30 PM.",
                isImportant = true
            )
        )

        val initialBills = listOf(
            MaintenanceBillEntity(
                billNumber = "INV-2026-08-304",
                monthYear = "August 2026",
                flatNo = "A-304",
                maintenanceCharge = 2200.0,
                waterCharges = 450.0,
                parkingFee = 350.0,
                festivalContribution = 200.0,
                totalAmount = 3200.0,
                dueDate = "15 Aug 2026",
                isPaid = false
            ),
            MaintenanceBillEntity(
                billNumber = "INV-2026-07-304",
                monthYear = "July 2026",
                flatNo = "A-304",
                maintenanceCharge = 2200.0,
                waterCharges = 420.0,
                parkingFee = 350.0,
                festivalContribution = 0.0,
                totalAmount = 2970.0,
                dueDate = "15 Jul 2026",
                isPaid = true,
                paidDate = "10 Jul 2026",
                receiptNumber = "REC-2026-07-981",
                paymentMethod = "UPI (GPay)"
            ),
            MaintenanceBillEntity(
                billNumber = "INV-2026-06-304",
                monthYear = "June 2026",
                flatNo = "A-304",
                maintenanceCharge = 2200.0,
                waterCharges = 400.0,
                parkingFee = 350.0,
                festivalContribution = 0.0,
                totalAmount = 2950.0,
                dueDate = "15 Jun 2026",
                isPaid = true,
                paidDate = "12 Jun 2026",
                receiptNumber = "REC-2026-06-432",
                paymentMethod = "Net Banking"
            )
        )

        val initialComplaints = listOf(
            ComplaintEntity(
                complaintNumber = "CMP-2026-089",
                category = "Plumbing",
                title = "Low Water Pressure in Master Bathroom",
                description = "Since yesterday evening, the main inlet line to A-304 master toilet has reduced water flow. Plumbing inspection needed.",
                flatNo = "A-304",
                priority = "Medium",
                status = "In Progress",
                dateCreated = "31 Jul 2026",
                estimatedResolution = "Today, 4:00 PM",
                assignedTechnician = "Plumber Ramesh (+91 98221 00112)"
            ),
            ComplaintEntity(
                complaintNumber = "CMP-2026-072",
                category = "Cleanliness",
                title = "Staircase Corridor Lighting & Dusting",
                description = "Light bulb near 3rd floor staircase landing in A-Wing fused. Corridor floor cleaning needed.",
                flatNo = "A-304",
                priority = "Low",
                status = "Resolved",
                dateCreated = "22 Jul 2026",
                estimatedResolution = "Completed",
                assignedTechnician = "Sanitation In-charge",
                feedbackRating = 5,
                feedbackComment = "Very quick response by electrician!"
            ),
            ComplaintEntity(
                complaintNumber = "CMP-2026-061",
                category = "Parking",
                title = "Unauthorised Vehicle in Bay #14",
                description = "Unknown two wheeler MH-12-AB-9876 parked in reserved Bay #14 without parking sticker.",
                flatNo = "A-304",
                priority = "High",
                status = "Resolved",
                dateCreated = "10 Jul 2026",
                estimatedResolution = "Completed",
                assignedTechnician = "Gate Security Guard",
                feedbackRating = 4,
                feedbackComment = "Guard verified and moved vehicle."
            )
        )

        val initialEvents = listOf(
            SocietyEventEntity(
                title = "Free General Health & Eye Checkup Camp",
                category = "Health Camp",
                date = "10 Aug 2026",
                time = "09:00 AM - 02:00 PM",
                venue = "Society Clubhouse Hall",
                description = "Organized in association with Sahyadri Hospitals. Includes BP, Blood Sugar, ECG, Dental & Eye Screening for all residents & staff.",
                organizer = "Sapana Park Women's Committee",
                isRegistered = true,
                registrationCount = 64,
                posterUri = "https://picsum.photos/seed/healthcamp/600/350",
                locationName = "Sapana Park Clubhouse, Ground Floor",
                latitude = 18.5204,
                longitude = 73.8567,
                contactPhone = "+91 98220 11223",
                isStopped = false
            ),
            SocietyEventEntity(
                title = "Women's Empowerment & Financial Literacy Workshop",
                category = "Women's Empowerment",
                date = "18 Aug 2026",
                time = "04:00 PM - 06:30 PM",
                venue = "Community Conference Room",
                description = "Interactive session covering SIP investments, digital banking safety, cyber fraud awareness, and self-defense techniques for women.",
                organizer = "Sapana Park Social Cell",
                isRegistered = false,
                registrationCount = 42,
                posterUri = "https://picsum.photos/seed/womenempower/600/350",
                locationName = "Sapana Park Community Hall, Wing B",
                latitude = 18.5210,
                longitude = 73.8572,
                contactPhone = "+91 98900 33445",
                isStopped = false
            ),
            SocietyEventEntity(
                title = "Youth Career Guidance & AI Skill Seminar",
                category = "Career Guidance",
                date = "22 Aug 2026",
                time = "05:00 PM - 07:30 PM",
                venue = "Clubhouse Audio-Visual Room",
                description = "Expert panel with Senior Software Engineers & University Professors guiding 10th/12th/College students on modern career paths, AI skills & scholarships.",
                organizer = "Education Advisory Group",
                isRegistered = true,
                registrationCount = 38,
                posterUri = "https://picsum.photos/seed/careerai/600/350",
                locationName = "AV Room, Sapana Park Activity Block",
                latitude = 18.5208,
                longitude = 73.8565,
                contactPhone = "+91 98500 55667",
                isStopped = false
            ),
            SocietyEventEntity(
                title = "Ganesh Utsav 2026 Grand Celebration & Cultural Night",
                category = "Festival",
                date = "27 Aug 2026",
                time = "07:00 PM Onwards",
                venue = "Central Courtyard Garden",
                description = "Grand Welcome Sthapana, Bhajan Sandhya, Kid's Fancy Dress, Rangoli Competition, and Mahaprasad distribution.",
                organizer = "Ganesh Utsav Mandal Sapana Park",
                isRegistered = false,
                registrationCount = 120,
                posterUri = "https://picsum.photos/seed/ganeshutsav/600/350",
                locationName = "Main Courtyard Lawn, Central Garden",
                latitude = 18.5202,
                longitude = 73.8569,
                contactPhone = "+91 97630 77889",
                isStopped = false
            )
        )

        val initialCommitteeMembers = listOf(
            CommitteeMember(
                id = 1,
                name = "Shri Vijay Deshmukh",
                designation = "Chairman",
                wingFlat = "B-501",
                phone = "+91 98220 11223",
                email = "chairman@sapanapark.org"
            ),
            CommitteeMember(
                id = 2,
                name = "Smt. Sunita Patil",
                designation = "Secretary",
                wingFlat = "A-202",
                phone = "+91 98900 33445",
                email = "secretary@sapanapark.org"
            ),
            CommitteeMember(
                id = 3,
                name = "Shri Ramesh Kulkarni",
                designation = "Treasurer",
                wingFlat = "C-104",
                phone = "+91 98500 55667",
                email = "treasurer@sapanapark.org"
            ),
            CommitteeMember(
                id = 4,
                name = "Shri Amit Shah",
                designation = "Managing Committee Member",
                wingFlat = "A-401",
                phone = "+91 97630 77889",
                email = "amit.shah@sapanapark.org"
            )
        )

        val initialDocuments = listOf(
            DocumentItem(
                id = 1,
                title = "Sapana Park CHS Registered Bye-Laws (2026)",
                category = "Society Bye-Laws",
                date = "Jan 2026",
                fileSize = "3.2 MB",
                description = "Complete Maharashtra Co-operative Societies Bye-Laws document as approved by District Registrar."
            ),
            DocumentItem(
                id = 2,
                title = "32nd AGM Minutes of Meeting (2025)",
                category = "AGM Minutes",
                date = "Aug 2025",
                fileSize = "1.1 MB",
                description = "Official ratified minutes, resolutions passed, and election details from last year's AGM."
            ),
            DocumentItem(
                id = 3,
                title = "Statutory Financial Audit Report (FY 2025-26)",
                category = "Audit Reports",
                date = "May 2026",
                fileSize = "2.4 MB",
                description = "Chartered Accountant audited balance sheet, income statement, and reserve funds summary."
            ),
            DocumentItem(
                id = 4,
                title = "Tenant Police Verification Application & Guidelines",
                category = "Tenant Forms",
                date = "Jul 2026",
                fileSize = "850 KB",
                description = "Mandatory police clearance submission form for flat owners renting out their premises."
            ),
            DocumentItem(
                id = 5,
                title = "No Objection Certificate (NOC) for Flat Renovation",
                category = "NOC Forms",
                date = "Jun 2026",
                fileSize = "420 KB",
                description = "Application form for structural modifications, interior woodwork, and debris disposal rules."
            ),
            DocumentItem(
                id = 6,
                title = "EV Charging Station & Meter Installation Request",
                category = "Application Forms",
                date = "Jul 2026",
                fileSize = "510 KB",
                description = "NOC request form for private EV charger setup in allotted parking bay."
            )
        )

        val initialFamilyMembers = listOf(
            FamilyMemberEntity(name = "Priya Sharma", relation = "Spouse", age = 38, phone = "+91 98765 11111"),
            FamilyMemberEntity(name = "Aarav Sharma", relation = "Son", age = 14, phone = "+91 98765 22222"),
            FamilyMemberEntity(name = "Ananya Sharma", relation = "Daughter", age = 9, phone = "")
        )

        val initialVehicles = listOf(
            VehicleEntity(vehicleType = "4-Wheeler", registrationNo = "MH-12-SP-3456", makeModel = "Hyundai Creta (White)", parkingBay = "Slot A-14"),
            VehicleEntity(vehicleType = "2-Wheeler", registrationNo = "MH-12-EX-8910", makeModel = "Honda Activa 6G (Grey)", parkingBay = "Slot A-14B")
        )

        val initialAchievements = listOf(
            AchievementItem(
                id = 1,
                title = "Class 10th Board Exam Topper (98.4%)",
                nomineeName = "Master Aarav Sharma",
                flatNo = "A-304",
                category = "Academic Excellence",
                description = "Secured 1st rank in CBSE Class X Board Examination with 100/100 in Mathematics & Science.",
                date = "May 2026"
            ),
            AchievementItem(
                id = 2,
                title = "State Level Badminton Gold Medalist",
                nomineeName = "Ms. Sneha Kulkarni",
                flatNo = "C-104",
                category = "Sports Pride",
                description = "Represented Pune district and won 1st Place in U-19 Girls Singles Championship.",
                date = "Jun 2026"
            ),
            AchievementItem(
                id = 3,
                title = "Best Green & Eco-Friendly Society Award",
                nomineeName = "Sapana Park Managing Committee",
                flatNo = "Society Office",
                category = "Society Honor",
                description = "Awarded by Municipal Corporation for 100% Waste Segregation, Rainwater Harvesting & Solar Lighting.",
                date = "Jul 2026"
            )
        )

        val initialGalleryPhotos = listOf(
            GalleryPhotoItem(id = 1, title = "Tree Plantation Drive", eventName = "Environment Day 2026", date = "June 2026", tag = "Green Drive"),
            GalleryPhotoItem(id = 2, title = "Yoga in the Garden", eventName = "International Yoga Day", date = "June 2026", tag = "Health"),
            GalleryPhotoItem(id = 3, title = "Diwali Illuminations", eventName = "Festival of Lights", date = "Nov 2025", tag = "Festivals"),
            GalleryPhotoItem(id = 4, title = "Republic Day Flag Hoisting", eventName = "National Event", date = "Jan 2026", tag = "Patriotic")
        )

        val initialResidentDocuments = listOf(
            ResidentDocumentEntity(
                id = 1,
                documentType = "Aadhaar Card",
                documentNumber = "XXXX-XXXX-4921",
                holderName = "Rajesh Sharma",
                status = "Verified ✓",
                dateUploaded = "10 Jan 2026",
                notes = "National UID - Verified by Society Admin"
            ),
            ResidentDocumentEntity(
                id = 2,
                documentType = "Police Verification Certificate",
                documentNumber = "PVC-PUNE-2026-8891",
                holderName = "Rajesh Sharma",
                status = "Verified ✓",
                dateUploaded = "12 Jan 2026",
                notes = "Katraj Police Station Clearance"
            ),
            ResidentDocumentEntity(
                id = 3,
                documentType = "Flat Allotment / Possession Letter",
                documentNumber = "POSS-SP-A304",
                holderName = "Rajesh Sharma",
                status = "Verified ✓",
                dateUploaded = "15 Jan 2021",
                notes = "Original Society Builder Copy"
            )
        )

        val initialUser = UserEntity(
            flatNo = "A-304",
            name = "Rajesh Sharma",
            role = "MEMBER",
            wing = "A-Wing",
            phone = "+91 98765 43210",
            email = "rajesh.sharma@sapanapark.org",
            isPromotionAllowed = true
        )

        val initialUtilityPayments = listOf(
            UtilityPaymentEntity(
                id = 1,
                transactionId = "TXN8921043291",
                userFlatNo = "A-304",
                userName = "Rajesh Sharma",
                billType = "Electricity Bill",
                billerName = "MSEDCL Maharashtra Electricity",
                consumerNumber = "049012389102",
                amount = 1450.0,
                paymentDate = "28 Jul 2026, 04:15 PM",
                paymentMode = "Google Pay UPI",
                status = "SUCCESSFUL",
                receiptNumber = "REC-SP-88219"
            ),
            UtilityPaymentEntity(
                id = 2,
                transactionId = "TXN8921011823",
                userFlatNo = "A-304",
                userName = "Rajesh Sharma",
                billType = "House Rent",
                billerName = "Landlord Rent Account (A-304)",
                consumerNumber = "RENT-AGR-2026",
                amount = 12000.0,
                paymentDate = "01 Jul 2026, 10:30 AM",
                paymentMode = "SBI NetBanking",
                status = "SUCCESSFUL",
                receiptNumber = "REC-SP-77102"
            )
        )
    }
}
