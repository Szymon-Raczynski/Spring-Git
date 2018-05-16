package sda.poznan.pl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sda.poznan.pl.domain.GitHubData;

@Repository
public interface GitHubDataRepository extends JpaRepository<GitHubData, Long> {

    GitHubData getByFullName(String fullName);

    boolean existsByFullName(String fullName);
}
