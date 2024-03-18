import { Box, ListItem, Text, UnorderedList, VStack } from '@chakra-ui/react'
import React from 'react'

export default function Cluster({ cluster, index }) {
    return (
        <VStack >
            <Text>Cluster {index}</Text>
            <UnorderedList>
                {cluster && cluster.map(node => <ListItem key={node.description}>{node.description}</ListItem>)}
            </UnorderedList>
        </VStack>
    )
}
