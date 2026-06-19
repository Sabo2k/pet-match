import { Box, Card, Heading, HStack, Separator, Spacer, Stack, Text } from "@chakra-ui/react";
import ChangePasswordButton from "./ChangePasswordButton";
import DeleteAccountButton from "./DeleteAccountButton";
import DisableAccountButton from "./DisableAccountButton";
import ChangeEmailButton from "./ChangeEmailButton";
import { useCurrentUser } from "../../hooks/useUserProfile";

export default function AccountSettings() {
    const { data: user } = useCurrentUser();

    return (
        <>
            <Card.Root variant="elevated">
                <Card.Title fontWeight="bold" ml="3" mt="2">Account</Card.Title>
                <Card.Body>
                    <Stack>
                    <Heading fontWeight="bold">Username</Heading>
                    <Text>{user?.username}</Text>
                    <Separator />
                    <HStack>
                        <Box>
                            <Heading fontWeight="bold">Email address</Heading>
                            <Text>{user?.email}</Text>
                        </Box>
                        <Spacer/>
                        <ChangeEmailButton/>
                    </HStack>
                    <Separator />
                    <Box>
                        <Heading fontWeight="bold">Password & Authentication</Heading>
                        <ChangePasswordButton/>
                    </Box>
                    <Separator/>
                    <Heading fontWeight="bold">Account Removal</Heading>
                    <Text>
                        Disabling your account means you can recover it at any time after taking this action.
                    </Text>
                    <HStack>
                        <DisableAccountButton/>
                        <DeleteAccountButton/>
                    </HStack>
                    </Stack>
                </Card.Body>
                
            </Card.Root>

        </>
    );
}