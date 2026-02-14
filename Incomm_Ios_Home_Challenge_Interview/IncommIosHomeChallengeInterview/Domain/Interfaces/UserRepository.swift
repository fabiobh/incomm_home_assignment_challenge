import Foundation

protocol UserRepository {
    func getUsers() async throws -> [User]
}
