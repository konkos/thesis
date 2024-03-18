import { Button, Divider, Flex, Input, NumberDecrementStepper, NumberIncrementStepper, NumberInput, NumberInputField, NumberInputStepper, Text } from '@chakra-ui/react'
import React, { useEffect, useState } from 'react'
import { SERVER_BASE_URL } from '../server_constants'
import Cluster from '../components/Cluster'


export default function ClusterPanel() {

    const [category, setCategory] = useState("")
    const [clusterSize, setClusterSize] = useState(1)
    const [clusterData, setClusterData] = useState([])

    useEffect(() => {
        if (clusterData.length === 0) return
        // console.log(`Cluster Data`, clusterData);
        // console.log(`Cluster Data [0]`, clusterData[0]);
        console.log(`Cluster Size ${clusterSize}`);
    }, [clusterData, clusterSize])


    const handleInputChange = (event) => {
        setCategory(event.target.value)
    }

    const handleClusterSizeChange = (valueAsString, valueAsNumber) => {
        let currentValue = valueAsNumber
        if (currentValue < 1) setClusterSize(1)
        else setClusterSize(currentValue)
    }

    const handleButtonClick = async (event) => {
        let endpoint = `${SERVER_BASE_URL}/cluster?k${clusterSize}`
        if (category !== "") endpoint = `${endpoint}&field=${category}`
        const res = await fetch(endpoint, { method: 'POST' })
        const data = await res.json()
        console.log(data);
        setClusterData(data)
    }

    return (<>
        <Flex gap={3}>
            <Input placeholder='Enter desired category (optional)' maxW={'600px'} onChange={handleInputChange} />
            <Text alignSelf={'center'} fontSize={'lg'}>Cluster Size:</Text>
            <NumberInput size='md' maxW={20} defaultValue={1} min={1} onChange={handleClusterSizeChange} allowMouseWheel aria-label='Cluster Size'>
                <NumberInputField />
                <NumberInputStepper>
                    <NumberIncrementStepper />
                    <NumberDecrementStepper />
                </NumberInputStepper>
            </NumberInput>
            <Button color="white" bg="blue.500" size='md' onClick={handleButtonClick}>Get Clusters</Button>
        </Flex>
        <Divider height={'2rem'} />

        <Flex gap={'2rem'}>
            {

                clusterData && clusterData.map(
                    (cluster, index) => (<>
                        <Cluster cluster={cluster} index={index} />
                    </>
                    ))
            }
        </Flex>
    </>)
}