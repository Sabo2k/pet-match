import { IconButton, Carousel, Image } from "@chakra-ui/react";
import { LuChevronLeft, LuChevronRight } from "react-icons/lu";
import type { ImageDto } from "../../hooks/useAdvertisements";

interface ImageCarouselProps {
    images: ImageDto[];
}

export default function ImageCarousel({ images }: ImageCarouselProps) {
    if (!images || images.length === 0) {
        return <p>No images available</p>;
    }

    return (
        <>
        <Carousel.Root slideCount={images.length} gap="4" >
            <Carousel.Control justifyContent="center" gap="4" width="full">
                <Carousel.PrevTrigger asChild>
                    <IconButton size="xs" variant="outline">
                        <LuChevronLeft />
                    </IconButton>
                </Carousel.PrevTrigger>

                <Carousel.ItemGroup width="full">
                    {images.map((image, index) => (
                        <Carousel.Item key={image.id} index={index}>
                            <Image
                                aspectRatio="16/9"
                                src={image.imageUrl}
                                alt="Advertisement image"
                                w="100%"
                                h="100%"
                                objectFit="cover"
                            />
                        </Carousel.Item>
                    ))}
                </Carousel.ItemGroup>

                <Carousel.NextTrigger asChild>
                    <IconButton size="xs" variant="outline">
                        <LuChevronRight />
                    </IconButton>
                </Carousel.NextTrigger>
            </Carousel.Control>

            <Carousel.IndicatorGroup>
                {images.map((image) => (
                    <Carousel.Indicator
                        key={image.id}
                        index={images.indexOf(image)}
                        unstyled
                        _current={{
                            outline: "2px solid currentColor",
                            outlineOffset: "2px",
                        }}
                    >
                        <Image
                            w="20"
                            aspectRatio="16/9"
                            src={image.imageUrl}
                            alt="Advertisement thumbnail"
                            objectFit="cover"
                        />
                    </Carousel.Indicator>
                ))}
            </Carousel.IndicatorGroup>
        </Carousel.Root>
        </>
    );
}
