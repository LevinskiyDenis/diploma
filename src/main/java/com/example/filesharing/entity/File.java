package com.example.filesharing.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String mimetype;

    @Column(nullable = false)
    private LocalDateTime lastedited;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(nullable = false)
    private byte[] file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_credentials_id", referencedColumnName = "id")
    private UserCredentials userCredentials;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File fileobj = (File) o;
        return name.equals(fileobj.name) && file.equals(fileobj.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, file);
    }

}
