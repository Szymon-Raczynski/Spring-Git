package sda.poznan.pl.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sda.poznan.pl.domain.CommitData;
import sda.poznan.pl.domain.GitHubData;
import sda.poznan.pl.domain.OwnerData;
import sda.poznan.pl.errorHanding.SDAException;
import sda.poznan.pl.repository.GitHubDataRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitRepoServiceTest {

    private final static String URL = "https://api.github.com/repos/{owner}/{repo}";
    private final static String URL_COMMIT = "https://api.github.com/repos/{owner}/{repo}/commits";
    private final static String REPO = "testRepo";
    private final static String USER = "testUser";
    private final static String FULL_NAME = USER + "/" + REPO;

    //@Mock
    //private OwnerDataRepository ownerDataRepository;
    @Mock
    private GitHubDataRepository gitHubDataRepository;
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private GitRepoService gitRepoService;


    @Test
    public void shouldReturnValidResponseForQuery() {
        //given
        OwnerData ownerData = new OwnerData();
        ownerData.setLogin("test_login");
        ownerData.setSiteAdmin(false);

        GitHubData gitHubData = new GitHubData();
        gitHubData.setFullName("test_name");
        gitHubData.setOwner(ownerData);
        gitHubData.setDescription("test_description");

        when(restTemplate.getForObject(any(String.class), eq(GitHubData.class),
                any(String.class), any(String.class))).thenReturn(gitHubData);
        when(gitHubDataRepository.existsByFullName(FULL_NAME)).thenReturn(false);
        //when
        //when(ownerDataRepository.existsByLogin("user")).thenReturn(true);
        GitHubData underTest = gitRepoService.getRepoByUserAndRepoName(USER, REPO);
        //then
        assertThat(underTest.getFullName()).isEqualTo(gitHubData.getFullName());
    }

    @Test
    public void shouldGetErrorWhen4xxFromGitHub() {
        //given
        String errorMessage = "test_error";
        when(restTemplate.getForObject(URL, GitHubData.class,
                USER, REPO))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN, errorMessage));
        //when
        GitHubData underTest = gitRepoService.getRepoByUserAndRepoName(USER, REPO);
        //then
        assertThat(underTest.getError()).isEqualTo(HttpStatus.FORBIDDEN.value() + " " + errorMessage);

    }

    @Test
    public void shouldReturnListOfCommits() {
        //given
        String url1 = "url_1";
        String url2 = "url_2";
        CommitData[] commitData = new CommitData[3];
        commitData[0] = new CommitData();
        commitData[1] = new CommitData();
        commitData[1].setUrl(url1);
        commitData[2] = new CommitData();
        commitData[2].setUrl(url2);

        when(restTemplate.getForObject(URL_COMMIT, CommitData[].class,
                USER, REPO)).thenReturn(commitData);
        //when
        List<CommitData> underTest = gitRepoService.getCommitsByUserAndRepoName(USER, REPO);
        //then
        assertThat(underTest.size()).isEqualTo(commitData.length);
        assertThat(underTest.stream()
                .map(CommitData::getUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList())).containsExactlyInAnyOrder(url1, url2);
    }

    @Test(expected = SDAException.class)
    public void shouldThrowSDAException() {
        //given
        String errorMessage = "test_error";
        when(restTemplate.getForObject(URL_COMMIT, CommitData[].class, USER, REPO))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN, errorMessage));
        //when
        gitRepoService.getCommitsByUserAndRepoName(USER, REPO);
    }

}
