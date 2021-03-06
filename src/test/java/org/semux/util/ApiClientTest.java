/**
 * Copyright (c) 2017-2018 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.semux.api.SemuxApiMock;
import org.semux.rules.KernelRule;

public class ApiClientTest {

    @Rule
    public KernelRule kernelRule = new KernelRule(51610, 51710);

    private SemuxApiMock api;

    @Before
    public void setUp() {
        api = new SemuxApiMock(kernelRule.getKernel());
        api.start();
    }

    @After
    public void tearDown() {
        api.stop();
    }

    @Test
    public void testRequest() throws IOException {
        String cmd = "get_block";

        ApiClient apiClient = kernelRule.getKernel().getApiClient();
        String response = apiClient.request(cmd, "number", 0);

        assertTrue(response.contains("\"success\":true"));
        assertTrue(response.contains("result"));
    }
}