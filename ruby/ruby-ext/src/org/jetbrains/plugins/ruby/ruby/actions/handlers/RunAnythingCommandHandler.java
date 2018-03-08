package org.jetbrains.plugins.ruby.ruby.actions.handlers;

import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.openapi.extensions.ExtensionPointName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * This class customizes 'run anything' command process handler settings depending on input command
 */
public abstract class RunAnythingCommandHandler {
  public static final ExtensionPointName<RunAnythingCommandHandler> EP_NAME =
    ExtensionPointName.create("org.jetbrains.plugins.ruby.runAnythingCommandHandler");

  public abstract boolean isMatched(@NotNull String commandLine);

  /**
   * See {@link KillableProcessHandler#shouldKillProcessSoftly()} for details.
   */
  public boolean shouldKillProcessSoftly() {
    return true;
  }

  @Nullable
  public static RunAnythingCommandHandler getMatchedHandler(@NotNull String commandLine) {
    return Arrays.stream(EP_NAME.getExtensions()).filter(handler -> handler.isMatched(commandLine)).findFirst().orElse(null);
  }
}