import Explorer from "../explorer";
import {observer} from "mobx-react";
import {useLocation} from "react-router-dom";
import MainPageContainer from "../../../components/main-page/main-page-container";

const Home = () => {
	const search = useLocation().search;
	const path = new URLSearchParams(search).get('path');

	return <MainPageContainer>
		<Explorer path={path ?? undefined}/>
	</MainPageContainer>

}

export default observer(Home);
