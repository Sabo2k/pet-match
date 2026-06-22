import { Select, Portal, createListCollection } from "@chakra-ui/react";

export default function LanguageSelect() {

    const languages = createListCollection({
        items: [
            { label: "English (English)", value: "english" },
            { label: "Deutsch (German)", value: "german" },
            { label: "Français (French)", value: "french" },
            { label: "Nederlands (Dutch)", value: "dutch" },
        ],
    });

    return (
        <>
            <Select.Root collection={languages} size="sm" width="320px">
                <Select.HiddenSelect />
                <Select.Control>
                    <Select.Trigger>
                        <Select.ValueText placeholder="language" />
                    </Select.Trigger>
                    <Select.IndicatorGroup>
                        <Select.Indicator />
                    </Select.IndicatorGroup>
                </Select.Control>
                <Portal>
                    <Select.Positioner>
                        <Select.Content>
                            {languages.items.map((language) => (
                                <Select.Item
                                    item={language}
                                    key={language.value}
                                >
                                    {language.label}
                                    <Select.ItemIndicator />
                                </Select.Item>
                            ))}
                        </Select.Content>
                    </Select.Positioner>
                </Portal>
            </Select.Root>
        </>
    );
}
