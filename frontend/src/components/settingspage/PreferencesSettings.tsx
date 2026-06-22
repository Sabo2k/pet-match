import {
    Card,
    Stack,
    Heading,
    Separator,
    HStack,
    Spacer,
    Text,
} from "@chakra-ui/react";
import LanguageSelect from "./LanguageSelect";
import ThemeSelect from "./ThemeSelect";

export default function PreferencesSettings() {
    return (
        <>
            <Card.Root variant="elevated">
                <Card.Title fontWeight="bold" ml="3" mt="2">
                    Preferences
                </Card.Title>
                <Card.Body>
                    <Stack>
                        <Text>Personalize your PetMatch experience.</Text>
                        <Separator />
                        <Heading fontWeight="bold">Language</Heading>
                        <HStack>
                            <Text>
                                Set the language
                            </Text>
                            <Spacer/>
                            <LanguageSelect/>
                        </HStack>
                        <Separator />
                        <Heading fontWeight="bold">Theme</Heading>
                        <HStack>
                            <Text>Set theme</Text>
                            <Spacer/>
                            <ThemeSelect/>
                        </HStack>
                    </Stack>
                </Card.Body>
            </Card.Root>
        </>
    );
}
