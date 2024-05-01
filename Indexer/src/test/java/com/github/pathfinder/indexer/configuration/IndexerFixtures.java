package com.github.pathfinder.indexer.configuration;

import java.time.Duration;
import static org.mockito.Mockito.timeout;
import lombok.experimental.UtilityClass;
import org.mockito.verification.VerificationWithTimeout;

@UtilityClass
public class IndexerFixtures {

    public static VerificationWithTimeout VERIFICATION_TIMEOUT = timeout(Duration.ofSeconds(5).toMillis());

}
