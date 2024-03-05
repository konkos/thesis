import { Button, Flex, Input, Table, TableCaption, Tbody, Td, Text, Tfoot, Th, Thead, Tr } from '@chakra-ui/react'
import React, { useState } from 'react'
import { SERVER_BASE_URL } from '../server_constants'

export default function SearchPanel() {

    const [githubLink, setGithubLink] = useState("");

    const isValidGithubLink = githubLink.includes("github.com");

    const addNewRepository = async (link) => {
        if (link === "" || isValidGithubLink) return

        const req_link = `${SERVER_BASE_URL}/analyze/project?gitUrl=${link}`
        console.log(`req_link ${req_link}`);
        const res = await fetch(req_link, {
            method: "POST",
        })
        console.log(`res => ${res}`);
    }

    const handleButtonClick = () => {
        console.log(`sending request about ${githubLink}`);
        addNewRepository(githubLink)
    }

    const handleinputChange = (event) => {
        // add Debounce
        setGithubLink(event.target.value)
    }

    return (
        <Flex direction={'column'} gap={100}>
            <Flex gap={2}>
                <Button color="white" bg="blue.500" size='md' onClick={handleButtonClick}>Analyze Repository</Button>
                <Input placeholder='Enter the url of the GitHub repository you would like to analyze' size={"md"} onChange={handleinputChange} />
            </Flex>
            <Flex direction={'column'} gap={10}>
                <Text>Project Name</Text>

                <Table variant='striped' colorScheme='teal'>
                    <TableCaption>Imperial to metric conversion factors</TableCaption>
                    <Thead>
                        <Tr>
                            <Th>To convert</Th>
                            <Th>into</Th>
                            <Th isNumeric>multiply by</Th>
                        </Tr>
                    </Thead>
                    <Tbody>
                        <Tr>
                            <Td>inches</Td>
                            <Td>millimetres (mm)</Td>
                            <Td isNumeric>25.4</Td>
                        </Tr>
                        <Tr>
                            <Td>feet</Td>
                            <Td>centimetres (cm)</Td>
                            <Td isNumeric>30.48</Td>
                        </Tr>
                        <Tr>
                            <Td>yards</Td>
                            <Td>metres (m)</Td>
                            <Td isNumeric>0.91444</Td>
                        </Tr>
                    </Tbody>
                    <Tfoot>
                        <Tr>
                            <Th>To convert</Th>
                            <Th>into</Th>
                            <Th isNumeric>multiply by</Th>
                        </Tr>
                    </Tfoot>
                </Table>
            </Flex>
        </Flex>
    )
}
