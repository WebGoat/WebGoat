package org.owasp.webgoat.container.session;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LabelDebuggerTest {

  @Test
  void testSetEnabledTrue() {
    LabelDebugger ld = new LabelDebugger();
    ld.setEnabled(true);
    Assertions.assertThat(ld.isEnabled()).isTrue();
  }

  @Test
  void testSetEnabledFalse() {
    LabelDebugger ld = new LabelDebugger();
    ld.setEnabled(false);
    Assertions.assertThat((ld.isEnabled())).isFalse();
  }

  @Test
  void testSetEnabledNullThrowsException() {
    LabelDebugger ld = new LabelDebugger();
    ld.setEnabled(true);
    Assertions.assertThat((ld.isEnabled())).isTrue();
  }

  @Test
  void testEnableIsTrue() {
    LabelDebugger ld = new LabelDebugger();
    ld.enable();
    Assertions.assertThat((ld.isEnabled())).isTrue();
  }

  @Test
  void testDisableIsFalse() {
    LabelDebugger ld = new LabelDebugger();
    ld.disable();
    Assertions.assertThat((ld.isEnabled())).isFalse();
  }
}
