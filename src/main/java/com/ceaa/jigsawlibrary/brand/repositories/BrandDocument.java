package com.ceaa.jigsawlibrary.brand.repositories;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@EqualsAndHashCode
@Document("brands")
public class BrandDocument {
    @Id
    private String id;
    private String name;
}
