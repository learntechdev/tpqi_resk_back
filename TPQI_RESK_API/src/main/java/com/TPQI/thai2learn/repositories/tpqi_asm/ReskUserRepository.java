package com.TPQI.thai2learn.repositories.tpqi_asm;

import com.TPQI.thai2learn.entities.tpqi_asm.ReskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReskUserRepository extends JpaRepository<ReskUser, Long> {

    Optional<ReskUser> findByUsername(String username);

    boolean existsByAppId(String appId);
}