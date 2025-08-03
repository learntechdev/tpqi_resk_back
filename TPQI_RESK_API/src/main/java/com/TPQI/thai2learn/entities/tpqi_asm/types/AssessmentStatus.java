package com.TPQI.thai2learn.entities.tpqi_asm.types;

import java.util.stream.Stream;

public enum AssessmentStatus {
    PENDING_SUBMISSION(0, "ยังไม่ส่งประเมิน"),
    SUBMITTED(1, "ส่งประเมินแล้ว"),
    MORE_EVIDENCE_REQUESTED(2, "ขอหลักฐานเพิ่มเติม"),
    EVALUATED_PASS(3, "ประเมินผลแล้ว (ผ่าน)"),
    EVALUATED_FAIL(4, "ประเมินผลแล้ว (ไม่ผ่าน)"),
    CANCELLED(5, "ยกเลิกผลการประเมินแล้ว");

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