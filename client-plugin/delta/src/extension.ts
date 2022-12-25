import path = require("path");
import * as vscode from 'vscode';

import {
	LanguageClientOptions,
	RevealOutputChannelOn,
} from 'vscode-languageclient';

import {
	LanguageClient,
	ServerOptions,
	State,
} from 'vscode-languageclient/node';

const outputChannel = vscode.window.createOutputChannel('Delta-Ads');
const LS_LAUNCHER_MAIN: string = 'de.facemirrored.deltaadslangserver.DeltaAdsLanguageServerLauncher';

export class DeltaAdsExtension {

	private languageClient?: LanguageClient;
	private context?: vscode.ExtensionContext;

	setContext(context: vscode.ExtensionContext) {
		this.context = context;
	}

	async init(): Promise<void> {

		try {

			// server options - LS client will use these options to start the LS
			let serverOptions: ServerOptions = getServerOptions();

			// creating the language client
			let clientId = 'delta-ads-vscode-client';
			let clientName = 'Delta-Ads LS Client';
			let clientOptions: LanguageClientOptions = {
				documentSelector: [{ scheme: 'file', language: 'delta-ads' }],
				outputChannel: outputChannel,
				revealOutputChannelOn: RevealOutputChannelOn.Never,
			};

			this.languageClient = new LanguageClient(
				clientId,
				clientName,
				serverOptions,
				clientOptions
			);

			this.languageClient.onDidChangeState((stateChangeEvent) => {

				if (stateChangeEvent.newState === State.Stopped) {

					vscode.window.showErrorMessage("Failed to initialize the Delta-Ads extension");

				} else if (stateChangeEvent.newState === State.Running) {

					vscode.window.showInformationMessage("Delta-Ads extension initialized successfully! :)");
				}
			});

			await this.languageClient.start();

		} catch (exception) {
			return Promise.reject('Extension error!');
		}
	}
}

export const extensionInstance = new DeltaAdsExtension();

// create a command to be run to start the LS java process
function getServerOptions() {

	// change the project home accordingly
	const PROJECT_HOME = path.resolve(__dirname).substring(0, 45);
	const LS_LIB = 'server-plugin\\server-plugin\\language_server_lib\\*';
	const LS_HOME = path.join(PROJECT_HOME, LS_LIB);
	const JAVA_HOME = process.env.JAVA_HOME;

	let executable: string = path.join(String(JAVA_HOME), "bin", "java");
	let args: string[] = ['-cp', LS_HOME];

	let serverOptions: ServerOptions = {
		command: executable,
		args: [...args, LS_LAUNCHER_MAIN],
		options: {},
	};

	return serverOptions;
}

// This method is called when the extension is activated
export function activate(context: vscode.ExtensionContext) {

	extensionInstance.setContext(context);

	// initialize LS Client extension instance
	extensionInstance.init()
		.then(() => {
			console.log('Extension "delta" is now finished with initialization and setup');
		})
		.catch((error) => {
			console.error("Failed to activate Delta-Ads LS extension", error);
		});
}

// This method is called when the extension is deactivated
export function deactivate() {

	vscode.window.showInformationMessage('Delta-Ads language-plugin is now deactivated!');
}
