import React from 'react'
import styles from './styles.module.css';
import { useState } from 'react';

//JSON object of courses returned from Java backend
let courseJSON = null;

const sampleData = [
    <Course courseCode={"COMP447"} name={"Networked"} description={"Sample desc"}
        professor={"James Borg"} meetingTimes = {["8:00 AM", "10:00 AM"]}
        meetingDays = {['M', 'W', 'F']}/>,
    <Course courseCode={"HUMA110"} name={"Northern Civ"} description={"Ah yes northern civ my favorite class"}
        professor={"Your Mom"} meetingTimes = {["11:00 AM"]} meetingDays = {['T', 'R']}/>,
    <Course courseCode={"BEANS101"} name={"Beans"} description={"Beans"}
        professor={"BIGLEGUMEFAN3007"} meetingTimes = {["1:00 PM"]} meetingDays = {['T', 'R']}/>
]

let data = [];

function updateJSON(newJSON){
  courseJSON = newJSON;
}

function updateCoursesFromJSON(){
  const courses = JSON.parse(courseJSON);
  const courseList = courses.map(course => course);
  data = courseList;
}

function timesFormat(timeList){
    const times = timeList.map(time => <li>{time}</li>);
    return <ul>{times}</ul>
  }
  
  function daysFormat(dayList){
    return dayList.join(", ");
  }
  
  export function Course({courseCode, name, description, professor, meetingTimes,
  meetingDays, meetingLocations, prerequisites, corequisites, year, semester, creditHours, capacity}){
      return (
      <tr className={styles.mainTable}>
          <td>{courseCode}</td>
          <td>{name}</td>
          <td>{description}</td>
          <td>{professor}</td>
          <td>{timesFormat(meetingTimes)}</td>
          <td>{daysFormat(meetingDays)}</td>
      </tr>
      );     
  }

  export function SearchBar(){

    let handleSearchChange = e => {
        setQuery({
            ...query,
            q: e.target.value
        });
    }

    const [query, setQuery] = useState({
        q: ''
    });

    return(
        <div className = "searchbar">
            <input type="text" value={query.q}
                onChange={handleSearchChange}/>
            {query.q}
        </div>
    );
}

export function FilterBar(){
  return(
    <div className = "filterbar">
      <p>Filter stuff goes here</p>
    </div>
  );
}

  export default function Search(){
    return (
    <div className = "main">
    <div className = "submain">
      <SearchBar/>
      <table>
        <tr>
            <th>Course Code</th>
            <th>Course Name</th>
            <th>Description</th>
            <th>Professor</th>
            <th>Meeting Times</th>
            <th>Meeting Days</th>
        </tr>
        {sampleData}     
      </table>
    </div>
    <FilterBar/>
    </div>
    );
}
