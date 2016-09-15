package org.owasp.webgoat.session;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LabelDebuggerTest {

    @Test
    public void testSetEnabledTrue() throws Exception {
        LabelDebugger ld = new LabelDebugger();
        ld.setEnabled(true);
        assertTrue(ld.isEnabled());
    }

    @Test
    public void testSetEnabledFalse() throws Exception {
        LabelDebugger ld = new LabelDebugger();
        ld.setEnabled(false);
        assertFalse(ld.isEnabled());
    }

    @Test
    public void testSetEnabledNullThrowsException() {
        LabelDebugger ld = new LabelDebugger();
        ld.setEnabled(true);
        assertTrue(ld.isEnabled());
    }

    @Test
    public void testEnableIsTrue() {
        LabelDebugger ld = new LabelDebugger();
        ld.enable();
        assertTrue(ld.isEnabled());
    }

    @Test
    public void testDisableIsFalse() {
        LabelDebugger ld = new LabelDebugger();
        ld.disable();
        assertFalse(ld.isEnabled());
    }



}
