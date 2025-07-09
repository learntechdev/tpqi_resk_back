package com.TPQI.thai2learn.repositories.ext_data;

import com.TPQI.thai2learn.DTO.TPQIProfessionDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TPQIProfessionRepositoryImpl implements TPQIProfessionRepository {

    @PersistenceContext(unitName = "sqlServerEntityManager")
    private EntityManager entityManager;

    @Override
    public List<TPQIProfessionDTO> getTpqiProfessionData() {
        String sql = """
            WITH RankedData AS (
                SELECT 
                    st1.standard_id AS tpqi_profession_profession_id,
                    st1.tier1_code AS tpqi_profession_profession_code,
                    st1.tier1_name AS tpqi_profession_profession_name,
                    st1.state AS tpqi_profession_status,
                    ROW_NUMBER() OVER (PARTITION BY st1.standard_id ORDER BY cer.id DESC) AS rn
                FROM standard_cer_level cer
                LEFT JOIN standard_tier1 st1 ON cer.tier1_id = st1.id
                WHERE st1.id IS NOT NULL
            )
            SELECT 
                tpqi_profession_profession_id,
                tpqi_profession_profession_code,
                tpqi_profession_profession_name,
                tpqi_profession_status
            FROM RankedData
            WHERE rn = 1
        """;

List<Object[]> results = entityManager.createNativeQuery(sql).getResultList();
List<TPQIProfessionDTO> dtos = new ArrayList<>();

for (Object[] row : results) {
    TPQIProfessionDTO dto = new TPQIProfessionDTO();
    dto.setTpqiProfessionProfessionId(row[0] != null ? ((Number) row[0]).intValue() : 0);
    dto.setTpqiProfessionProfessionCode((String) row[1]);
    dto.setTpqiProfessionProfessionName((String) row[2]);
    dto.setTpqiProfessionStatus((String) row[3]);
    dtos.add(dto);
}

return dtos;

    }
}
