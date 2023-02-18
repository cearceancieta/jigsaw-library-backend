package com.ceaa.jigsawLibrary.jigsaw;

import java.util.UUID;

public interface JigsawRepository {
    Jigsaw get(UUID id);
}
