package sda.poznan.pl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sda.poznan.pl.domain.AuthorData;

@Repository
public interface AuthorDataRepository extends JpaRepository<AuthorData, Long> {
}
