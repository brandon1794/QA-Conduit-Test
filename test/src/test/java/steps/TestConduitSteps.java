package steps;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import TestConduit.TestConduit;

@Singleton
public class TestConduitSteps {
  private final TestConduit testConduit;
  private final String URL = "https://conduit-react-v1.netlify.app/";
  private final String PASSWORD = "12345678";

  @Inject
  public TestConduitSteps(TestConduit testConduit) {
    this.testConduit = testConduit;
  }

  @Given("^User navigates to TestConduit page$")
  public void userNavigatesToTestConduitPage() {
    this.testConduit.navigateToTestConduitPage(URL);
  }

  @Then("^User clicks to sign up button$")
  public void userClicksToSignUpButton() {
    this.testConduit.clickToSignUpAndRegisterAccount();
  }

  @And("^User fills information required to sign up$")
  public void userFillsInformationRequiredToSignUp() {
    this.testConduit.fillSignUpForm(PASSWORD);
  }

  @Then("^User clicks to login into account$")
  public void userClicksToLoginIntoAccount() {
    this.testConduit.clickToLoginIntoAccount();
  }

  @And("^User fills information required to login$")
  public void userFillsInformationRequiredToLogin() {
    this.testConduit.loginForm(PASSWORD);
  }

  @Then("^User should be able to see new options to navigate when logged in the nav tab bar$")
  public void userShouldBeAbleToSeeNewOptionsToNavigateWhenLoggedInTheNavTabBar() {
    this.testConduit.isProfileDisplayed();
    this.testConduit.isNewPostDisplayed();
  }

  @Then("^User clicks to add a New Post$")
  public void userClicksToAddANewPost() {
    this.testConduit.clickToNewPost();
  }

  @And("^User fills information to publish a New Post$")
  public void userFillsInformationToPublishANewPost() {
    this.testConduit.addNewPost();
    this.testConduit.newPostAdded();
  }
}
