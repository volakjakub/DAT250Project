package tech.jakubvolak.dat250project.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Device implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id")
    private final Collection<Vote> votes = new ArrayList<>();

    public Device() {}
    public Long getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public Collection<Vote> getVotes() {
        return votes;
    }
}
