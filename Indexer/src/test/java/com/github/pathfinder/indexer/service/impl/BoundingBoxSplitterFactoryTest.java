package com.github.pathfinder.indexer.service.impl;

import com.github.pathfinder.indexer.client.osm.OsmClient;
import com.github.pathfinder.indexer.configuration.SplitterConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoundingBoxSplitterFactoryTest {

    @InjectMocks
    BoundingBoxSplitterFactory target;

    @Mock
    OsmClient osmClient;

    @Mock
    SplitterConfiguration configuration;

    @Test
    void create_Request_CreateFromConfiguration() {
        assertThat(target.create()).isNotNull();

        verify(configuration).getElementsLimit();
        verify(configuration).getAdditionalSpaceMeters();
    }

}