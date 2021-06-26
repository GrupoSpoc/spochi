package com.spochi.repository.fiware.ngsi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("NGSIQueryBuilder Test | Unit")
class NGSIQueryBuilderTest {

    private static final NGSIEntityType testEntityType = () -> "testEntity";

    @Test
    @DisplayName("build | when params is empty | should return empty string")
    void buildParamsEmpty() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();

        assertEquals("", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with type | ok")
    void buildWithTypeOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.type(testEntityType);

        assertEquals("?type=testEntity", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with single attributeEq | ok")
    void buildWithSingleAttributeEqOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attributeEQ(NGSITestFields.A_ATTRIBUTE, "aValue");

        assertEquals("?q=aAttribute==aValue", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with multiple values for same attributeEQ | ok")
    void buildWithMultipleAttributeEqOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attributeEQ(NGSITestFields.A_ATTRIBUTE, "aValue", "bValue");

        assertEquals("?q=aAttribute==aValue,bValue", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with attributeLS | ok")
    void buildWithSingleAttributeLsOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attributeLS(NGSITestFields.A_ATTRIBUTE, "aValue");

        assertEquals("?q=aAttribute<aValue", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with attributeGT | ok")
    void buildWithSingleAttributeGtOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attributeGT(NGSITestFields.A_ATTRIBUTE, "aValue");

        assertEquals("?q=aAttribute>aValue", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with count | ok")
    void buildWithCountOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.count();

        assertEquals("?options=count", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with keyValues | ok")
    void buildWithKeyValuesOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.keyValues();

        assertEquals("?options=keyValues", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with limit | ok")
    void buildWithLimitOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.limit(5);

        assertEquals("?limit=5", queryBuilder.build());
    }

    @Test
    @DisplayName("build | one | ok")
    void buildOneOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.one();

        assertEquals("?limit=1", queryBuilder.build());
    }

    @Test
    @DisplayName("build | ref | ok")
    void buildRefOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.ref(testEntityType, "test-id");

        assertEquals("?q=reftestEntity==test-id", queryBuilder.build());
    }

    @Test
    @DisplayName("build | ref eq two attributes, one ls and same gt | ok")
    void buildRefAndTwoAttributes() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attributeEQ(NGSITestFields.A_ATTRIBUTE, "aValue", "bValue");
        queryBuilder.attributeLS(NGSITestFields.A_ATTRIBUTE, "4");
        queryBuilder.attributeGT(NGSITestFields.A_ATTRIBUTE, "1");

        queryBuilder.ref(testEntityType, "test-id");

        assertEquals("?q=aAttribute==aValue,bValue;aAttribute<4;aAttribute>1;reftestEntity==test-id", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with get attribute | ok")
    void buildWithGetAttributeOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.getAttribute(NGSITestFields.A_ATTRIBUTE);

        assertEquals("?attr=aAttribute", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with order by desc | ok")
    void buildWithOrderByDescOk() {
        final NGSIField field = mock(NGSIField.class);
        when(field.label()).thenReturn("aOrder");

        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.orderByDesc(field);

        assertEquals("?orderBy=!aOrder", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with offset | ok")
    void buildWithOffsetOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.offset(1);

        assertEquals("?offset=1", queryBuilder.build());
    }


    @Test
    @DisplayName("build | with multiple params | ok")
    void buildWithMultipleParamsOk() {
        final NGSIField field = mock(NGSIField.class);
        when(field.label()).thenReturn("bOrder");

        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();

        queryBuilder.type(testEntityType)
                .attributeEQ(NGSITestFields.B_ATTRIBUTE, "bValue")
                .getAttribute(NGSITestFields.B_ATTRIBUTE)
                .keyValues()
                .offset(2)
                .orderByDesc(field)
                .limit(5);

        assertEquals("?q=bAttribute==bValue&offset=2&options=keyValues&limit=5&orderBy=!bOrder&type=testEntity&attr=bAttribute", queryBuilder.build());
    }

    @Test
    @DisplayName("comparator | ok")
    void comparatorOk() {
        assertEquals("=", NGSIQueryBuilder.comparator("key"));
        assertEquals("", NGSIQueryBuilder.comparator("key>"));
        assertEquals("", NGSIQueryBuilder.comparator("key<"));
    }

}