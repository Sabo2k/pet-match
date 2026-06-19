import { Tabs } from "@chakra-ui/react";

import { FaBell, FaLock  } from "react-icons/fa";
import { IoColorPalette } from "react-icons/io5";
import { MdAccountCircle } from "react-icons/md";
import { IoSettingsSharp } from "react-icons/io5";
import AccountSettings from "./AccountSettings";
export default function SettingsTabs() {

    return (
        <>
            <Tabs.Root defaultValue="account" colorPalette="purple">
                <Tabs.List>
                    <Tabs.Trigger value="account">
                        <IoSettingsSharp/>
                        Account
                    </Tabs.Trigger>
                    <Tabs.Trigger value="profile">
                        <MdAccountCircle />
                        Profile
                    </Tabs.Trigger>
                    <Tabs.Trigger value="safety-privacy">
                        <FaLock />
                        Safety & Privacy
                    </Tabs.Trigger>
                    <Tabs.Trigger value="notifications">
                        <FaBell/>
                        Notifications
                    </Tabs.Trigger>
                    <Tabs.Trigger value="preferences">
                        <IoColorPalette />
                        Preferences
                    </Tabs.Trigger>
                </Tabs.List>
                <Tabs.Content value="account">
                    <AccountSettings/>
                </Tabs.Content>
                <Tabs.Content value="profile">
                    Lorem ipsum
                </Tabs.Content>
                <Tabs.Content value="safety-privacy">
                    Lorem ipsum
                </Tabs.Content>
                <Tabs.Content value="notifications">
                    Lorem ipsum
                </Tabs.Content>
                <Tabs.Content value="preferences">
                    Lorem ipsum
                </Tabs.Content>
            </Tabs.Root>
        </>
    );
}
