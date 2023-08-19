package com.github.pathfinder.web.dto.path;

import com.github.pathfinder.web.dto.HealthTypeDto;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class FindPathDtoTest {

    @Test
    void ctor_NullHealthStatus_DefaultToHealthy() {
        var actual = new FindPathDto(null, null, null);

        assertThat(actual)
                .extracting(FindPathDto::health)
                .isEqualTo(HealthTypeDto.HEALTHY);
    }

}
