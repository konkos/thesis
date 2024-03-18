import { Button, Flex, Heading, Input, Table, TableCaption, Tbody, Td, Th, Thead, Tr } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { SERVER_BASE_URL } from '../server_constants';


export default function CompareProjectsPanel() {

    const [githubLink, setGithubLink] = useState("");
    const [compData, setCompData] = useState({ distance: [] })
    const [branchName, setBranchName] = useState("");

    useEffect(() => {
        console.log(`DATA: `, compData);
        if (compData.projectName) {
            fetch(`${SERVER_BASE_URL}/categorize?projectName=${compData.projectName}`)
        }
    }, [compData])


    const handleButtonClick = async () => {

        if (!githubLink || !branchName) return

        console.log(`sending request about ${githubLink}, branch ${branchName}`);
        const data = await (await fetch(`${SERVER_BASE_URL}/analyze/project?gitUrl=${githubLink}&branch=${branchName}`)).json()
        console.log(`data.status ${data.status}`);
        setCompData(data)
    }

    const handleInputChange = (event) => {
        // add Debounce
        setGithubLink(event.target.value)
    }

    const handleChangeBrachName = (event) => {
        setBranchName(event.target.value)
    }

    return (
        <div>
            <Flex gap={2} >
                <Button color="white" bg="blue.500" size='md' onClick={handleButtonClick}>Compare Repository</Button>
                <Input placeholder='Enter the url of the GitHub repository you would like to compare against' maxW={'600px'} onChange={handleInputChange} />
                <Input placeholder='Enter the production Branch of your repository' maxW={'400px'} onChange={handleChangeBrachName} />
            </Flex>
            <Flex direction={'column'} alignItems={'center'} gap={5} marginTop={10}>

                <Heading>{compData.projectName}</Heading>
                {
                    compData.distance && compData.distance.map((item) => {
                        return (
                            <Table variant='striped' colorScheme='teal' key={item.projectName} size={'sm'}>
                                <TableCaption placement='top' >{item.projectName}</TableCaption>
                                <Thead>
                                    <Tr>
                                        <Th isNumeric>Coverage Difference</Th>
                                        <Th isNumeric>Statements Difference</Th>
                                        <Th isNumeric>Miss Difference</Th>
                                        <Th isNumeric>Dependencies Difference</Th>
                                        <Th isNumeric>Comments Difference</Th>
                                    </Tr>
                                </Thead>
                                <Tbody>
                                    <Tr>
                                        <Td isNumeric>{item.coverageDifference}</Td>
                                        <Td isNumeric>{item.stmtsDifference}</Td>
                                        <Td isNumeric>{item.missDifference}</Td>
                                        <Td isNumeric>{item.dependenciesDifference}</Td>
                                        <Td isNumeric>{item.commentsDifference}</Td>
                                    </Tr>

                                </Tbody>

                            </Table>
                        )
                    })

                }
            </Flex>
        </div>
    )
}
