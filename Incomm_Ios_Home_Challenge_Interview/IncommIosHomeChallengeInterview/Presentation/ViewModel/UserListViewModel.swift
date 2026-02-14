import Foundation

@MainActor
protocol UserListViewModelDelegate: AnyObject {
    func didUpdateUsers()
    func didFail(with error: Error)
}

@MainActor
final class UserListViewModel {
    private let fetchUsersUseCase: FetchUsersUseCase
    private(set) var users: [User] = []
    
    weak var delegate: UserListViewModelDelegate?

    init(fetchUsersUseCase: FetchUsersUseCase) {
        self.fetchUsersUseCase = fetchUsersUseCase
    }

    func loadUsers() {
        Task {
            do {
                self.users = try await fetchUsersUseCase.execute()
                self.delegate?.didUpdateUsers()
            } catch {
                self.delegate?.didFail(with: error)
            }
        }
    }

    func numberOfItems() -> Int {
        return users.count
    }

    func user(at index: Int) -> User {
        return users[index]
    }
}
