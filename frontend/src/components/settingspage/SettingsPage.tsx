import { Box, Heading } from "@chakra-ui/react";
import Navbar from "../navbar/Navbar";
import SettingsTabs from "./SettingsTabs";


export default function SettingsPage() {
    return (
        <>
            <Navbar/>
            <Heading fontWeight="bold">Settings</Heading>
            <Box p={10}>
            <SettingsTabs/>
            </Box>
        </>
    );
}
