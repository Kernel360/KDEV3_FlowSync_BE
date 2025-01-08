package com.checkping.domain.member;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization")
@Entity
public class Organization {
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
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

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

    public enum Type {
        DEVELOPER, CUSTOMER
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}