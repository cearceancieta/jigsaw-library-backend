package com.ceaa.jigsawlibrary.jigsaw.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class SaveJigsawRequestDto {
    @NotBlank(message = "must not be blank")
    private String title;
    private String subtitle;
    private String collection;
    @NotBlank(message = "must not be blank")
    private String brand;
    @NotBlank(message = "must not be blank")
    private String shape;
    @NotNull(message = "must not be null")
    @Positive(message = "must be greater than 0")
    private Integer nPieces;
}
