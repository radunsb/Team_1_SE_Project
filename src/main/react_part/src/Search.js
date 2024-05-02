import React from 'react'
import styles from './styles.module.css';
import { useState, useEffect} from 'react';

//JSON object of courses returned from Java backend


async function getClasses(q){
  let response = '';
  if(filters.length > 0){
    const filtquery = filterStrings.join(",");
    response = await fetch(`http://localhost:7979/search/${q}/${filtquery}`);
  }
  else{
    response = await fetch(`http://localhost:7979/search/${q}`);
  }
  const content = await response.json();
  const courses = JSON.parse(JSON.stringify(content));
  let toReturn = [];
  courses.forEach((course) => toReturn.push(<Course courseCode={course.courseCode}
  name={course.name} description={course.description} professor={course.professor}
  meetingDays = {course.meetingDays} meetingTimes = {course.meetingTimes}/>));
  return toReturn;
}

let data = [];

let filters = [];

let filterStrings = [];

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

function timesFormat(timeList){
    const timeString = (timeList + "").replaceAll(",", "");
    if(timeString.length < 16){
      return <p>No Times</p>
    }
    var st = Number(timeString.substring(0, 8));
    var et = Number(timeString.substring(8, 16));
    return <p>{convertTimeToString(st)} - {convertTimeToString(et)}</p>
  }
  
  function daysFormat(dayList){
    dayList = (dayList + '').split(",");
    const days = ['M', 'T', 'W', 'R', 'F'];
    const realDays = [];
    for(let i = 0; i < 5; i++){
      if(dayList[i].trim() === "true"){
        realDays.push(days[i]);
      }
    }
    return realDays.join(", ");
  }

  
  export function Course({courseCode, name, description, professor, meetingTimes,
  meetingDays, meetingLocations, prerequisites, corequisites, year, semester, creditHours, capacity}){
      return (
      <tr className={styles.mainTable}>
          <td>{courseCode}</td>
          <td>{name}</td>
          <td>{professor}</td>
          <td>{daysFormat(meetingDays)}</td>
          <td>{timesFormat(meetingTimes)}</td>
      </tr>
      );     
  }

  export function FilterSetup(){
    const [type, setType] = useState("Day");

  }

  export function TimeBar(){
    return(
      <div>

      </div>    
    );
  }

  export function Filter({input, type}){
    return (
      <p>{type}: {input}</p>
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
    filterStrings.push(type + "& " + input);
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
        <column>{filterStrings}</column>
      </p>
    </div>
  );
}

  export default function Search(){

    const [courses, setCourses] = useState('');

    useEffect(() => {
      async function fetchData() {
          const courseData = await getClasses(query.q);
          setCourses(courseData);
      }
      fetchData();
  });

    let handleSearchChange = e => {
      setQuery({
          ...query,
          q: e.target.value
      });
      getClasses(query.q);      
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
            <th>Professor</th>
            <th>Meeting Days</th>
            <th>Meeting Time</th>
        </tr>
        {courses}     
      </table>
      </div>
    </div>
    <FilterBar/>
    </div>
    );
}
