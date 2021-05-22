package com.spochi.service.fiware.ngsi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("NGSIQueryBuilder Test | Unit")
class NGSIQueryBuilderTest {

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
        queryBuilder.type("aType");

        assertEquals("?type=aType", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with attribute | ok")
    void buildWithAttributeOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.attribute(UserTestFields.A_ATTRIBUTE, "aValue");

        assertEquals("?q=aAttribute==aValue", queryBuilder.build());
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
    @DisplayName("build | with get attribute | ok")
    void buildWithGetAttributeOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.getAttribute(UserTestFields.A_ATTRIBUTE);

        assertEquals("?attr=aAttribute", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with order by desc | ok")
    void buildWithOrderByDescOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.orderByDesc("aOrder");

        assertEquals("?orderBy=!aOrder", queryBuilder.build());
    }

    @Test
    @DisplayName("build | with multiple params | ok")
    void buildWithMultipleParamsOk() {
        final NGSIQueryBuilder queryBuilder = new NGSIQueryBuilder();
        queryBuilder.type("bType")
                .attribute(UserTestFields.B_ATTRIBUTE, "bValue")
                .getAttribute(UserTestFields.B_ATTRIBUTE)
                .keyValues()
                .orderByDesc("bOrder")
                .limit(5);


        assertEquals("?q=bAttribute==bValue&options=keyValues&limit=5&orderBy=!bOrder&type=bType&attr=bAttribute", queryBuilder.build());
    }

    private enum UserTestFields implements NGSIField {
        A_ATTRIBUTE("aAttribute"),
        B_ATTRIBUTE("bAttribute");

        private final String value;

        UserTestFields(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.value;
        }
    }
}