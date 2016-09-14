package org.owasp.webgoat.session;

import org.junit.Assert;
import org.junit.Test;

public class LabelDebuggerTest {

    @Test
    public void testSetEnabledTrue() throws Exception {
        LabelDebugger ld = new LabelDebugger();
        ld.setEnabled(true);
        Assert.assertTrue(ld.isEnabled());
    }

    @Test
    public void testSetEnabledFalse() throws Exception {
        LabelDebugger ld = new LabelDebugger();
        ld.setEnabled(false);
        Assert.assertFalse(ld.isEnabled());
    }

    @Test(expected = Exception.class)
    public void testSetEnabledNullThrowsException() throws Exception {
        LabelDebugger ld = new LabelDebugger();
        ld.setEnabled(null);
    }

    @Test
    public void testEnableIsTrue() {
        LabelDebugger ld = new LabelDebugger();
        ld.enable();
        Assert.assertTrue(ld.isEnabled());
    }

    @Test
    public void testDisableIsFalse() {
        LabelDebugger ld = new LabelDebugger();
        ld.disable();
        Assert.assertFalse(ld.isEnabled());
    }



}
