package com.cydeo.entity;

import antlr.BaseAST;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "roles")
@Entity
public class Role extends BaseEntity {
    private String description;

}
