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
import ReactDOM from 'react-dom';

function Home() {

    const [currentSchedule, setCurrentSchedule] = useState(null);
    const [scheduleName, setScheduleName] = useState(null);

    useEffect(() => {
        fetchCurrentSchedule();
    }, []);

    const [scheduleTitle, setScheduleTitle] = useState("default");
    const fetchCurrentSchedule = async () => {
        const schedule = await getCurrentSchedule();
        setCurrentSchedule(schedule);
        setScheduleName(schedule.scheduleName);
        setScheduleTitle(schedule.scheduleName);
        updateCalendar(schedule);
    };

    const scheduleTitleChange = (inputField) => {
        setScheduleTitle(inputField.target.value);
        changeScheduleName(inputField.target.value);
    };

    const changeScheduleName = async (name) => {
        const url = `http:\/\/localhost:7979/renameSchedule/${name}`;
        await fetch(url);
        fetchScheduleNames();
    }

    const handleRemoveCourse = async (name) => {
        const url = `http:\/\/localhost:7979/removeCourse/${name}`;
        await fetch(url);
        fetchCurrentSchedule();
    }

    const updateCalendar = (schedule) => {
        const scheduleNameHeader = document.querySelectorAll('.titleInput input');
        scheduleNameHeader.textContent = schedule.scheduleName;
        const timeSlots = document.querySelectorAll('.TimeSlots tr');
        const WeekDays = document.querySelectorAll('.DaysOfWeek th');
        for (let i = 0; i < timeSlots.length; i++) {
            const row = timeSlots[i];
            // Remove all but the first td item (to refresh)
            const cellsToRemove = row.querySelectorAll('td:not(:first-child)');
            cellsToRemove.forEach(cell => cell.remove());
            //console.log(row.id);

            // Add 5 tds for new course data
            for (let j = 0; j < 5; j++) {
                const day = WeekDays[j+1];
                //console.log(day.id);
                const newCell = document.createElement('td');

                // Check if a course is in this timeslot
                for(const course of schedule.courses){
                    const hasClass = course.meetingDays[j];
                    //console.log(hasClass);
                    if(hasClass){
                        const times = course.meetingTimes;
                        const timeString = (times + "").replaceAll(",", "");
                        //console.log(timeString);
                        var startTime = Number(timeString.substring(0,8));
                        var endTime = Number(timeString.substring(8,16));
                        const startTimeString = convertTimeToString(startTime);
                        const endTimeString = convertTimeToString(endTime);

                        //console.log(course.courseCode);
                        //console.log(row.id);
                        //console.log(startTimeString);
                        if(row.id === startTimeString){
                            newCell.textContent = course.courseCode; // Set tooltip text
                            newCell.classList.add("courseTime");
                            newCell.setAttribute('data-tooltip', course.courseCode); // Set tooltip text

                            const tooltipContent = document.createElement('div');
                            tooltipContent.className = "tooltip-content";

                            const span = document.createElement('span');
                            span.textContent = course.courseCode;
                            tooltipContent.appendChild(span);

                            const button = document.createElement('button');
                            button.className = "tooltip-button";
                            button.textContent = "Remove";
                            button.addEventListener('click', function() {
                                handleRemoveCourse(course.courseCode);
                            });
                            //Add event handler to the button
                            tooltipContent.appendChild(button);

                            newCell.appendChild(tooltipContent);
                            break;
                        }
                        if(isAfter(row.id, endTimeString) && isAfter(startTimeString, row.id)){
                            //newCell.textContent = course.courseCode;
                            newCell.classList.add("courseTime");
                            break;
                        }
                    }else{
                        newCell.textContent = '';
                    }
                }
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

    const [newScheduleClicked, setNewScheduleClicked] = useState(false);

    const makeNewSchedule = async (name) => {
        name = "newSchedule";
        const url = `http:\/\/localhost:7979/createSchedule/${name}`;
        const response = await fetch(url);
        const content = await validateJSON(response);
        fetchCurrentSchedule();
        updateCalendar(content);
        fetchScheduleNames();
        // Not really necessary -> it might be necessary for updates but haven't tried removing it lol
        setNewScheduleClicked(true);
    }

    const deleteSchedule = async () => {
        //TODO: Make sure they don't delete the last schedule in their list lol

        const url = `http:\/\/localhost:7979/deleteSchedule/${scheduleName}`;
        await fetch(url);
        //change the current schedule
        const scheduleData = await getScheduleNames();

        fetchScheduleNames();
        const url2 = `http:\/\/localhost:7979/changeCurrentSchedule/${scheduleData[0]}`;
        await fetch(url2); // Wait for the request to complete
        fetchCurrentSchedule(); // Refetch the current schedule
    }

    return (
        <div>
            {searchClicked ? (
                <Search />
            ) : (
                <div className="container">
                    <div className="Navigation">
                        <Button className="SearchBtn btn btn-danger" onClick={onSearch}>Search Courses</Button>

                        <Dropdown className="dropdown">
                            <Dropdown.Toggle variant="success" id="dropdown-basic" className="btn btn-danger">
                                Schedules
                            </Dropdown.Toggle>
                            <Dropdown.Menu>
                                {dropdownOptions}
                            </Dropdown.Menu>
                        </Dropdown>

                        <Button className="newScheduleBtn btn btn-danger" onClick={makeNewSchedule}>Create New Schedule <strong>+</strong></Button>

                        <Button className="deleteScheduleBtn btn btn-danger" onClick={deleteSchedule}>Delete This Schedule</Button>
                    </div>
                    <div className="HomePage">
                        <div className="ScheduleName">
                            <input
                                className="titleInput"
                                type="text"
                                value={scheduleTitle}
                                onChange={scheduleTitleChange}
                            />
                        </div>
                        <div className="ScheduleView">
                        <div className="TableContainer">
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
                                    <tr id="8:00" className="time"><td> <strong>8:00</strong> </td></tr>
                                    <tr id="8:15"><td>  </td></tr>
                                    <tr id="8:30"><td>  </td></tr>
                                    <tr id="8:45"><td>  </td></tr>
                                    <tr id="9:00" className="time"><td> <strong>9:00</strong> </td></tr>
                                    <tr id="9:15"><td>  </td></tr>
                                    <tr id="9:30"><td>  </td></tr>
                                    <tr id="9:45"><td>  </td></tr>
                                    <tr id="10:00" className="time"><td> <strong>10:00</strong> </td></tr>
                                    <tr id="10:15"><td>  </td></tr>
                                    <tr id="10:30"><td>  </td></tr>
                                    <tr id="10:45"><td>  </td></tr>
                                    <tr id="11:00" className="time"><td> <strong>11:00</strong> </td></tr>
                                    <tr id="11:15"><td>  </td></tr>
                                    <tr id="11:30"><td>  </td></tr>
                                    <tr id="11:45"><td>  </td></tr>
                                    <tr id="12:00" className="time"><td> <strong>12:00</strong> </td></tr>
                                    <tr id="12:15"><td>  </td></tr>
                                    <tr id="12:30"><td>  </td></tr>
                                    <tr id="12:45"><td>  </td></tr>
                                    <tr id="13:00" className="time"><td> <strong>1:00</strong> </td></tr>
                                    <tr id="13:15"><td>  </td></tr>
                                    <tr id="13:30"><td>  </td></tr>
                                    <tr id="13:45"><td>  </td></tr>
                                    <tr id="14:00 PM" className="time"><td> <strong>2:00</strong> </td></tr>
                                    <tr id="14:15 PM"><td>  </td></tr>
                                    <tr id="14:30"><td>  </td></tr>
                                    <tr id="14:45"><td>  </td></tr>
                                    <tr id="15:00 PM" className="time"><td> <strong>3:00</strong> </td></tr>
                                    <tr id="15:15 PM"><td>  </td></tr>
                                    <tr id="15:30"><td>  </td></tr>
                                    <tr id="15:45"><td>  </td></tr>
                                    <tr id="16:00 PM" className="time"><td> <strong>4:00</strong> </td></tr>
                                    <tr id="16:15 PM"><td>  </td></tr>
                                    <tr id="16:30"><td>  </td></tr>
                                    <tr id="16:45"><td>  </td></tr>
                                    <tr id="17:00" className="time"><td> <strong>5:00</strong> </td></tr>
                                    <tr id="17:15"><td>  </td></tr>
                                    <tr id="17:30"><td>  </td></tr>
                                    <tr id="17:45"><td>  </td></tr>
                                    <tr id="18:00" className="time"><td> <strong>6:00</strong> </td></tr>
                                    <tr id="18:15"><td>  </td></tr>
                                    <tr id="18:30"><td>  </td></tr>
                                    <tr id="18:45"><td>  </td></tr>
                                    <tr id="19:00" className="time"><td> <strong>7:00</strong> </td></tr>
                                    <tr id="19:15"><td>  </td></tr>
                                    <tr id="19:30"><td>  </td></tr>
                                    <tr id="19:45"><td>  </td></tr>
                                    <tr id="20:00" className="time"><td> <strong>8:00</strong> </td></tr>
                                    <tr id="20:15"><td>  </td></tr>
                                    <tr id="20:30"><td>  </td></tr>
                                    <tr id="20:45"><td>  </td></tr>
                                </tbody>
                            </table>
                        </div>
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
    //const content = await response.json();
    const content = await validateJSON(response);
//    console.log("HI");
//    console.log(content);
//    console.log("-----");
    return content;
}

function convertTimeToString(time){
  const base = 18000000;
  const diff = time - base;
  const realTime = diff/3600000;
  const hour = Math.floor(realTime);
  let minute = Math.floor(1/60 * Math.round((realTime - hour)/(1/60)) * 60) + '';
  if (minute.length < 2) {
    minute = '0' + minute;
    }
  return hour + ":" + minute;
}

function isAfter(time1, time2){
    const t1 = time1.split(":");
    const t2 = time2.split(":");
    const h1 = Number(t1[0]);
    const m1 = Number(t1[1]);
    const h2 = Number(t2[0]);
    const m2 = Number(t2[1]);
    if(h2 > h1){
        return true;
    }
    if(h2 === h1 && m2 > m1){
        return true;
    }
    return false;
}

function validateJSON(response) {
    if (response.ok) {
        return response.json();
    } else {
        return Promise.reject(response);
    }
}

export default Home;
