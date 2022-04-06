package runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    glue = {"steps"},
    features = "src/test/resources/features/",
    tags = "@ui",
    strict = true,
    monochrome = true)
public class RunnerUiTest {}
