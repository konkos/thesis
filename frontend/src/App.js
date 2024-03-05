import { useEffect } from "react";
import { useState } from "react";
import { Tab, TabList, TabPanel, TabPanels, Tabs } from '@chakra-ui/react'

import SearchPanel from "./panel/SearchPanel.jsx";
import CompareProjectsPanel from "./panel/CompareProjectsPanel.jsx";
import ClusterPanel from "./panel/ClusterPanel.jsx";

function App() {


  return (
    <div className="App">

      <Tabs >

        <TabList bg="blue.500" padding={"10px"}>
          <Tab color={"white"}>Add New Repository</Tab>
          <Tab color={"white"}>Compare Project Metrics</Tab>
          <Tab color={"white"}>Create Clusters</Tab>
        </TabList>

        <TabPanels p='2rem'>
          <TabPanel>
            <SearchPanel />
          </TabPanel>
          <TabPanel>
            <CompareProjectsPanel />
          </TabPanel>
          <TabPanel>
            <ClusterPanel />
          </TabPanel>
        </TabPanels>

      </Tabs>
    </div >
  );
}

export default App;