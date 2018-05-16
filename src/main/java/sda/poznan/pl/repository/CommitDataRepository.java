package sda.poznan.pl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sda.poznan.pl.domain.CommitData;

import java.util.List;

@Repository
public interface CommitDataRepository extends JpaRepository<CommitData, Long> {
    List<CommitData> getAllByUrlContaining(String urlPart);

    boolean existsByUrlContaining(String urlPath);
}
