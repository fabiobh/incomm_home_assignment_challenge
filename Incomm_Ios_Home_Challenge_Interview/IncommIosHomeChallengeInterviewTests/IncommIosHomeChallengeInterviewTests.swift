//
//  IncommIosHomeChallengeInterviewTests.swift
//  IncommIosHomeChallengeInterviewTests
//
//  Created by FabioCunha on 14/02/26.
//

import XCTest
@testable import IncommIosHomeChallengeInterview

// MARK: - Mocks

final class MockNetworkService: NetworkServiceProtocol {
    var result: Result<[UserDTO], Error>?
    
    func fetchUsers() async throws -> [UserDTO] {
        guard let result = result else {
            fatalError("Result not set in MockNetworkService")
        }
        switch result {
        case .success(let users):
            return users
        case .failure(let error):
            throw error
        }
    }
}

final class MockUserRepository: UserRepository {
    var result: Result<[User], Error>?
    
    func getUsers() async throws -> [User] {
        guard let result = result else {
            fatalError("Result not set in MockUserRepository")
        }
        switch result {
        case .success(let users):
            return users
        case .failure(let error):
            throw error
        }
    }
}

final class MockFetchUsersUseCase: FetchUsersUseCase {
    var result: Result<[User], Error>?
    
    func execute() async throws -> [User] {
        guard let result = result else {
            fatalError("Result not set in MockFetchUsersUseCase")
        }
        switch result {
        case .success(let users):
            return users
        case .failure(let error):
            throw error
        }
    }
}

final class MockUserListViewModelDelegate: UserListViewModelDelegate {
    var didUpdateUsersCalled = false
    var didFailCalled = false
    var errorReceived: Error?
    
    var asyncExpectation: XCTestExpectation?
    
    func didUpdateUsers() {
        didUpdateUsersCalled = true
        asyncExpectation?.fulfill()
    }
    
    func didFail(with error: Error) {
        didFailCalled = true
        errorReceived = error
        asyncExpectation?.fulfill()
    }
}

// MARK: - Extensions for Testing

extension User: Equatable {
    public static func == (lhs: User, rhs: User) -> Bool {
        return lhs.id == rhs.id &&
               lhs.name == rhs.name &&
               lhs.email == rhs.email &&
               lhs.avatarURL == rhs.avatarURL &&
               lhs.role == rhs.role &&
               lhs.department == rhs.department
    }
}

// MARK: - Tests

final class UserRepositoryTests: XCTestCase {
    var sut: UserRepositoryImpl!
    var mockNetworkService: MockNetworkService!
    
    override func setUp() {
        super.setUp()
        mockNetworkService = MockNetworkService()
        sut = UserRepositoryImpl(networkService: mockNetworkService)
    }
    
    override func tearDown() {
        sut = nil
        mockNetworkService = nil
        super.tearDown()
    }
    
    func test_getUsers_success_mapsDTOsToUsers() async throws {
        // Given
        let userDTO = UserDTO(id: 1, firstName: "John", lastName: "Doe", email: "john@example.com", avatarUrl: "http://example.com/avatar.png", role: "Developer", department: "Engineering")
        mockNetworkService.result = .success([userDTO])
        
        // When
        let users = try await sut.getUsers()
        
        // Then
        XCTAssertEqual(users.count, 1)
        let user = users.first!
        XCTAssertEqual(user.id, 1)
        XCTAssertEqual(user.name, "John Doe")
        XCTAssertEqual(user.email, "john@example.com")
        XCTAssertEqual(user.avatarURL?.absoluteString, "http://example.com/avatar.png")
        XCTAssertEqual(user.role, "Developer")
        XCTAssertEqual(user.department, "Engineering")
    }
    
    func test_getUsers_failure_throwsError() async {
        // Given
        let expectedError = NetworkError.serverError(statusCode: 500)
        mockNetworkService.result = .failure(expectedError)
        
        // When/Then
        do {
            _ = try await sut.getUsers()
            XCTFail("Expected error to be thrown")
        } catch {
            // Success
            XCTAssertTrue(error is NetworkError)
        }
    }
}

final class FetchUsersUseCaseTests: XCTestCase {
    var sut: FetchUsersUseCaseImpl!
    var mockUserRepository: MockUserRepository!
    
    override func setUp() {
        super.setUp()
        mockUserRepository = MockUserRepository()
        sut = FetchUsersUseCaseImpl(userRepository: mockUserRepository)
    }
    
    override func tearDown() {
        sut = nil
        mockUserRepository = nil
        super.tearDown()
    }
    
    func test_execute_success_returnsUsers() async throws {
        // Given
        let user = User(id: 1, name: "John Doe", email: "john@example.com", avatarURL: nil, role: "Dev", department: "Eng")
        mockUserRepository.result = .success([user])
        
        // When
        let users = try await sut.execute()
        
        // Then
        XCTAssertEqual(users, [user])
    }
    
    func test_execute_failure_throwsError() async {
        // Given
        struct TestError: Error {}
        mockUserRepository.result = .failure(TestError())
        
        // When/Then
        do {
            _ = try await sut.execute()
            XCTFail("Expected error to be thrown")
        } catch {
            XCTAssertTrue(error is TestError)
        }
    }
}

final class UserListViewModelTests: XCTestCase {
    var sut: UserListViewModel!
    var mockUseCase: MockFetchUsersUseCase!
    var mockDelegate: MockUserListViewModelDelegate!
    
    @MainActor
    override func setUp() {
        super.setUp()
        mockUseCase = MockFetchUsersUseCase()
        mockDelegate = MockUserListViewModelDelegate()
        sut = UserListViewModel(fetchUsersUseCase: mockUseCase)
        sut.delegate = mockDelegate
    }
    
    override func tearDown() {
        sut = nil
        mockUseCase = nil
        mockDelegate = nil
        super.tearDown()
    }
    
    @MainActor
    func test_loadUsers_success_updatesUsersAndCallsDelegate() async {
        // Given
        let user = User(id: 1, name: "John Doe", email: "john@example.com", avatarURL: nil, role: "Dev", department: "Eng")
        mockUseCase.result = .success([user])
        
        let expectation = XCTestExpectation(description: "Delegate updated")
        mockDelegate.asyncExpectation = expectation
        
        // When
        sut.loadUsers()
        
        // Then
        await fulfillment(of: [expectation], timeout: 1.0)
        
        XCTAssertTrue(mockDelegate.didUpdateUsersCalled)
        XCTAssertFalse(mockDelegate.didFailCalled)
        XCTAssertEqual(sut.users.count, 1)
        XCTAssertEqual(sut.users.first, user)
        XCTAssertEqual(sut.numberOfItems(), 1)
    }
    
    @MainActor
    func test_loadUsers_failure_callsDelegateWithError() async {
        // Given
        struct TestError: Error {}
        mockUseCase.result = .failure(TestError())
        
        let expectation = XCTestExpectation(description: "Delegate failed")
        mockDelegate.asyncExpectation = expectation
        
        // When
        sut.loadUsers()
        
        // Then
        await fulfillment(of: [expectation], timeout: 1.0)
        
        XCTAssertFalse(mockDelegate.didUpdateUsersCalled)
        XCTAssertTrue(mockDelegate.didFailCalled)
        XCTAssertTrue(mockDelegate.errorReceived is TestError)
        XCTAssertEqual(sut.users.count, 0)
    }
}
