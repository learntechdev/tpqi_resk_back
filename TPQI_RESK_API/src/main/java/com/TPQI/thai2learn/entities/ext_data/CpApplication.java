package com.TPQI.thai2learn.entities.ext_data;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cp_applications", schema = "dbo")  // ปกติ schema บน SQL Server คือ dbo
public class CpApplication {

    @Id
    private int id;

    @Column(name = "application_code")
    private String applicationCode;

    @Column(name = "person_id")
    private int personId;

    @Column(name = "application_date")
    private Date applicationDate;

    @Column(name = "fullname_thai")
    private String fullnameThai;

    @Column(name = "fullname_en")
    private String fullnameEn;

    @Column(name = "assessment_type")
    private String assessmentType;

    @Column(name = "exam_opening_id")
    private Integer examOpeningId;

    @Column(name = "mou_id")
    private Integer mouId;

    @Column(name = "mou_category_id")
    private Integer mouCategoryId;

    @Column(name = "mou_type_id")
    private Integer mouTypeId;

    @Column(name = "status")
    private String status;

    @Column(name = "imported_id")
    private Integer importedId;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_used")
    private String isUsed;

    @Column(name = "fee_amount")
    private Double feeAmount;

    @Column(name = "fee_payment_status")
    private String feePaymentStatus;

    @Column(name = "fee_payment_date")
    private Date feePaymentDate;

    @Column(name = "Ref1")
    private String ref1;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "attend_exam")
    private String attendExam;


    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getFullnameThai() {
        return fullnameThai;
    }

    public void setFullnameThai(String fullnameThai) {
        this.fullnameThai = fullnameThai;
    }

    public String getFullnameEn() {
        return fullnameEn;
    }

    public void setFullnameEn(String fullnameEn) {
        this.fullnameEn = fullnameEn;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public Integer getExamOpeningId() {
        return examOpeningId;
    }

    public void setExamOpeningId(Integer examOpeningId) {
        this.examOpeningId = examOpeningId;
    }

    public Integer getMouId() {
        return mouId;
    }

    public void setMouId(Integer mouId) {
        this.mouId = mouId;
    }

    public Integer getMouCategoryId() {
        return mouCategoryId;
    }

    public void setMouCategoryId(Integer mouCategoryId) {
        this.mouCategoryId = mouCategoryId;
    }

    public Integer getMouTypeId() {
        return mouTypeId;
    }

    public void setMouTypeId(Integer mouTypeId) {
        this.mouTypeId = mouTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getImportedId() {
        return importedId;
    }

    public void setImportedId(Integer importedId) {
        this.importedId = importedId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }

    public Double getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Double feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getFeePaymentStatus() {
        return feePaymentStatus;
    }

    public void setFeePaymentStatus(String feePaymentStatus) {
        this.feePaymentStatus = feePaymentStatus;
    }

    public Date getFeePaymentDate() {
        return feePaymentDate;
    }

    public void setFeePaymentDate(Date feePaymentDate) {
        this.feePaymentDate = feePaymentDate;
    }

    public String getRef1() {
        return ref1;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getAttendExam() {
        return attendExam;
    }

    public void setAttendExam(String attendExam) {
        this.attendExam = attendExam;
    }
}
