package xyz.webmc.wlib.api.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.CommandSender;

public final class CaptureSender extends ExtendableCommandSender {
  private final List<String> messages = new ArrayList<>();

  public CaptureSender(final CommandSender parent) {
    super(parent);
  }

  public CaptureSender() {
    super(null);
  }

  public final List<String> getMessages() {
    return this.messages;
  }

  @Override
  public final void sendMessage(final String message) {
    this.messages.add(message);
  }

  @Override
  public final void sendMessage(final String[] messages) {
    this.messages.addAll(Arrays.asList(messages));
  }
}
