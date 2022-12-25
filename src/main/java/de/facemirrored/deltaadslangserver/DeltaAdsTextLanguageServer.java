package de.facemirrored.deltaadslangserver;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static de.facemirrored.deltaadslangserver.Util.not;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;

public class DeltaAdsTextLanguageServer implements LanguageServer, LanguageClientAware {

    private final TextDocumentService textDocumentService;
    private final WorkspaceService workspaceService;
    private ClientCapabilities clientCapabilities;
    LanguageClient languageClient;
    private int shutdown = 1;

    public DeltaAdsTextLanguageServer() {
        textDocumentService = new DeltaAdsTextDocumentService();
        workspaceService = new DeltaAdsWorkspaceService(this);
    }

    @Override
    public void connect(LanguageClient client) {

        this.languageClient = client;
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {

        final var response = new InitializeResult(new ServerCapabilities());
        response.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
        this.clientCapabilities = params.getCapabilities();

        // register new completion provider if not done by the client
        if (not(isDynamicCompletionRegistration())) {

            response.getCapabilities().setCompletionProvider(new CompletionOptions());
        }

        return CompletableFuture.supplyAsync(() -> response);
    }

    @Override
    public void initialized(InitializedParams params) {

        // Check if dynamic completion support is allowed, if so register
        if (isDynamicCompletionRegistration()) {

            final var completionRegistrationOptions = new CompletionRegistrationOptions();
            final var completionRegistration = new Registration(
                    UUID.randomUUID().toString(),
                    "textDocument/completion",
                    completionRegistrationOptions);

            languageClient.registerCapability(new RegistrationParams(singletonList(completionRegistration)));
        }
    }

    /**
     * Check if dynamic registration of completion capability is allowed by the client.
     *
     * @return If dynamic registration is allowed
     */
    private boolean isDynamicCompletionRegistration() {

        final var textDocumentClientCapabilities = clientCapabilities.getTextDocument();
        return nonNull(textDocumentClientCapabilities)
                && nonNull(textDocumentClientCapabilities.getCompletion())
                && Boolean.FALSE.equals(textDocumentClientCapabilities.getCompletion().getDynamicRegistration());
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        shutdown = 0;
        return CompletableFuture.supplyAsync(Object::new);
    }

    @Override
    public void exit() {
        System.exit(shutdown);
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return workspaceService;
    }
}
