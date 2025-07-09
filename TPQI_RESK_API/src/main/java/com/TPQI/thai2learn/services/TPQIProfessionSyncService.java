package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.entities.tpqi_asm.TPQIProfessionMySQLEntity;
import com.TPQI.thai2learn.repositories.tpqi_asm.TPQIProfessionMySQLRepository;

import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TPQIProfessionSyncService {

    @PersistenceContext(unitName = "sqlServerEntityManager")
    private EntityManager sqlServerEntityManager;

    @Autowired
    private TPQIProfessionMySQLRepository mysqlRepository;

    public void syncTPQIProfessions() {
        String sql = """
            WITH RankedData AS (
                SELECT 
                    st1.standard_id AS professionId,
                    st1.tier1_code AS professionCode,
                    st1.tier1_name AS professionName,
                    st1.state AS status,
                    ROW_NUMBER() OVER (PARTITION BY st1.standard_id ORDER BY cer.id DESC) AS rn
                FROM standard_cer_level cer
                LEFT JOIN standard_tier1 st1 ON cer.tier1_id = st1.id
                WHERE st1.id IS NOT NULL
            )
            SELECT professionId, professionCode, professionName, status
            FROM RankedData
            WHERE rn = 1
        """;

        List<Object[]> resultList = sqlServerEntityManager.createNativeQuery(sql).getResultList();

        for (Object[] row : resultList) {
            int id = ((Number) row[0]).intValue();

            if (!mysqlRepository.existsById(id)) {
                TPQIProfessionMySQLEntity entity = new 	TPQIProfessionMySQLEntity();
                entity.setProfessionId(id);
                entity.setProfessionCode(row[1] != null ? row[1].toString() : null);
                entity.setProfessionName(row[2] != null ? row[2].toString() : null);
                entity.setStatus(row[3] != null ? row[3].toString() : null);

                mysqlRepository.save(entity);
            }
        }
    }
}
