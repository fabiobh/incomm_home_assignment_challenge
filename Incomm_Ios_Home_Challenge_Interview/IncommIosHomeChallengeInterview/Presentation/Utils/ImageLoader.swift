import UIKit

enum ImageLoaderError: Error {
    case invalidURL
    case serverError
    case unknown
}

final class ImageLoader {
    static let shared = ImageLoader()
    private let cache = NSCache<NSString, UIImage>()

    private init() {}

    func loadImage(from url: URL) async throws -> UIImage? {
        let key = url.absoluteString as NSString

        if let cachedImage = cache.object(forKey: key) {
            return cachedImage
        }

        let (data, response) = try await URLSession.shared.data(from: url)

        guard let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) else {
            throw ImageLoaderError.serverError
        }

        if let image = UIImage(data: data) {
            cache.setObject(image, forKey: key)
            return image
        }

        return nil
    }
}
