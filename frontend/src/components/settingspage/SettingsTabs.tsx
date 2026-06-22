import { Tabs } from "@chakra-ui/react";

import { FaBell, FaLock  } from "react-icons/fa";
import { IoColorPalette } from "react-icons/io5";
import { MdAccountCircle } from "react-icons/md";
import AccountSettings from "./AccountSettings";
import PreferencesSettings from "./PreferencesSettings";

export default function SettingsTabs() {

    return (
        <>
            <Tabs.Root defaultValue="account" colorPalette="purple">
                <Tabs.List>
                    <Tabs.Trigger value="account">
                        <MdAccountCircle />
                        Account
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
                <Tabs.Content value="safety-privacy">
                    Coming Soon
                </Tabs.Content>
                <Tabs.Content value="notifications">
                    Coming Soon
                </Tabs.Content>
                <Tabs.Content value="preferences">
                    <PreferencesSettings/>
                </Tabs.Content>
            </Tabs.Root>
        </>
    );
}
