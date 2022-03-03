import {PropsWithChildren} from "react";
import styles from './center-container.module.scss'
import classNames from "classnames";

interface Props {
	className?: string
}

const CenterContainer = ({className, children}: PropsWithChildren<Props>) => {
	return (
		<div
			className={classNames(styles.centerVertically, className)}
		>
			{children}
		</div>
	)
};


export default CenterContainer;
