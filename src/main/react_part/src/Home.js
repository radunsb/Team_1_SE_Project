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

        updateCalendar(schedule);
    };

    const updateCalendar = (schedule) => {
        const timeSlots = document.querySelectorAll('.TimeSlots tr');
        const WeekDays = document.querySelectorAll('.DaysOfWeek th');
        for (let i = 0; i < timeSlots.length; i++) {

            const row = timeSlots[i];
            // Remove all but the first td item
            const cellsToRemove = row.querySelectorAll('td:not(:first-child)');
            cellsToRemove.forEach(cell => cell.remove());
            //console.log(row.id);

            // Add 5 tds
            for (let j = 0; j < 5; j++) {
                const day = WeekDays[j+1];
                //console.log(day.id);
                const newCell = document.createElement('td');

                // Check if a course is in this timeslot
                for(const course of schedule.courses){
                    //TODO: Ben's code here lol
                }

                newCell.textContent = 'hello';
                row.appendChild(newCell);
            }
        }
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
                            <table className="Calendar">
                                <thead className="WeekDays">
                                    <tr className="DaysOfWeek">
                                        <th id="empty"> </th>
                                        <th id="M"> Monday </th>
                                        <th id="T"> Tuesday </th>
                                        <th id="W"> Wednesday </th>
                                        <th id="R"> Thursday </th>
                                        <th id="F"> Friday </th>
                                    </tr>
                                </thead>
                                <tbody className="TimeSlots">
                                    <tr id="8:00 AM"><td> <strong>8:00</strong> </td></tr>
                                    <tr id="8:30 AM"><td>  </td></tr>
                                    <tr id="9:00 AM"><td> <strong>9:00</strong> </td></tr>
                                    <tr id="9:30 AM"><td>  </td></tr>
                                    <tr id="10:00 AM"><td> <strong>10:00</strong> </td></tr>
                                    <tr id="10:30 AM"><td>  </td></tr>
                                    <tr id="11:00 AM"><td> <strong>11:00</strong> </td></tr>
                                    <tr id="11:30 AM"><td>  </td></tr>
                                    <tr id="12:00 PM"><td> <strong>12:00</strong> </td></tr>
                                    <tr id="12:30 PM"><td>  </td></tr>
                                    <tr id="1:00 PM"><td> <strong>1:00</strong> </td></tr>
                                    <tr id="1:30 PM"><td>  </td></tr>
                                    <tr id="2:00 PM"><td> <strong>2:00</strong> </td></tr>
                                    <tr id="2:30 PM"><td>  </td></tr>
                                    <tr id="3:00 PM"><td> <strong>3:00</strong> </td></tr>
                                    <tr id="3:30 PM"><td>  </td></tr>
                                    <tr id="4:00 PM"><td> <strong>4:00</strong> </td></tr>
                                    <tr id="4:30 PM"><td>  </td></tr>
                                    <tr id="5:00 PM"><td> <strong>5:00</strong> </td></tr>
                                    <tr id="5:30 PM"><td>  </td></tr>
                                    <tr id="6:00 PM"><td> <strong>6:00</strong> </td></tr>
                                    <tr id="6:30 PM"><td>  </td></tr>
                                    <tr id="7:00 PM"><td> <strong>7:00</strong> </td></tr>
                                    <tr id="7:30 PM"><td>  </td></tr>
                                    <tr id="8:00 PM"><td> <strong>8:00</strong> </td></tr>
                                    <tr id="8:30 PM"><td>  </td></tr>
                                </tbody>
                            </table>
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
