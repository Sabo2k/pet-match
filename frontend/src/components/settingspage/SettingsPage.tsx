import { Box, Flex, Heading } from "@chakra-ui/react";
import Navbar from "../navbar/Navbar";
import SettingsTabs from "./SettingsTabs";

export default function SettingsPage() {
    return (
        <>
            <Navbar />
            <Box p={10}>
                <Flex>
                    <Heading fontWeight="bold">Settings</Heading>
                </Flex>
                <SettingsTabs />
            </Box>
        </>
    );
}
