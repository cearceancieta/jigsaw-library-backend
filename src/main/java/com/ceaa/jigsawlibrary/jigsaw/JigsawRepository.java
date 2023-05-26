package com.ceaa.jigsawlibrary.jigsaw;

import java.util.List;

public interface JigsawRepository {
    Jigsaw get(String id);

    List<Jigsaw> find();

    Jigsaw save(Jigsaw jigsaw);
}
