package com.nanuvem.lom.api.tests.entity;

import static com.nanuvem.lom.api.tests.entity.EntityHelper.createAndSaveOneEntity;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.createUpdateAndVerifyOneEntity;
import static com.nanuvem.lom.api.tests.entity.EntityHelper.expectExceptionOnInvalidEntityUpdate;

import org.junit.Assert;
import org.junit.Test;

import com.nanuvem.lom.api.EntityType;
import com.nanuvem.lom.api.tests.LomTestCase;

public abstract class UpdateEntityTest extends LomTestCase {

    @Test
    public void validNewNameAndPackage() {
        createUpdateAndVerifyOneEntity("a", "aaa1", "b", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa2", "a", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa3", "b", "aaa");
        createUpdateAndVerifyOneEntity("", "aaa1", "", "bbb");
        createUpdateAndVerifyOneEntity(null, "aaa2", null, "bbb");
        createUpdateAndVerifyOneEntity("a.b.c", "aaa1", "b", "bbb");
        createUpdateAndVerifyOneEntity("a.b.c", "aaa2", "b.c", "bbb");
    }

    @Test
    public void removePackageSetPackage() {
        createUpdateAndVerifyOneEntity("a", "aaa1", "", "aaa");
        createUpdateAndVerifyOneEntity("a", "aaa2", "", "bbb");
        createUpdateAndVerifyOneEntity("", "aaa1", "b", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa3", null, "aaa");
        createUpdateAndVerifyOneEntity("a", "aaa4", null, "bbb");
        createUpdateAndVerifyOneEntity(null, "aaa2", "b", "bbb");
        createUpdateAndVerifyOneEntity("a", "aaa5", "a", "aaa5");
        createUpdateAndVerifyOneEntity("a", "aaa6", "a", "aaa7");
        createUpdateAndVerifyOneEntity(null, "aaa3", null, "aaa4");
    }

    @Test
    public void renameCausingTwoEntitiesWithSameNameInDifferentPackages() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");

        ea.setName("bbb");
        facade.update(ea);
    }

    @Test
    public void moveCausingTwoEntitiesWithSameNameInDifferentPackages() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");

        ea.setNamespace("c");
        ea.setName("bbb");
        facade.update(ea);
    }

    @Test
    public void newNameAndPackageWithSpaces() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "name space", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace",
                "name space");
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "na me", INVALID_VALUE_FOR_ENTITY, "name", "na me");
    }

    @Test
    public void removeName() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", null, ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "", ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnInvalidEntityUpdate(ea, null, null, ENTITY_NAME_IS_MANDATORY);
        expectExceptionOnInvalidEntityUpdate(ea, null, "", ENTITY_NAME_IS_MANDATORY);
    }

    @Test
    public void renameMoveCausingTwoEntitiesWithSameNameInDefaultPackage() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");
        createAndSaveOneEntity("b", "aaa");
        createAndSaveOneEntity("a", "bbb");

        expectExceptionOnInvalidEntityUpdate(ea, "b", "bbb", ENTITY_ALREADY_EXISTS, "b.bbb");
        expectExceptionOnInvalidEntityUpdate(ea, "b", "aaa", ENTITY_ALREADY_EXISTS, "b.aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "bbb", ENTITY_ALREADY_EXISTS, "a.bbb");

        EntityType e1 = createAndSaveOneEntity("a.b.c", "aaa");
        EntityType e2 = createAndSaveOneEntity("b.c", "aaa");
        EntityType e3 = createAndSaveOneEntity("a.b.c", "bbb");
        createAndSaveOneEntity("b.c", "bbb");

        expectExceptionOnInvalidEntityUpdate(e1, "b.c", "bbb", ENTITY_ALREADY_EXISTS, "b.c.bbb");
        expectExceptionOnInvalidEntityUpdate(e2, "b.c", "bbb", ENTITY_ALREADY_EXISTS, "b.c.bbb");
        expectExceptionOnInvalidEntityUpdate(e3, "b.c", "bbb", ENTITY_ALREADY_EXISTS, "b.c.bbb");
    }

    @Test
    public void renameMoveCausingTwoEntitiesWithSameNameInAnonDefaultPackage() {
        EntityType ea1 = createAndSaveOneEntity("a", "aaa");
        EntityType ea2 = createAndSaveOneEntity(null, "aaa");
        EntityType ea3 = createAndSaveOneEntity("a", "bbb");
        createAndSaveOneEntity(null, "bbb");

        expectExceptionOnInvalidEntityUpdate(ea1, null, "bbb", ENTITY_ALREADY_EXISTS, "bbb");
        expectExceptionOnInvalidEntityUpdate(ea2, null, "bbb", ENTITY_ALREADY_EXISTS, "bbb");
        expectExceptionOnInvalidEntityUpdate(ea3, null, "bbb", ENTITY_ALREADY_EXISTS, "bbb");

        EntityType ec1 = createAndSaveOneEntity("a.b.c", "ccc");
        EntityType ec2 = createAndSaveOneEntity("", "ccc");
        EntityType ec3 = createAndSaveOneEntity("a.b.c", "ddd");
        createAndSaveOneEntity("", "ddd");

        expectExceptionOnInvalidEntityUpdate(ec1, "", "ddd", ENTITY_ALREADY_EXISTS, "ddd");
        expectExceptionOnInvalidEntityUpdate(ec2, "", "ddd", ENTITY_ALREADY_EXISTS, "ddd");
        expectExceptionOnInvalidEntityUpdate(ec3, "", "ddd", ENTITY_ALREADY_EXISTS, "ddd");
    }

    @Test
    public void renameMoveCausingNameAndPackagesWithInvalidChars() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa$", INVALID_VALUE_FOR_ENTITY, "name", "aaa$");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa#", INVALID_VALUE_FOR_ENTITY, "name", "aaa#");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa=", INVALID_VALUE_FOR_ENTITY, "name", "aaa=");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa'", INVALID_VALUE_FOR_ENTITY, "name", "aaa'");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa.a", INVALID_VALUE_FOR_ENTITY, "name", "aaa.a");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa/a", INVALID_VALUE_FOR_ENTITY, "name", "aaa/a");
        expectExceptionOnInvalidEntityUpdate(ea, "a", "aaa*", INVALID_VALUE_FOR_ENTITY, "name", "aaa*");
        expectExceptionOnInvalidEntityUpdate(ea, "a$", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a$");
        expectExceptionOnInvalidEntityUpdate(ea, "a#", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a#");
        expectExceptionOnInvalidEntityUpdate(ea, "a=", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a=");
        expectExceptionOnInvalidEntityUpdate(ea, "a'", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a'");
        // expectExceptionOnInvalidEntityUpdate(ea, "a.", "aaa",
        // INVALID_VALUE_FOR_ENTITY, "namespace", "a.");
        expectExceptionOnInvalidEntityUpdate(ea, "a/a", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a/a");
        expectExceptionOnInvalidEntityUpdate(ea, "a*", "aaa", INVALID_VALUE_FOR_ENTITY, "namespace", "a*");
    }

    @Test
    public void renameMoveForcingCaseInsentivePackagesAndNames() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        createAndSaveOneEntity("b", "bbb");
        createAndSaveOneEntity("CcC", "ccc");
        createAndSaveOneEntity("DDD", "ddd");

        expectExceptionOnInvalidEntityUpdate(ea, "b", "BbB", ENTITY_ALREADY_EXISTS, "b.bbb");
        expectExceptionOnInvalidEntityUpdate(ea, "b", "BBB", ENTITY_ALREADY_EXISTS, "b.bbb");
        expectExceptionOnInvalidEntityUpdate(ea, "ccc", "ccc", ENTITY_ALREADY_EXISTS, "ccc.ccc");
        expectExceptionOnInvalidEntityUpdate(ea, "CCC", "ccc", ENTITY_ALREADY_EXISTS, "ccc.ccc");
        expectExceptionOnInvalidEntityUpdate(ea, "ddd", "ddd", ENTITY_ALREADY_EXISTS, "ddd.ddd");
        expectExceptionOnInvalidEntityUpdate(ea, "ddd", "DDD", ENTITY_ALREADY_EXISTS, "ddd.ddd");
    }

    @Test
    public void invalidIdAndVersion() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");
        Long originalId = ea.getId();
        Integer originalVersion = ea.getVersion();

        ea.setId(null);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "The id of an Entity is mandatory on update");

        ea.setId(originalId);
        ea.setVersion(null);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "The version of an Entity is mandatory on update");

        ea.setId(null);
        ea.setVersion(null);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name",
                "The version and id of an Entity are mandatory on update");

        ea.setId(originalId + 1);
        ea.setVersion(originalVersion);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "Invalid id for Entity namespace.name");

        ea.setId(originalId);
        ea.setVersion(originalVersion - 1);
        expectExceptionOnInvalidEntityUpdate(ea, "namespace", "name", "Updating a deprecated version of Entity a.aaa. "
                + "Get the Entity again to obtain the newest version and proceed updating.");
    }

    @Test
    public void severalUpdates() {
        EntityType ea = createAndSaveOneEntity("a", "aaa");

        ea.setNamespace("b");
        ea.setName("abc");
        ea = facade.update(ea);

        ea.setNamespace("a.b.d");
        ea.setName("abc");
        ea = facade.update(ea);

        ea.setNamespace(null);
        ea.setName("abc");
        ea = facade.update(ea);

        ea.setNamespace("a.b.c");
        ea.setName("abc");
        ea = facade.update(ea);

        EntityType found = facade.findEntityById(ea.getId());
        Assert.assertEquals("a.b.c", found.getNamespace());
        Assert.assertEquals("abc", found.getName());
        Assert.assertEquals(new Long(1), found.getId());
        Assert.assertEquals(new Integer(4), found.getVersion());
    }
}
