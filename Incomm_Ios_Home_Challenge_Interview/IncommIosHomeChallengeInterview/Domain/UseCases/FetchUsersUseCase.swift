import Foundation

protocol FetchUsersUseCase {
    func execute() async throws -> [User]
}

final class FetchUsersUseCaseImpl: FetchUsersUseCase {
    private let userRepository: UserRepository

    init(userRepository: UserRepository) {
        self.userRepository = userRepository
    }

    func execute() async throws -> [User] {
        return try await userRepository.getUsers()
    }
}
