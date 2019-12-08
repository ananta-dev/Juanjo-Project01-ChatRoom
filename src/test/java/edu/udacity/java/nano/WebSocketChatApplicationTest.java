package edu.udacity.java.nano;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

// @RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@ContextConfiguration(locations= "classpath:**/application-context.xml")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketChatApplicationTest extends BaseSeleniumTests {

    @Test
    public void login() {
        this.driver.get("http://localhost:8080");
        WebElement usernameElement = this.driver.findElement(By.id("username"));
        WebElement submitElement = this.driver.findElement(By.className("submit"));
        // Check that there is a username and a submit button
        Assert.assertNotNull(usernameElement);
        Assert.assertNotNull(submitElement);
        usernameElement.sendKeys("Juanjo");
        submitElement.click();
        // Is the chat page being loaded after login?
        String expectedURL = "http://localhost:8080/index?username=Juanjo";
        String actualURL = this.driver.getCurrentUrl();
        Assert.assertEquals(actualURL, expectedURL);
    }

    @Test
    public void join() {
        this.driver.get("http://localhost:8080/index?username=Juanjo");
        String enterText = driver.findElement(By.className("message-content-enter")).getText();
        String chatnumText = driver.findElement(By.className("chat-num")).getText();
        // Check that the text includes "... joined the chat" and
        // that there is one user in the chat room
        Assert.assertTrue("Text not found!", enterText.contains("joined the chat."));
        Assert.assertTrue("Text not found!", chatnumText.contains("1"));
    }
    @Test
    public void chat() {
        this.driver.get("http://localhost:8080/index?username=Juanjo");
        WebElement msgElement = this.driver.findElement(By.id("msg"));
        WebElement sendElement = this.driver.findElement(By.className("mdui-send"));
        Assert.assertNotNull(msgElement);
        Assert.assertNotNull(sendElement);
        msgElement.sendKeys("Hi all!");
        sendElement.click();
        WebElement myDynamicElement = (new WebDriverWait(driver, 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("message-content-msg")));
        // Check that the message has been added to the webpage.
        String msgText = driver.findElement(By.className("message-content-msg")).getText();
        Assert.assertTrue("Text not found!", msgText.contains("Hi all!"));
    }

    @Test
    public void leave() {
        this.driver.get("http://localhost:8080/index?username=Juanjo");
        WebElement leaveElement = this.driver.findElement(By.className("mdui-btn-icon"));
        leaveElement.click();
        // Check that we are back in the login page
        WebElement inputElement = this.driver.findElement(By.id("username"));
        WebElement submitElement2 = this.driver.findElement(By.className("submit"));
        Assert.assertNotNull(inputElement);
        Assert.assertNotNull(submitElement2);

    }

}
