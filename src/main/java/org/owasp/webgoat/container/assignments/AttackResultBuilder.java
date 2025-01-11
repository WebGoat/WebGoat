package org.owasp.webgoat.container.assignments;

import org.owasp.webgoat.container.i18n.PluginMessages;

public class AttackResultBuilder {

  private PluginMessages messages;
  private boolean lessonCompleted;
  private Object[] feedbackArgs;
  private String feedbackResourceBundleKey;
  private String output;
  private Object[] outputArgs;
  private AssignmentEndpoint assignment;
  private boolean attemptWasMade = false;
  private boolean assignmentCompleted;

  public AttackResultBuilder(PluginMessages messages) {
    this.messages = messages;
  }

  public AttackResultBuilder() {}

  public AttackResultBuilder lessonCompleted(boolean lessonCompleted) {
    this.lessonCompleted = lessonCompleted;
    this.feedbackResourceBundleKey = "lesson.completed";
    return this;
  }

  public AttackResultBuilder lessonCompleted(boolean lessonCompleted, String resourceBundleKey) {
    this.lessonCompleted = lessonCompleted;
    this.feedbackResourceBundleKey = resourceBundleKey;
    return this;
  }

  public AttackResultBuilder assignmentCompleted(boolean assignmentCompleted) {
    this.assignmentCompleted = assignmentCompleted;
    this.feedbackResourceBundleKey = "assignment.completed";
    return this;
  }

  public AttackResultBuilder assignmentCompleted(
      boolean assignmentCompleted, String resourceBundleKey) {
    this.assignmentCompleted = assignmentCompleted;
    this.feedbackResourceBundleKey = resourceBundleKey;
    return this;
  }

  public AttackResultBuilder feedbackArgs(Object... args) {
    this.feedbackArgs = args;
    return this;
  }

  public AttackResultBuilder feedback(String resourceBundleKey) {
    this.feedbackResourceBundleKey = resourceBundleKey;
    return this;
  }

  public AttackResultBuilder output(String output) {
    this.output = output;
    return this;
  }

  public AttackResultBuilder outputArgs(Object... args) {
    this.outputArgs = args;
    return this;
  }

  public AttackResultBuilder attemptWasMade() {
    this.attemptWasMade = true;
    return this;
  }

  public AttackResult build() {
    return new AttackResult(
        lessonCompleted,
        feedbackResourceBundleKey,
        feedbackArgs,
        output,
        outputArgs,
        assignment.getClass().getSimpleName(),
        attemptWasMade);
  }

  public AttackResultBuilder assignment(AssignmentEndpoint assignment) {
    this.assignment = assignment;
    return this;
  }

  /**
   * Convenience method for create a successful result:
   *
   * <p>- Assignment is set to solved - Feedback message is set to 'assignment.solved'
   *
   * <p>Of course you can overwrite these values in a specific lesson
   *
   * @return a builder for creating a result from a lesson
   * @param assignment
   */
  public static AttackResultBuilder success(AssignmentEndpoint assignment) {
    return new AttackResultBuilder()
        .lessonCompleted(true)
        .assignmentCompleted(true)
        .attemptWasMade()
        .feedback("assignment.solved")
        .assignment(assignment);
  }

  /**
   * Convenience method for create a failed result:
   *
   * <p>- Assignment is set to not solved - Feedback message is set to 'assignment.not.solved'
   *
   * <p>Of course you can overwrite these values in a specific lesson
   *
   * @return a builder for creating a result from a lesson
   * @param assignment
   */
  public static AttackResultBuilder failed(AssignmentEndpoint assignment) {
    return new AttackResultBuilder()
        .lessonCompleted(false)
        .assignmentCompleted(true)
        .attemptWasMade()
        .feedback("assignment.not.solved")
        .assignment(assignment);
  }

  public static AttackResultBuilder informationMessage(AssignmentEndpoint assignment) {
    return new AttackResultBuilder().lessonCompleted(false).assignment(assignment);
  }
}
