package org.example.springbootlog.domain.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @Column(name = "NO", nullable = false)
    private Long no;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "BALANCE", nullable = false)
    private Long balance;

    public static Account empty(String name) {
        return new Account(0L, name, 0L);
    }
}
