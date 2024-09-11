package com.example.sirmafinalexam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @Column(name= "team_id", unique = true)
    private int id;

    @Column(name= "team_name")
    private String name;

    @Column(name= "manager_full_name")
    private String managerFullName;

    @Column(name= "`group`")
    private String group;
}
