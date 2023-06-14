import * as React from "react"
import {useEffect, useState} from "react"
import {
    Box,
    Button,
    ChakraProvider,
    Container,
    FormControl,
    FormHelperText,
    FormLabel,
    Heading,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalFooter,
    ModalHeader,
    ModalOverlay,
    Table,
    TableContainer,
    Tbody,
    Td,
    Text,
    Th,
    Thead,
    theme,
    Tr,
    useDisclosure,
} from "@chakra-ui/react"
import API, {Booking} from "./API";
import {RangeDatepicker} from "chakra-dayzed-datepicker";

const api: API = new API()

function formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
}

function parseDate(date: string): Date {
    return new Date(new Date(date).toLocaleString("en-US", {timeZone: "UTC"}))
}

export const App = () => {
    const [bookings, setBookings] = useState<Booking[]>([])

    // modal state
    const {isOpen, onOpen, onClose} = useDisclosure()
    const [activeBooking, setActiveBooking] = useState<Booking>({
        start: formatDate(new Date()),
        end: formatDate(new Date())
    })
    const [pickerDates, setPickerDates] = useState<Date[]>([new Date(), new Date()])
    const [createOrUpdate, setCreateOrUpdate] = useState<'create' | 'update'>('create')

    useEffect(() => {
        //fixme ignoring any bad status codes
        api.all().then(setBookings)
    }, [])

    const cancel = (id: number): Promise<void> => {
        //fixme ignoring any bad status codes
        return api.delete(id).then(() => {
            setBookings((bookings) => [...bookings.filter(booking => booking.id !== id)])
        })
    }

    const create = () => {
        //fixme ignoring any bad status codes
        api.create({
            start: activeBooking.start,
            end: activeBooking.end
        }).then(booking => {
            setBookings((bookings) => [...bookings, booking])
        })
    }

    const update = () => {
        cancel(activeBooking.id!!).then(() => create());
    }

    return (
        <ChakraProvider theme={theme}>
            <Container my={5} maxW='3xl'>
                <Heading as={"h1"}>
                    Bookings <Button onClick={() => {
                    setCreateOrUpdate('create')
                    setActiveBooking({
                        start: formatDate(new Date()),
                        end: formatDate(new Date())
                    })
                    onOpen()
                }}>Add</Button>
                </Heading>
                <Text my={5}>View your bookings below or add them by clicking the 'Add' button above.</Text>
                <TableContainer>
                    <Table variant='striped' colorScheme='blue'>
                        <Thead>
                            <Tr>
                                <Th isNumeric>ID</Th>
                                <Th>Start</Th>
                                <Th>End</Th>
                            </Tr>
                        </Thead>
                        <Tbody>
                            {bookings.map(booking => (
                                <Tr key={booking.id}>
                                    <Td isNumeric>{booking.id}</Td>
                                    <Td>{booking.start}</Td>
                                    <Td>{booking.end}</Td>
                                    <Td>
                                        <Button onClick={() => cancel(booking.id!!)}>Cancel</Button>
                                    </Td>
                                    <Td>
                                        <Button onClick={() => {
                                            setCreateOrUpdate('update')
                                            setPickerDates([parseDate(booking.start), parseDate(booking.end)])
                                            setActiveBooking({
                                                id: booking.id,
                                                start: booking.start,
                                                end: booking.end
                                            })
                                            onOpen()
                                        }}>Update</Button>
                                    </Td>
                                </Tr>
                            ))}
                        </Tbody>
                    </Table>
                    <Modal isOpen={isOpen} onClose={onClose}>
                        <ModalOverlay/>
                        <ModalContent>
                            <ModalHeader>New Booking</ModalHeader>
                            <ModalCloseButton/>
                            <ModalBody>
                                <Box>
                                    <FormControl isRequired>
                                        <FormLabel>Booking Dates</FormLabel>
                                        <RangeDatepicker
                                            selectedDates={pickerDates}
                                            onDateChange={(dates) => {
                                                setPickerDates(dates)
                                                setActiveBooking(prev => {
                                                    return {
                                                        id: prev.id,
                                                        start: formatDate(dates.length > 0 ? dates[0] : new Date()),
                                                        end: formatDate(dates.length > 1 ? dates[1] : new Date())
                                                    }
                                                })
                                            }}
                                        />
                                        <FormHelperText>Select a booking date range</FormHelperText>
                                    </FormControl>
                                </Box>
                            </ModalBody>
                            <ModalFooter>
                                <Button colorScheme='blue' mr={3} onClick={() => {
                                    switch (createOrUpdate) {
                                        case "create":
                                            create()
                                            break;
                                        case "update":
                                            update()
                                            break;
                                    }
                                    onClose()
                                }}>
                                    Save
                                </Button>
                                <Button variant='ghost' onClick={onClose}>Cancel</Button>
                            </ModalFooter>
                        </ModalContent>
                    </Modal>
                </TableContainer>
            </Container>
        </ChakraProvider>
    )
}
