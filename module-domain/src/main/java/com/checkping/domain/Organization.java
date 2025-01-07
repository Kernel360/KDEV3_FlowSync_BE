package com.checkping.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "organization")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Organization {

    @Id
    @UuidGenerator
    @GeneratedValue
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
