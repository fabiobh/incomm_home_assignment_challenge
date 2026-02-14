package com.uaialternativa.incommandroidhomechallengeinterview.domain.model

/**
 * DOMAIN LAYER
 *
 * This class represents the pure business entity (Model).
 * It does not depend on third-party libraries (like Gson or Retrofit) or Android frameworks.
 * The goal is to keep business logic isolated and independent of data or UI implementation.
 */
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String?, // Phone can be null in JSON
    val avatarUrl: String?, // Avatar can be null
    val role: String,
    val department: String,
    val isActive: Boolean,
    val joinedDate: String
)
