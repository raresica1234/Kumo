import {OptionsObject, SnackbarMessage} from "notistack";
import {createContext} from "react";


type EnqueueSnackbar = (message: SnackbarMessage, options?: OptionsObject | undefined) => void;

type MessageType = JSX.Element | string;

export class ToastServiceStore {
	private enqueue: EnqueueSnackbar = () => {};

	setEnqueue = (enqueue: EnqueueSnackbar) => {
		this.enqueue = enqueue;
	}

	public showError = (message: MessageType) => this.enqueue(message, { variant: "error" });

	public showWarning = (message: MessageType) => this.enqueue(message, { variant: "warning" });

	public showInfo = (message: MessageType) => this.enqueue(message, { variant: "info" });

	public showSuccess = (message: MessageType) => this.enqueue(message, { variant: "success" });
}

export const toastServiceStore = new ToastServiceStore();
export const ToastServiceContext = createContext(toastServiceStore);
