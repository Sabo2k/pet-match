import { createListCollection, Portal, Select } from "@chakra-ui/react";
import { useTheme } from "next-themes";

export default function ThemeSelect() {
    const { theme, setTheme } = useTheme();

    const themes = createListCollection({
        items: [
            { label: "Light", value: "light" },
            { label: "Dark", value: "dark" },
            { label: "System", value: "system" },
        ],
    });

    return (
        <>
            <Select.Root
                collection={themes}
                size="sm"
                width="320px"
                value={theme ? [theme] : []}
                onValueChange={(e) => setTheme(e.value[0])}
            >
                <Select.HiddenSelect />
                <Select.Control>
                    <Select.Trigger>
                        <Select.ValueText placeholder="Theme" />
                    </Select.Trigger>
                    <Select.IndicatorGroup>
                        <Select.Indicator />
                    </Select.IndicatorGroup>
                </Select.Control>
                <Portal>
                    <Select.Positioner>
                        <Select.Content>
                            {themes.items.map((theme) => (
                                <Select.Item
                                    item={theme}
                                    key={theme.value}
                                >
                                    {theme.label}
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