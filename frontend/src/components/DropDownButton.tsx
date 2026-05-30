import { Menu, IconButton } from "@chakra-ui/react";
import { BsThreeDotsVertical } from "react-icons/bs";
import { FiShare2, FiEyeOff, FiFlag } from "react-icons/fi";


export default function DropDownButton() {
    return (
        <Menu.Root>
            <Menu.Trigger asChild>
                <IconButton
                    aria-label="Options"
                    variant="ghost"
                    size="sm"
                >
                    <BsThreeDotsVertical />
                </IconButton>
            </Menu.Trigger>
            <Menu.Positioner>
                <Menu.Content>
                    <Menu.Item value="share">
                        <FiShare2/> Share
                    </Menu.Item>
                    <Menu.Item value="notinterested">
                        <FiEyeOff/> Not Interested
                    </Menu.Item>
                    <Menu.Item value="report">
                        <FiFlag/> Report
                    </Menu.Item>
                </Menu.Content>
            </Menu.Positioner>
        </Menu.Root>
    );
}