package com.demosocket.blog.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Getter
@NoArgsConstructor
@RedisHash("hashCode")
public class RegistrationHashCode implements Serializable {

    private static final int EXPIRATION = 86400000;
    private static final long serialVersionUID = 1942146604489206625L;

    private String email;
    private String registrationHash;
    private Date expiryDate;

    public RegistrationHashCode(String email) {
        this.email = email;
        this.registrationHash = UUID.randomUUID().toString();
        this.expiryDate = calculateExpiryDate();
    }

    private Date calculateExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MILLISECOND, RegistrationHashCode.EXPIRATION);
        return new Date(cal.getTime().getTime());
    }

    public boolean checkExpiryDate() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        return expiryDate.getTime() >= cal.getTime().getTime();
    }
}
