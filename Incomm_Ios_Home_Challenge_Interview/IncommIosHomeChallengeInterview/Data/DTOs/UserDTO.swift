import Foundation

struct UserListResponseDTO: Codable {
    let status: String
    let totalCount: Int
    let users: [UserDTO]
    
    enum CodingKeys: String, CodingKey {
        case status
        case totalCount = "total_count"
        case users
    }
}

struct UserDTO: Codable {
    let id: Int
    let firstName: String
    let lastName: String
    let email: String
    let avatarUrl: String?
    let role: String
    let department: String
    
    enum CodingKeys: String, CodingKey {
        case id
        case firstName = "first_name"
        case lastName = "last_name"
        case email
        case avatarUrl = "avatar_url"
        case role
        case department
    }
}
