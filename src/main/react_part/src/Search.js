import React from 'react'
import styles from './styles.module.css';
import { useState } from 'react';
import "./Search.css"

//JSON object of courses returned from Java backend
let courseJSON = null;

let myCourses = [
  <Course courseCode={"COMP447"} name={"Networked"} description={"Sample desc"}
  professor={"James Borg"} meetingTimes = {["8:00 AM", "10:00 AM"]}
  meetingDays = {['M', 'W', 'F']}/>,
  <Course courseCode={"HUMA110"} name={"Northern Civ"} description={"Ah yes northern civ my favorite class"}
  professor={"Your Mom"} meetingTimes = {["11:00 AM"]} meetingDays = {['T', 'R']}/>,
]

let sampleData = [
    <Course courseCode={"COMP447"} name={"Networked"} description={"Sample desc"}
        professor={"James Borg"} meetingTimes = {["8:00 AM", "10:00 AM"]}
        meetingDays = {['M', 'W', 'F']}/>,
    <Course courseCode={"HUMA110"} name={"Northern Civ"} description={"Ah yes northern civ my favorite class"}
        professor={"Your Mom"} meetingTimes = {["11:00 AM"]} meetingDays = {['T', 'R']}/>,
    <Course courseCode={"BEANS101"} name={"Beans"} description={"Beans"}
        professor={"BIGLEGUMEFAN3007"} meetingTimes = {["1:00 PM"]} meetingDays = {['T', 'R']}/>
]

let data = [];

let filters = [];

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

  export function Filter({input, type}){
    return (
      <p>{type} filter: {input}</p>
    );
  }

  function useForceUpdate(){
    const [value, setValue] = useState(0);
    return () => setValue(value => value + 1);
  }

export function FilterBar(){

  let handleFilterChange = e => {
    e.preventDefault();
    const inString = document.getElementById("filterinput").value + "";
    const type = inString.substring(0, inString.indexOf(' '));
    const input = inString.substring(inString.indexOf(' '));
    filters.push(<Filter type={type} input={input}/>);
    forceUpdateFilter();
  }


  const forceUpdateFilter = useForceUpdate();

  return(
    <div className = "filterbar">
      <p>
        <form id="filterform" onSubmit={handleFilterChange}>
          <label for = "filterinput">Filter: </label>
          <input type="text" id="filterinput" placeholder = "Format: [type] [input]" onSubmit={handleFilterChange}/>
          <input type="submit" value="Submit"/>
        </form>       
        <column>{filters}</column>
      </p>
    </div>
  );
}

  export default function Search(){

    let handleSearchChange = e => {
      setQuery({
          ...query,
          q: e.target.value
      });
      let newArray = [<Course courseCode={"BEANS101"} name={"Beans"} description={"Beans"}
      professor={"BIGLEGUMEFAN3007"} meetingTimes = {["1:00 PM"]} meetingDays = {['T', 'R']}/>]
      sampleData = sampleData.concat(newArray);      
      forceUpdate();
  }

  const [query, setQuery] = useState({
      q: ''
  });

    const forceUpdate = useForceUpdate();

    return (
    <div className = "main">
    <div className = "submain">
    <div className = "searchbar">
          <p>Search by Name or Course Code:</p>
            <input type="text" value={query.q}
                onChange={handleSearchChange}/>
            {query.q}
        </div>
    <div className = "coursetable">
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
    </div>
    <FilterBar/>
    </div>
    );
}
