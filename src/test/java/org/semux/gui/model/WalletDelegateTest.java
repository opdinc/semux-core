/**
 * Copyright (c) 2017 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.gui.model;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.semux.Kernel;
import org.semux.core.Blockchain;
import org.semux.core.state.Delegate;
import org.semux.util.Bytes;

public class WalletDelegateTest {

    private byte[] address = Bytes.random(20);
    private byte[] name = Bytes.of("test");
    private long registeredAt = 2;
    private long votes = 3;

    @Test
    public void testBasic() {

        Delegate d = new Delegate(address, name, registeredAt, votes);
        WalletDelegate wd = new WalletDelegate(d);

        assertThat(wd.getAddress(), equalTo(address));
        assertThat(wd.getName(), equalTo(name));
        assertThat(wd.getRegisteredAt(), equalTo(registeredAt));
        assertThat(wd.getVotes(), equalTo(votes));

        assertEquals(0, wd.getVotesFromMe());
        assertEquals(0, wd.getNumberOfBlocksForged());
        assertEquals(0, wd.getNumberOfTurnsHit());
        assertEquals(0, wd.getNumberOfTurnsMissed());

        wd.setVotesFromMe(1);
        wd.setNumberOfBlocksForged(2);
        wd.setNumberOfTurnsHit(3);
        wd.setNumberOfTurnsMissed(4);

        assertEquals(1L, wd.getVotesFromMe());
        assertEquals(2L, wd.getNumberOfBlocksForged());
        assertEquals(3L, wd.getNumberOfTurnsHit());
        assertEquals(4L, wd.getNumberOfTurnsMissed());

        assertEquals(100.0 * 3 / (3 + 4), wd.getRate(), 10 ^ -8);
    }

    @Test
    public void testIsValidator() {
        Kernel kernel = mock(Kernel.class);
        Blockchain blockchain = mock(Blockchain.class);

        String v1 = "validator1";
        String v2 = "validator2";
        when(kernel.getBlockchain()).thenReturn(blockchain);
        when(blockchain.getValidators()).thenReturn(Arrays.asList(v1, v2));

        Delegate d = new Delegate(address, name, registeredAt, votes);
        WalletDelegate wd = new WalletDelegate(d);
        assertFalse(wd.isValidator(kernel));

        d = new Delegate(address, Bytes.of(v1), registeredAt, votes);
        wd = new WalletDelegate(d);
        assertFalse(wd.isValidator(kernel));
    }
}
