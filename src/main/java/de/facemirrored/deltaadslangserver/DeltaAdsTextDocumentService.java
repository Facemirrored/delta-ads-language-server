package de.facemirrored.deltaadslangserver;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

// https://medium.com/ballerina-techblog/practical-guide-for-the-language-server-protocol-3091a122b750
public class DeltaAdsTextDocumentService implements TextDocumentService {

    private static final Logger LOGGER = Logger.getLogger(DeltaAdsTextDocumentService.class.getName());

    @Override
    public void didOpen(DidOpenTextDocumentParams didOpenTextDocumentParams) {

        LOGGER.info("Open text document:\t" + didOpenTextDocumentParams.getTextDocument().getUri());
    }

    @Override
    public void didChange(DidChangeTextDocumentParams didChangeTextDocumentParams) {

        LOGGER.info("Change text document:\t" + didChangeTextDocumentParams.getTextDocument().getUri());
    }

    @Override
    public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {

        LOGGER.info("Close text document:\t" + didCloseTextDocumentParams.getTextDocument().getUri());
    }

    @Override
    public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {

        LOGGER.info("Save text document:\t" + didSaveTextDocumentParams.getTextDocument().getUri());
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {

        return CompletableFuture.supplyAsync(() -> {

            LOGGER.info("Completion:\t" + position.getTextDocument().getUri());
            final var completionItem = new CompletionItem();
            completionItem.setLabel("Test completion item");
            completionItem.setInsertText("Test insert text");
            completionItem.setDetail("Snippet");
            completionItem.setKind(CompletionItemKind.Snippet);

            return Either.forLeft(Collections.singletonList(completionItem));
        });
    }
}
