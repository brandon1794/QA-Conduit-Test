@ui
Feature: TestConduit User Actions
  This feature file includes all TestConduit types of tests that can be executed from an user perspective.

  Background: Navigating to TestConduit Page
    Given User navigates to TestConduit page

  @signUp-conduit
  Scenario: TestConduit user sign up actions
    Then User clicks to sign up button
    And User fills information required to sign up
    Then User should be able to see new options to navigate when logged in the nav tab bar

  @login-conduit
  Scenario: TestConduit user login actions
    Then User clicks to login into account
    And User fills information required to login
    Then User should be able to see new options to navigate when logged in the nav tab bar

  @new-post-conduit
  Scenario: TestConduit user New Post actions
    Then User clicks to login into account
    And User fills information required to login
    Then User should be able to see new options to navigate when logged in the nav tab bar
    Then User clicks to add a New Post
    And User fills information to publish a New Post
