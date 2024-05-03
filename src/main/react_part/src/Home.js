import React, { useState, useEffect } from 'react';
import './Home.css';
import Search from "./Search.js";
import { Button } from 'react-bootstrap';
import { DropdownButton, Dropdown } from 'react-bootstrap';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import $ from 'jquery';
import Popper from 'popper.js';

function Home() {

    const [currentSchedule, setCurrentSchedule] = useState(null);
    const [scheduleName, setScheduleName] = useState(null);

    useEffect(() => {
        fetchCurrentSchedule();
    }, []);

    const fetchCurrentSchedule = async () => {
        const schedule = await getCurrentSchedule();
        setCurrentSchedule(schedule);
        setScheduleName(schedule.scheduleName);
    };

    const [scheduleNames, setScheduleNames] = useState(null); // Initialize as null
    const [dropdownOptions, setDropdownOptions] = useState([]);

    const fetchScheduleNames = async () => {
        const scheduleData = await getScheduleNames();
        const options = scheduleData.map((name, index) => (
            <Dropdown.Item key={name} onClick={() => handleDropdownItemClick(name)}>
                {name}
            </Dropdown.Item>
        ));
        setDropdownOptions(options);
    };

    useEffect(() => {
        fetchScheduleNames();
    }, []);

    const handleDropdownItemClick = async (selectedOption) => {
        console.log("Selected option:", selectedOption);
        const url = `http:\/\/localhost:7979/changeCurrentSchedule/${selectedOption}`;
        await fetch(url); // Wait for the request to complete
        fetchCurrentSchedule(); // Refetch the current schedule
    };

    const [searchClicked, setSearchClicked] = useState(false);
    const onSearch = () => {
        setSearchClicked(true);
    };

    return (
        <div>
            {searchClicked ? (
                <Search />
            ) : (
                <div className="container">
                    <div className="Navigation">
                        <Button className="SearchBtn" onClick={onSearch}>Search</Button>

                        <Dropdown className="dropdown">
                            <Dropdown.Toggle variant="success" id="dropdown-basic">
                                Schedules
                            </Dropdown.Toggle>
                            <Dropdown.Menu>
                                {dropdownOptions}
                            </Dropdown.Menu>
                        </Dropdown>

                    </div>
                    <div className="HomePage">
                        <div className="ScheduleName">
                            <h1>{scheduleName}</h1>
                        </div>
                        <div className="ScheduleView">

                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

async function getScheduleNames() {
    const response = await fetch('http://localhost:7979/getScheduleNames');
    const content = await validateJSON(response);
    return content;
}

async function getCurrentSchedule(){
    const response = await fetch('http://localhost:7979/getCurrentSchedule');
    const content = await validateJSON(response);
    return content;
}

function validateJSON(response) {
    if (response.ok) {
        return response.json();
    } else {
        return Promise.reject(response);
    }
}

export default Home;
