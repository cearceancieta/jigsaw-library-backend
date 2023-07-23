package com.ceaa.jigsawlibrary.jigsaw.controllers;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class JigsawDto {
    private String id;
    private String title;
    private String subtitle;
    private String collection;
    private String brand;
    private String shape;
    private Integer nPieces;
}
