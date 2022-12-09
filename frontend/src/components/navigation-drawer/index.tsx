import {Drawer, List, ListItemButton, ListItemText, Toolbar} from "@mui/material";
import {observer} from "mobx-react";

const NavigationDrawer = () => {

    return <Drawer variant="permanent">
        <List>
            <ListItemButton>
                <ListItemText primary={"Test"} />
            </ListItemButton>
        </List>
    </Drawer>
}

export default observer(NavigationDrawer)