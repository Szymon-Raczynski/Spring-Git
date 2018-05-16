package sda.poznan.pl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GitHubData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "id")
    private OwnerData owner;
    @JsonProperty("full_name")
    @Column(unique = true)
    private String fullName;
    private String description;
    private String url;
    @JsonProperty("stargazer_counts")
    private String stargazerCounts;
    @JsonProperty("commits_url")
    private String commitsUrl;
    @JsonProperty("watchers_count")
    private Integer watchersCount;
    @JsonIgnore
    private String error;

}
