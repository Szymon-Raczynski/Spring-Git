package sda.poznan.pl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sda.poznan.pl.domain.SingleCommit;

@Repository
public interface SingleCommitRepository extends JpaRepository<SingleCommit, Long> {
}
