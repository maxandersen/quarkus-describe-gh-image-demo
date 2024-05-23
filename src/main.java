///usr/bin/env jbang "$0" "$@" ; exit $?


//DEPS io.quarkus.platform:quarkus-bom:3.10.2@pom
//DEPS io.quarkiverse.langchain4j:quarkus-langchain4j-core:RELEASE
//DEPS io.quarkiverse.langchain4j:quarkus-langchain4j-openai:RELEASE

//JAVAC_OPTIONS -parameters
//JAVA_OPTIONS -Djava.util.logging.manager=org.jboss.logmanager.LogManager 
//JAVA_OPTIONS -Dquarkus.langchain4j.openai.timeout=60s
//JAVA_OPTIONS -Dquarkus.langchain4j.openai.chat-model.model-name=gpt-4o
//JAVA_OPTIONS -Dquarkus.banner.enabled=false
//JAVA_OPTIONS -Dquarkus.log.console.enable=false


import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import io.quarkus.logging.Log;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;

@QuarkusMain
public class main implements QuarkusApplication {

    private final ChatLanguageModel chatLanguageModel;

    public main(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @Override
    @ActivateRequestContext
    public int run(String... args) throws Exception {

        String url = args.length>0 ? args[0] : "https://github.com/quarkusio/quarkus/assets/405347/8d57711b-4be5-437c-8fd4-90e542e65c3a";
        UserMessage userMessage = UserMessage.from(
                TextContent.from(
                        "This is image was reported on a GitHub issue on the Quarkus repository. If this is a snippet of Java code, please respond with only the Java code. If it is not, describe what the image is showing"),
                ImageContent.from(url));
        System.out.println("Analyzing " + url + "...");
        Response<AiMessage> response = chatLanguageModel.generate(userMessage);
        System.out.println(response.content().text());
        return 0;
    }

}
