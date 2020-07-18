package me.liuweiqiang.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.liuweiqiang.App;
import me.liuweiqiang.Student;
import org.junit.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.TimeUnit;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = RANDOM_PORT)
public class DemoTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @LocalServerPort
    private int port;

    private MockMvc mockMvc;
    private static FirefoxDriver firefoxDriver;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
//                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @BeforeClass
    public static void openFirefox() {
//        System.setProperty("webdriver.chrome.driver", "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome");
    }

    @AfterClass
    public static void closeFirefox() {
    }

    @Test
    public void maintest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/demo"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "lwq",
    password = "123456",
    roles = "LWQ")
    public void case002() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/demo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Qiang"));
    }

    private Student okdeserialize(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        return objectMapper.readValue(response, Student.class);
    }

    @Test
    public void integTest() {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBasicAuth("lwq", "123456");
            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/demo", HttpMethod.GET,
                    httpEntity, String.class);

            if(HttpStatus.OK.equals(response.getStatusCode())) {
                Student student = okdeserialize(response.getBody()); //根据状态码的不同反序列化
                Assert.assertEquals("Qiang", student.getName());
            } else
                Assert.fail();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void seleniumTest() throws JsonProcessingException {
        System.setProperty("webdriver.gecko.driver", "/Users/code/Downloads/geckodriver"); //需要先下载geckodriver
        firefoxDriver = new FirefoxDriver();
        firefoxDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        String baseUrl = "http://lwq:123456@localhost:" + port + "/demo";
        firefoxDriver.get(baseUrl);
        Student student = okdeserialize(firefoxDriver.findElementById("json").getText());

        if (firefoxDriver != null) {
            firefoxDriver.quit();
        }

        Assert.assertEquals("Qiang", student.getName());
    }
}
