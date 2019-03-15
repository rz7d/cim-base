package milktea.cim.framework.interactive;

import java.util.concurrent.CompletionStage;

public interface Interactive {

  CompletionStage<Message> sendMessage(String message);

  CompletionStage<Message> sendMessage(Message message);

}
