package com.TPQI.thai2learn.entities.tpqi_asm.types;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.TPQI.thai2learn.DTO.StatusDTO;

public enum AssessmentStatus {
    JUST_START(0, "ยังไม่ได้ประเมิน"),
    PENDING_SUBMISSION(1, "ยังไม่ส่งประเมิน"),
    SUBMITTED(2, "ส่งประเมินแล้ว"),
    MORE_EVIDENCE_REQUESTED(3, "เจ้าหน้าที่สอบขอหลักฐานเพิ่ม"),
    EVALUATED_PASS(4, "ประเมินผลแล้ว (ผ่าน)"),
    EVALUATED_FAIL(5, "ประเมินผลแล้ว (ไม่ผ่าน)"),
    CANCELLED(6, "ยกเลิกผลการประเมินแล้ว");

    private final int code;
    private final String displayName;

    private AssessmentStatus(int code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static AssessmentStatus of(int code) {
        return Stream.of(AssessmentStatus.values())
                .filter(s -> s.getCode() == code)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static List<StatusDTO> getDistinctStatusDTOs() {
        Map<String, StatusDTO> map = Arrays.stream(values())
                .map(status -> {
                    String displayName = switch (status) {
                        case EVALUATED_PASS, EVALUATED_FAIL -> "ประเมินผลแล้ว";
                        default -> status.getDisplayName();
                    };
                    return new StatusDTO(status.getCode(), displayName);
                })
                .collect(Collectors.toMap(
                        StatusDTO::getDisplayName,
                        dto -> dto,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        return map.values().stream()
                .sorted(Comparator.comparing(StatusDTO::getKey))
                .collect(Collectors.toList());
    }
}