package de.facemirrored.deltaadslangserver;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class DeltaAdsLanguageServerLauncher {

    private DeltaAdsLanguageServerLauncher() {
        // start class
    }

    public static void main() throws IOException, ExecutionException, InterruptedException {

        try (final var clientSocket = new Socket("127.0.0.1", 9925)) {

            startServer(clientSocket.getInputStream(), clientSocket.getOutputStream());

        }
    }

    private static void startServer(final InputStream inputStream, final OutputStream outputStream)
            throws ExecutionException, InterruptedException {

        final var languageServer = new DeltaAdsTextLanguageServer();
        final var launcher = Launcher.createLauncher(
                languageServer,
                LanguageClient.class,
                inputStream,
                outputStream);

        languageServer.connect(launcher.getRemoteProxy());
        final var startListening = launcher.startListening();
        startListening.get();
    }
}
