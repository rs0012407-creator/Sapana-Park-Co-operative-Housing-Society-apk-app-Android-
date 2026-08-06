package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ComplaintEntity
import com.example.data.model.EmergencyContactEntity
import com.example.data.model.FamilyMemberEntity
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

@Dao
interface SocietyDao {

    // Users Persistent Data
    @Query("SELECT * FROM users WHERE flatNo = :flatNo LIMIT 1")
    suspend fun getUserByFlat(flatNo: String): UserEntity?

    @Query("SELECT * FROM users WHERE phone = :phone LIMIT 1")
    suspend fun getUserByPhone(phone: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY flatNo DESC LIMIT 1")
    fun getLatestUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM users ORDER BY flatNo DESC LIMIT 1")
    suspend fun getLatestUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query("UPDATE users SET isPromotionAllowed = :isAllowed WHERE flatNo = :flatNo")
    suspend fun updatePromotionPermission(flatNo: String, isAllowed: Boolean)

    // Notices
    @Query("SELECT * FROM notices ORDER BY id DESC")
    fun getAllNotices(): Flow<List<NoticeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotice(notice: NoticeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotices(notices: List<NoticeEntity>)

    // Complaints
    @Query("SELECT * FROM complaints ORDER BY id DESC")
    fun getAllComplaints(): Flow<List<ComplaintEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: ComplaintEntity)

    @Update
    suspend fun updateComplaint(complaint: ComplaintEntity)

    @Query("DELETE FROM complaints WHERE id = :id")
    suspend fun deleteComplaintById(id: Int)

    // Bills
    @Query("SELECT * FROM bills ORDER BY id DESC")
    fun getAllBills(): Flow<List<MaintenanceBillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: MaintenanceBillEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBills(bills: List<MaintenanceBillEntity>)

    @Query("UPDATE bills SET isPaid = 1, paidDate = :paidDate, receiptNumber = :receiptNo, paymentMethod = :method WHERE id = :billId")
    suspend fun markBillAsPaid(billId: Int, paidDate: String, receiptNo: String, method: String)

    // Events
    @Query("SELECT * FROM events ORDER BY id DESC")
    fun getAllEvents(): Flow<List<SocietyEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: SocietyEventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEvents(events: List<SocietyEventEntity>)

    @Update
    suspend fun updateEvent(event: SocietyEventEntity)

    @Query("UPDATE events SET isRegistered = :isRegistered, registrationCount = registrationCount + (CASE WHEN :isRegistered = 1 THEN 1 ELSE -1 END) WHERE id = :eventId")
    suspend fun toggleEventRegistration(eventId: Int, isRegistered: Boolean)

    @Query("UPDATE events SET isStopped = :isStopped WHERE id = :eventId")
    suspend fun toggleEventStopped(eventId: Int, isStopped: Boolean)

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun deleteEventById(id: Int)

    // Family Members
    @Query("SELECT * FROM family_members ORDER BY id ASC")
    fun getAllFamilyMembers(): Flow<List<FamilyMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(member: FamilyMemberEntity)

    @Query("DELETE FROM family_members WHERE id = :id")
    suspend fun deleteFamilyMemberById(id: Int)

    // Vehicles
    @Query("SELECT * FROM vehicles ORDER BY id ASC")
    fun getAllVehicles(): Flow<List<VehicleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteVehicleById(id: Int)

    // Suggestions
    @Query("SELECT * FROM suggestions ORDER BY id DESC")
    fun getAllSuggestions(): Flow<List<SuggestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuggestion(suggestion: SuggestionEntity)

    // Resident Identity Documents
    @Query("SELECT * FROM resident_documents ORDER BY id DESC")
    fun getAllResidentDocuments(): Flow<List<ResidentDocumentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResidentDocument(doc: ResidentDocumentEntity)

    @Query("DELETE FROM resident_documents WHERE id = :id")
    suspend fun deleteResidentDocumentById(id: Int)

    // Utility & Bill Payments (User specific)
    @Query("SELECT * FROM utility_payments WHERE userFlatNo = :flatNo ORDER BY id DESC")
    fun getUtilityPaymentsForUser(flatNo: String): Flow<List<UtilityPaymentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUtilityPayment(payment: UtilityPaymentEntity)

    // Colony Meetings
    @Query("SELECT * FROM meetings ORDER BY id DESC")
    fun getAllMeetings(): Flow<List<SocietyMeetingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeeting(meeting: SocietyMeetingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMeetings(meetings: List<SocietyMeetingEntity>)

    @Query("UPDATE meetings SET isAttending = :isAttending, attendeeCount = attendeeCount + (CASE WHEN :isAttending = 1 THEN 1 ELSE -1 END) WHERE id = :meetingId")
    suspend fun toggleMeetingAttendance(meetingId: Int, isAttending: Boolean)

    @Query("DELETE FROM meetings WHERE id = :id")
    suspend fun deleteMeetingById(id: Int)

    // Emergency Contacts
    @Query("SELECT * FROM emergency_contacts ORDER BY id ASC")
    fun getAllEmergencyContacts(): Flow<List<EmergencyContactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyContact(contact: EmergencyContactEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllEmergencyContacts(contacts: List<EmergencyContactEntity>)

    @Query("DELETE FROM emergency_contacts WHERE id = :id")
    suspend fun deleteEmergencyContactById(id: Int)
}
