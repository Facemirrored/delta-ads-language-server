package de.facemirrored.deltaadslangserver;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;

import java.util.concurrent.ExecutionException;

public class DeltaAdsLanguageServerLauncher {

    private DeltaAdsLanguageServerLauncher() {
        // start class
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        startServer();
    }

    private static void startServer()
            throws ExecutionException, InterruptedException {

        final var languageServer = new DeltaAdsTextLanguageServer();
        final var launcher = Launcher.createLauncher(
                languageServer,
                LanguageClient.class,
                System.in,
                System.out);

        languageServer.connect(launcher.getRemoteProxy());
        final var startListening = launcher.startListening();
        startListening.get();
    }
}
