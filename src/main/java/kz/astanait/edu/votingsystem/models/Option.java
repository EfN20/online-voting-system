package kz.astanait.edu.votingsystem.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "options")
public class Option {

    @Id
    @SequenceGenerator(
            name = "options_sequence",
            sequenceName = "options_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "options_sequence"
    )
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;
}
