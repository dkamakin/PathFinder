package com.github.pathfinder.indexer.service.impl;

import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.github.pathfinder.indexer.configuration.SplitterConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties.OsmTagConfiguration;
import com.github.pathfinder.indexer.configuration.osm.OsmConfigurationProperties.OsmTagValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoundingBoxSplitterFactoryTest {

    @InjectMocks
    BoundingBoxSplitterFactory target;

    @Mock
    SplitterConfiguration configuration;

    @Mock
    OsmConfigurationProperties osmConfigurationProperties;

    void whenNeedToReturnTags(Set<OsmTagConfiguration> tags) {
        when(osmConfigurationProperties.getTags()).thenReturn(tags);
    }

    @Test
    void create_Request_CreateFromConfiguration() {
        whenNeedToReturnTags(Set.of(new OsmTagConfiguration("natural", Set.of(new OsmTagValue("beach", 1D)))));

        assertThat(target.create()).isNotNull();

        verify(osmConfigurationProperties).getTags();
        verify(configuration).getElementsLimit();
        verify(configuration).getAdditionalSpaceMeters();
    }

}