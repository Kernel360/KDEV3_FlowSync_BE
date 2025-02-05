package com.checkping.domain.member;

import com.checkping.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization")
@Entity
public class Organization extends BaseEntity {
    /*
      id : 업체아이디
      type : 업체유형
      status : 업체상태
      brNumber : 사업자등록번호
      name : 업체명
      regAt : 등록일시
      brCertificateUrl : 사업자등록증 이미지 링크
      streetAddress : 도로명주소
      detailAddress : 상세주소
      phoneNumber : 전화번호
      remark : 비고
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "br_number", length = 100)
    private String brNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @CreatedDate
    @Column(name = "reg_at", updatable = false, nullable = false)
    private LocalDateTime regAt;

    @Column(name = "br_certificate_url", columnDefinition = "TEXT")
    private String brCertificateUrl;

    @Column(name = "street_address", length = 255)
    private String streetAddress;

    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    @Column(name = "phone_number", length = 100)
    private String phoneNumber;

    @Column(length = 100)
    private String remark;

    @Column(name = "reason_for_delete_organization", length = 100)
    private String reasonForDeleteOrganization;

    public enum Type {
        DEVELOPER, CUSTOMER
    }

    public enum Status {
        ACTIVE, INACTIVE, DELETED
    }

    public void updateOrganization(
            String brNumber,
            String brCertificateUrl,
            String streetAddress,
            String detailAddress,
            String phoneNumber
    ) {
        this.brNumber = brNumber;
        this.brCertificateUrl = brCertificateUrl;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
        this.phoneNumber = phoneNumber;
    }

    public void deleteOragnization(String reason) {
        this.status = Status.DELETED;
        this.reasonForDeleteOrganization = reason;
    }

    public void changeStatus(String reason) {
        if (this.status == Status.ACTIVE) {
            this.status = Status.INACTIVE;
            this.reasonForDeleteOrganization = reason;
        } else {
            this.status = Status.ACTIVE;
            this.reasonForDeleteOrganization = "";
        }
    }

}