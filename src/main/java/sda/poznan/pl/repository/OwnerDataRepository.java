package sda.poznan.pl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sda.poznan.pl.domain.OwnerData;

@Repository
public interface OwnerDataRepository extends JpaRepository<OwnerData, Long> {

    OwnerData getByLogin(String login);

    boolean existsByLogin(String login);
}
