package com.example.demo.vo.Category;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="category")
public class CategoryVO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;

    @Column(name="memberidx")
    private Long memberidx;

    @Column(name="category")
    private Integer category;

    @Column(name="categoryname")
    private String categoryname;

    @Column(name="legdate")
    private Date legdate;

    @Column(name="isdeleted")
    private Integer isdeleted;

}
