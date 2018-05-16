package sda.poznan.pl.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sda.poznan.pl.WebDemoApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebDemoApplication.class)
@AutoConfigureMockMvc
public class GitRepoControllerITest {

    private static final String URL = "/getRepository/{owner}/{repo}";
    private static final String COMMITS = "/commits";
    private static final String REPO = "java5pozHibernate";
    private static final String USER = "lukasz-bacic";
    private static final String NOT_FOUND = "404 Not Found";
    private static final String ERROR_USER = "lukasz-bac";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnValidResponse() throws Exception {
        mockMvc.perform(get(URL, USER, REPO)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.owner.login")
                        .value(USER));
    }

    @Test
    public void shouldGetErrorWhen4xxFromGitHub() throws Exception {
        mockMvc.perform(get(URL, ERROR_USER, USER, REPO)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$")
                        .value(NOT_FOUND));
    }

    @Test
    public void should4xxErrorWhenAskingForCommits() throws Exception {
        mockMvc.perform(get(URL + COMMITS, ERROR_USER, USER, REPO)).andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$")
                        .value(NOT_FOUND));
    }

    @Test
    public void shouldReturnListOfCommits() throws Exception {
        mockMvc.perform(get(URL + COMMITS, USER, REPO)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commit.author.name")
                        .value("lbacic"));
    }

}
