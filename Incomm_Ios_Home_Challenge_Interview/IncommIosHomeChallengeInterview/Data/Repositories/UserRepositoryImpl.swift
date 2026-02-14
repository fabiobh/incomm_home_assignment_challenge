import Foundation

final class UserRepositoryImpl: UserRepository {
    private let networkService: NetworkServiceProtocol

    init(networkService: NetworkServiceProtocol) {
        self.networkService = networkService
    }

    func getUsers() async throws -> [User] {
        let userDTOs = try await networkService.fetchUsers()
        return userDTOs.map { dto in
            User(
                id: dto.id,
                name: "\(dto.firstName) \(dto.lastName)",
                email: dto.email,
                avatarURL: URL(string: dto.avatarUrl ?? ""),
                role: dto.role,
                department: dto.department
            )
        }
    }
}
