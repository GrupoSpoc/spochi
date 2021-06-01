package com.spochi.service.query;

import com.spochi.entity.InitiativeStatus;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class InitiativeQuery {
    private InitiativeSorter sorter;
    private List<InitiativeStatus> statuses;
    private String userId;
    private LocalDateTime dateTop;
    private Integer limit;

    public void withSorter(@Nullable Integer sorterId) {
        if (sorterId != null) {
            this.sorter = InitiativeSorter.fromIdOrElseThrow(sorterId);
        }
    }

    public void withStatuses(@Nullable Integer[] statusId) {
        if (statusId != null) {
            this.statuses = Arrays.stream(statusId).map(InitiativeStatus::fromIdOrElseThrow).collect(Collectors.toList());
        }
    }

    public void withUserId(@NotNull String userId) {
        this.userId = userId;
    }

    public void withDateTop(@Nullable String dateFrom) {
        if (dateFrom != null) {
            this.dateTop = LocalDateTime.parse(dateFrom);
        }
    }

    public void withLimit(@Nullable Integer limit) {
        if (limit != null) {
            this.limit = limit;
        }
    }
}
