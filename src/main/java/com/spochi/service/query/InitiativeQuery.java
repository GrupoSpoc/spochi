package com.spochi.service.query;

import com.spochi.entity.InitiativeStatus;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InitiativeQuery {
    private InitiativeSorter sorter;
    private InitiativeStatus status;
    private String userId;
    private LocalDateTime dateFrom;

    public void withSorter(@Nullable Integer sorterId) {
        if (sorterId != null) {
            this.sorter = InitiativeSorter.fromIdOrElseThrow(sorterId);
        }
    }

    public void withStatus(@Nullable Integer statusId) {
        if (statusId != null) {
            this.status = InitiativeStatus.fromIdOrElseThrow(statusId);
        }
    }

    public void withUserId(@NotNull String userId) {
        this.userId = userId;
    }

    public void withDateFrom(@Nullable String dateFrom) {
        if (dateFrom != null) {
            this.dateFrom = LocalDateTime.parse(dateFrom);
        }
    }
}
