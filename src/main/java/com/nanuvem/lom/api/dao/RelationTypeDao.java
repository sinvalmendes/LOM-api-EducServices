package com.nanuvem.lom.api.dao;

import java.util.List;

import com.nanuvem.lom.api.RelationType;

public interface RelationTypeDao {
    RelationType create(RelationType relationType);

    RelationType findRelationTypeById(Long id);

    RelationType update(RelationType relationType);

    List<RelationType> listAllRelationTypes();

    void delete(Long id);
}
