package com.nanuvem.lom.api.tests.relationtype;

import static com.nanuvem.lom.api.tests.relationtype.RelationTypeHelper.createRelationType;
import junit.framework.Assert;

import org.junit.Test;

import com.nanuvem.lom.api.Cardinality;
import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.MetadataException;
import com.nanuvem.lom.api.RelationType;
import com.nanuvem.lom.api.tests.LomTestCase;
import com.nanuvem.lom.api.tests.entity.EntityHelper;

public abstract class CreateRelationTypeTest extends LomTestCase {

    @Test
    public void createRelationTypeWithStringName() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Assert.assertEquals("RelationType", facade.findRelationTypeById(relationType.getId()).getName());
        Assert.assertEquals(1, facade.listAllRelationTypes().size());
        Assert.assertEquals(relationType.getSourceCardinality(), Cardinality.ONE);
        Assert.assertEquals(relationType.getTargetCardinality(), Cardinality.ONE);
    }

    @Test
    public void getsExceptionWhenCreateARelationTypeWithoutName() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        try {
            createRelationType(null, sourceEntity, targetEntity, null, null, false, null);
        } catch (MetadataException me) {
            Assert.assertEquals("Name is mandatory!", me.getMessage());
        }
    }

    @Test
    public void createRelationTypeWithValidSourceAndTargetEntity() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, Cardinality.ONE,
                Cardinality.ONE, false, null);
        Assert.assertEquals(sourceEntity, relationType.getSourceEntityType());
        Assert.assertEquals(targetEntity, relationType.getTargetEntityType());
        Assert.assertEquals(relationType.getSourceCardinality(), Cardinality.ONE);
        Assert.assertEquals(relationType.getTargetCardinality(), Cardinality.ONE);
    }

    @Test
    public void getsExceptionWhenCreateARelationTypeWithoutSourceEntity() {
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        try {
            createRelationType("RelationType", null, targetEntity, null, null, false, null);
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid value for source entity: The source entity is mandatory", me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenCreateARelationTypeWithoutTargetEntity() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        try {
            createRelationType("RelationType", sourceEntity, null, null, null, false, null);
        } catch (MetadataException me) {
            Assert.assertEquals("Invalid value for target entity: The target entity is mandatory", me.getMessage());
        }
    }

    @Test
    public void sourceCardinalityDefaultValueIsONE() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Assert.assertEquals(relationType.getSourceCardinality(), Cardinality.ONE);
    }

    @Test
    public void targetCardinalityDefaultValueIsONE() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Assert.assertEquals(relationType.getTargetCardinality(), Cardinality.ONE);
    }

    @Test
    public void bidirectionalDefaultValueIsFalse() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = new RelationType();
        relationType.setSourceEntityType(sourceEntity);
        relationType.setTargetEntityType(targetEntity);
        Assert.assertFalse(relationType.isBidirectional());
    }

    @Test
    public void reverseNameShouldBeNullWhenBidirectionalIsFalse() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        Assert.assertNull(relationType.getReverseName());
        Assert.assertEquals(relationType.getSourceCardinality(), Cardinality.ONE);
        Assert.assertEquals(relationType.getTargetCardinality(), Cardinality.ONE);
    }

    @Test
    public void getsExceptionBecauseTheReverseNameIsMandatoryIfBidirectionalIsTrue() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        try {
            createRelationType("RelationType", sourceEntity, targetEntity, null, null, true, null);
        } catch (MetadataException me) {
            Assert.assertEquals("ReverseName is mandatory when the relationship is bidirectional!", me.getMessage());
        }
    }

    @Test
    public void createABidirectionalRelationTypeWithReverseName() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, true,
                "ReverseName");
        Assert.assertEquals(relationType.getSourceCardinality(), Cardinality.ONE);
        Assert.assertEquals(relationType.getTargetCardinality(), Cardinality.ONE);
        Assert.assertEquals("ReverseName", relationType.getReverseName());
    }

    @Test
    public void getsExceptionWhenCreatesARelationTypeWithAReverseNameAndBidirectionalIsFalse() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        try {
            createRelationType("RelationType", sourceEntity, targetEntity, null, null, false, "ReverseName");
        } catch (MetadataException me) {
            Assert.assertEquals("ReverseName is not necessary when the relationship is no bidirectional!",
                    me.getMessage());
        }
    }

    @Test
    public void getsExceptionWhenSetAReverseNameOnANonBidirectionalRelationType() {
        EntityType sourceEntity = EntityHelper.createAndSaveOneEntity("namespace", "SourceEntity");
        EntityType targetEntity = EntityHelper.createAndSaveOneEntity("namespace", "TargetEntity");
        RelationType relationType = createRelationType("RelationType", sourceEntity, targetEntity, null, null, false,
                null);
        try {
            relationType.setReverseName("ReverseName");
            facade.update(relationType);
        } catch (MetadataException me) {
            Assert.assertEquals("ReverseName is not necessary when the relationship is no bidirectional!",
                    me.getMessage());
        }
    }

}
