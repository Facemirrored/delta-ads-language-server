package de.facemirrored.deltaadslangserver;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;

import java.util.logging.Logger;

public class DeltaAdsWorkspaceService implements WorkspaceService {

    private DeltaAdsTextLanguageServer deltaAdsTextLanguageServer;
    private static final Logger LOGGER = Logger.getLogger(DeltaAdsWorkspaceService.class.getName());

    public DeltaAdsWorkspaceService(final DeltaAdsTextLanguageServer languageServer) {

        this.deltaAdsTextLanguageServer = languageServer;
    }

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams params) {

        LOGGER.info("Operation 'workspace/didChangeConfiguration' Ack");
    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {

        LOGGER.info("Operation 'workspace/didChangeWatchedFilesParams' Ack");
    }

    @Override
    public void didRenameFiles(RenameFilesParams params) {

        LOGGER.info("Operation 'workspace/didRenameFiles' Ack");
    }
}
