import Foundation

enum NetworkError: Error {
    case invalidURL
    case noData
    case decodingError
    case serverError(statusCode: Int)
    case unknown(Error)
}

protocol NetworkServiceProtocol {
    func fetchUsers() async throws -> [UserDTO]
}

final class NetworkService: NetworkServiceProtocol {
    private let urlString = "https://oneqmock.proxy.beeceptor.com/incomm"
    private let session: URLSession

    init(session: URLSession = .shared) {
        self.session = session
    }

    func fetchUsers() async throws -> [UserDTO] {
        guard let url = URL(string: urlString) else {
            throw NetworkError.invalidURL
        }

        do {
            let (data, response) = try await session.data(from: url)

            guard let httpResponse = response as? HTTPURLResponse else {
                throw NetworkError.noData
            }

            guard (200...299).contains(httpResponse.statusCode) else {
                throw NetworkError.serverError(statusCode: httpResponse.statusCode)
            }

            let decoder = JSONDecoder()
            let responseDTO = try decoder.decode(UserListResponseDTO.self, from: data)
            return responseDTO.users
        } catch let error as NetworkError {
            throw error
        } catch let error as DecodingError {
            print("Decoding error: \(error)")
            throw NetworkError.decodingError
        } catch {
            print("Unknown network error: \(error)")
            throw NetworkError.unknown(error)
        }
    }
}
