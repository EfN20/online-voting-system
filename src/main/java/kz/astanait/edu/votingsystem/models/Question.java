package kz.astanait.edu.votingsystem.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    @SequenceGenerator(
            name = "questions_sequence",
            sequenceName = "questions_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "questions_sequence"
    )
    @Column(name = "id")
    private Long id;

    @NonNull
    @Column(name = "title")
    private String title;

    @NonNull
    @OneToMany(
            fetch = FetchType.EAGER,
            orphanRemoval = true,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.REMOVE
            }
    )
    @JoinColumn(name = "option_id")
    private Set<Option> options = new LinkedHashSet<>();

    public void addOption(Option option) {
        voteCount += option.getVoteCount();
        options.add(option);
    }

    public void removeOption(Option option) {
        voteCount -= option.getVoteCount();
        options.remove(option);
    }

    @NonNull
    @Column(name = "vote_count")
    private Long voteCount;
}
