//
//  IncommIosHomeChallengeInterviewUITests.swift
//  IncommIosHomeChallengeInterviewUITests
//
//  Created by FabioCunha on 14/02/26.
//

import XCTest

final class IncommIosHomeChallengeInterviewUITests: XCTestCase {

    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    override func tearDownWithError() throws {
    }

    @MainActor
    func testUserListDisplay() throws {
        // UI tests must launch the application that they test.
        let app = XCUIApplication()
        app.launch()

        // Verify Navigation Bar title
        let navBar = app.navigationBars["Users"]
        XCTAssertTrue(navBar.waitForExistence(timeout: 5), "Users navigation bar should appear on launch")
        
        // Verify Collection View exists
        let collectionView = app.collectionViews.firstMatch
        XCTAssertTrue(collectionView.waitForExistence(timeout: 5), "User list collection view should be visible")
        
        // Note: Without mocking the network layer in the app structure (e.g. via Launch Arguments),
        // we cannot deterministically assert specific cells unless we control the backend response.
        // For this challenge, ensuring the UI hierarchy loads is the primary integration test.
    }

    @MainActor
    func testLaunchPerformance() throws {
        if #available(macOS 10.15, iOS 13.0, tvOS 13.0, watchOS 7.0, *) {
            // This measures how long it takes to launch your application.
            measure(metrics: [XCTApplicationLaunchMetric()]) {
                XCUIApplication().launch()
            }
        }
    }
}
