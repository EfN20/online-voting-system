package kz.astanait.edu.votingsystem.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "id")
    private Long id;

    @NonNull
    @NotBlank(message = "First name should not be empty")
    @Size(max = 30, message = "Maximum possible length for First Name is 30 characters")
    @Column(name = "first_name")
    private String firstName;

    @NonNull
    @NotBlank(message = "Last name should not be empty")
    @Size(max = 30, message = "Max string length for First Name is 30")
    @Column(name = "last_name")
    private String lastName;

    @NonNull
    @NotBlank(message = "Nickname should not be empty")
    @Size(max = 30, message = "Max string length for Last Name is 30")
    @Column(name = "nickname", unique = true, updatable = false)
    private String nickname;

    @NonNull
    @Pattern(regexp = ".+@.+\\..+", message = "Email should be valid")
    @Size(max = 30, message = "Max string length for Nickname is 30")
    @Column(name = "email", unique = true)
    private String email;

    @NonNull
    @NotNull
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "group_id")
    private Group group;

    @NonNull
    @NotNull(message = "Age should not be empty")
    @Max(value = 100, message = "Age should not be greater than 100")
    @Min(value = 16, message = "You are underage (ЩЕГОЛ)")
    @Column(name = "age")
    private int age;

    @NonNull
    @NotBlank(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @NonNull
    @JsonBackReference
    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = CascadeType.MERGE
    )
    @JoinColumn(name = "role_id")
    private Role role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "users_interests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private Set<Interest> interests = new HashSet<>();

    public void addInterest(Interest interest) {
        interests.add(interest);
        interest.getUsers().add(this);
    }

    public void removeInterest(Interest interest) {
        interests.remove(interest);
        interest.getUsers().remove(this);
    }

}
