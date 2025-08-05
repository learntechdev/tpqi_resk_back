package com.TPQI.thai2learn.entities.tpqi_asm.types;

import java.util.stream.Stream;

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
}