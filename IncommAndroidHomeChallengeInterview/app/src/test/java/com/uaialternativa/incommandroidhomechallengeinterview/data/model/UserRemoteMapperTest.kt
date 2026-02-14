package com.uaialternativa.incommandroidhomechallengeinterview.data.model

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * UNIT TEST
 *
 * This class tests the `toDomain()` extension function (Mapper).
 * 
 * Goal: Verify if the transformation from Data Model (UserRemote) to Domain Model (User)
 * is happening correctly, mapping all fields as expected.
 */
class UserRemoteMapperTest {

    @Test
    fun `toDomain should map UserRemote to User correctly`() {
        // Arrange (Prepare data)
        val userRemote = UserRemote(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            phone = "123456",
            avatarUrl = "http://avatar.com/1",
            role = "Dev",
            department = "IT",
            isActive = true,
            joinedDate = "2023-01-01"
        )

        // Act (Execute the action)
        val userDomain = userRemote.toDomain()

        // Assert (Verify results)
        assertEquals(userRemote.id, userDomain.id)
        assertEquals(userRemote.firstName, userDomain.firstName)
        assertEquals(userRemote.lastName, userDomain.lastName)
        assertEquals(userRemote.email, userDomain.email)
        assertEquals(userRemote.phone, userDomain.phone)
        assertEquals(userRemote.avatarUrl, userDomain.avatarUrl)
        assertEquals(userRemote.role, userDomain.role)
        assertEquals(userRemote.department, userDomain.department)
        assertEquals(userRemote.isActive, userDomain.isActive)
        assertEquals(userRemote.joinedDate, userDomain.joinedDate)
    }
}
