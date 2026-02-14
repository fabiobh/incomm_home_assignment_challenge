package com.uaialternativa.incommandroidhomechallengeinterview.data.model

import com.google.gson.annotations.SerializedName
import com.uaialternativa.incommandroidhomechallengeinterview.domain.model.User

/**
 * DATA LAYER
 *
 * Data models specific to API response (DTOs - Data Transfer Objects).
 * They contain specific annotations from the serialization library (Gson) to map the JSON.
 */
data class UserResponse(
    @SerializedName("status") val status: String,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("users") val users: List<UserRemote>
)

data class UserRemote(
    @SerializedName("id") val id: Int,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("role") val role: String,
    @SerializedName("department") val department: String,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("joined_date") val joinedDate: String
)

/**
 * MAPPER
 *
 * Extension function to convert the API model (UserRemote) to the Domain model (User).
 * This ensures the domain layer is not polluted with API implementation details (like @SerializedName annotations).
 */
fun UserRemote.toDomain() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    phone = phone,
    avatarUrl = avatarUrl,
    role = role,
    department = department,
    isActive = isActive,
    joinedDate = joinedDate
)
