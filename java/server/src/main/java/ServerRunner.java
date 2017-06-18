/**
 * Created on 6/17/17.
 */


import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import java.util.Scanner;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class ServerRunner {
    private static ClientAndServer mockServer;
    public static void main(String... args) {
        mockServer = startClientAndServer(1080);

        mockServer.when(request("/hello"), Times.unlimited())
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("{\"greet\": \"world\"}")
                );

        quitSelfOnTerminationSignal();
    }

    private static void quitSelfOnTerminationSignal() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                quitServer();
            }
        });

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        quitServer();
    }

    private static void quitServer() {
        System.out.println("Terminating self");
        mockServer.stop();
        System.out.println("Terminated");
    }
}
