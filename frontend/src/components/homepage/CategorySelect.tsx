import { Select, Spinner, Box, Text } from "@chakra-ui/react";
import { useCategories } from "@/hooks/useCategories";
import { useMemo } from "react";
import { createListCollection } from "@chakra-ui/react";

interface CategorySelectProps {
    value?: string;
    onChange?: (categoryId: string, categoryName: string) => void;
}

export default function CategorySelect({ value, onChange }: CategorySelectProps) {
    const { data: categories = [], isLoading, error } = useCategories();

    const items = useMemo(() => {
        const filtered = categories
            .filter((category) => category && category.name)
            .map((category) => ({
                label: category.name,
                value: category.id || category.name, // Use name as fallback if id is null
            }));
        return filtered;
    }, [categories]);

    const collection = useMemo(
        () =>
            createListCollection({
                items,
            }),
        [items]
    );

    if (error) {
        return (
            <Box color="red.500" mb={4}>
                <Text>Failed to load categories</Text>
            </Box>
        );
    }

    return (
        <Select.Root
            collection={collection}
            value={value ? [value] : []}
            onValueChange={(details) => {
                const selectedValue = details.value[0];
                if (selectedValue && onChange) {
                    const selectedCategory = categories.find(
                        (cat) => String(cat.id) === selectedValue
                    );
                    onChange(selectedValue, selectedCategory?.name || "");
                }
            }}
            mb={4}
        >
            <Select.HiddenSelect />
            <Select.Control>
                <Select.Trigger>
                    {isLoading ? (
                        <Spinner size="sm" mr={2} />
                    ) : (
                        <Select.ValueText placeholder="Choose a category" />
                    )}
                </Select.Trigger>
                <Select.IndicatorGroup>
                    <Select.Indicator />
                </Select.IndicatorGroup>
            </Select.Control>
            <Select.Content>
                {collection.items.map((item) => (
                    <Select.Item key={item.value} item={item}>
                        {item.label}
                    </Select.Item>
                ))}
            </Select.Content>
        </Select.Root>
    );
}