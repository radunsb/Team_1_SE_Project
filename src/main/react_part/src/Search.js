import React from 'react'
import styles from './styles.module.css';
import { useState, useEffect} from 'react';
import { Button } from 'react-bootstrap';
import Home from './Home.js';

//JSON object of courses returned from Java backend

async function getSchedule(){
  let response = '';
  response = await fetch(`http://localhost:7979/getCurrentSchedule`);
  const content = await response.json();
  const schedule = JSON.parse(JSON.stringify(content));
  schedule.courses.forEach((course) => takenCourses.push(course.courseCode.substring(0, course.courseCode - 1)));
}

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
  meetingDays = {course.meetingDays} meetingTimes = {course.meetingTimes}
  isTaken = {takenCourses.includes(course.courseCode.substring(0, course.courseCode.length - 1))}/>));
  return toReturn;
}

let takenCourses = [];

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
  meetingDays, isTaken}){

    const buttonClicked = () => {
      if(!isTaken){
        takenCourses.push(courseCode.substring(0, courseCode.length - 1));
      }
      else{
        takenCourses.pop(courseCode.substring(0, courseCode.length - 1));
      }
  };

    return (
      <tr className={styles.mainTable}>
          <td>{courseCode}</td>
          <td>{name}</td>
          <td>{professor}</td>
          <td>{daysFormat(meetingDays)}</td>
          <td>{timesFormat(meetingTimes)}</td>
          <td>{!isTaken ? (
            <Button className = "addButton" onClick = {buttonClicked}>+</Button>
          ) : (
            <Button className = "removeButton" onClick = {buttonClicked}>-</Button>
          )}</td>
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

    const [backClicked, setBackClicked] = useState(false);
    const onBack = () => {
        setBackClicked(true);
    };

    const [courses, setCourses] = useState('');

    useEffect(() => {
      async function fetchData() {
          const courseData = await getClasses(query.q);
          setCourses(courseData);
      }
      fetchData();
  });

    useEffect(() => {
      async function fetchData(){
        await getSchedule();
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
      <div>
      {backClicked ? (<Home/>) : 

      
    <div className = "main">
      
    <div className = "submain">
    
    <div className = "searchbar">
    <Button className="BackBtn" onClick = {onBack}>Back</Button>
          <p>Search by Name or Course Code:</p>
            <input type="text" value={query.q}
                onChange={handleSearchChange}/>
        </div>
    <div className = "coursetable">
      <table>
        <tr>
            <th>Course Code</th>
            <th>Course Name</th>
            <th>Professor</th>
            <th>Meeting Days</th>
            <th>Meeting Time</th>
            <th>Add/Remove</th>
        </tr>
        {courses}     
      </table>
      </div>
    </div>
    <FilterBar/>
    </div>
      }
      </div>
    );
}
