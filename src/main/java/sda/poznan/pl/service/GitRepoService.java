package sda.poznan.pl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import sda.poznan.pl.domain.CommitData;
import sda.poznan.pl.domain.GitHubData;
import sda.poznan.pl.errorHanding.SDAException;
import sda.poznan.pl.repository.CommitDataRepository;
import sda.poznan.pl.repository.GitHubDataRepository;
import sda.poznan.pl.repository.OwnerDataRepository;

import java.util.Arrays;
import java.util.List;

@Service
public class GitRepoService {

    private final static String URL = "https://api.github.com/repos/{owner}/{repo}";
    private final static String COMMITS = "/commits";

    private RestTemplate restTemplate;
    private GitHubDataRepository gitHubDataRepository;
    private CommitDataRepository commitDataRepository;
    private OwnerDataRepository ownerDataRepository;

    @Autowired
    public GitRepoService(RestTemplate restTemplate,
                          GitHubDataRepository gitHubDataRepository,
                          CommitDataRepository commitDataRepository,
                          OwnerDataRepository ownerDataRepository) {
        this.restTemplate = restTemplate;
        this.gitHubDataRepository = gitHubDataRepository;
        this.commitDataRepository = commitDataRepository;
        this.ownerDataRepository = ownerDataRepository;
    }

    @Transactional
    public GitHubData getRepoByUserAndRepoName(String username, String repositoryName) {
        try {
            String fullName = getFullName(username, repositoryName);
            if (gitHubDataRepository.existsByFullName(fullName)) {
                return gitHubDataRepository.getByFullName(fullName);
            } else {
                GitHubData response = restTemplate.getForObject(URL,
                        GitHubData.class, username, repositoryName);
                if (ownerDataRepository.existsByLogin(response.getOwner().getLogin())) {
                    response.setOwner(ownerDataRepository.
                            getByLogin(response.getOwner().getLogin()));
                }
                gitHubDataRepository.save(response);
                return response;
            }
        } catch (HttpClientErrorException ex) {
            GitHubData errorResponse = new GitHubData();
            errorResponse.setError(ex.getMessage());
            return errorResponse;
        }
    }

    public List<CommitData> getCommitsByUserAndRepoName(String username, String repositoryName) {
        try {
            String fullName = getFullName(username, repositoryName);
            if (commitDataRepository.existsByUrlContaining(fullName)) {
                return commitDataRepository.getAllByUrlContaining(fullName);
            } else {
                CommitData[] response = restTemplate.getForObject(URL + COMMITS,
                        CommitData[].class, username, repositoryName);
                List<CommitData> commitDataList = Arrays.asList(response);
                commitDataList = commitDataList.size() > 3 ? commitDataList.subList(0, 3)
                        : commitDataList;
                commitDataRepository.saveAll(commitDataList);
                return commitDataList;
            }
        } catch (HttpClientErrorException ex) {
            throw new SDAException(ex.getMessage());
        }
    }

    private static String getFullName(String userName, String repoName) {
        return String.format("%s/%s", userName, repoName);
    }
}