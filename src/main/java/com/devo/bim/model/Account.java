package com.devo.bim.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.DATE;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;
    private String password;
    private long companyId;
    private String userName;
    private String rank;
    private String phoneNo;
    private String mobileNo;
    private String address;
    private String languageCode;
    private String photo;
    private Integer enabled;
    private long writerId;
    @Temporal(DATE)
    @DateTimeFormat(pattern = "YYYY-MM-dd")
    private Date writeDate;
}
