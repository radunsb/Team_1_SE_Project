import React from 'react'
import styles from './styles.module.css';
import { useState, useEffect} from 'react';
import { Button } from 'react-bootstrap';
import Home from './Home.js';
import FilterPart from './FilterPart.js';
import "./Search.css"

//JSON object of courses returned from Java backend

async function getSchedule(){
  let response = '';
  response = await fetch(`http://localhost:7979/getCurrentSchedule`);
  const content = await response.json();
  const schedule = JSON.parse(JSON.stringify(content));
  return schedule;
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

    const addNewClass = async (course) => {
      const url = `http:\/\/localhost:7979/addCourseToSchedule/${course}`;
      await fetch(url);
      getSchedule();
    }

    const removeClass = async (course) => {
      const url = `http:\/\/localhost:7979/removeCourse/${course}`;
      await fetch(url);
      getSchedule();
    }
    

    const buttonClicked = () => {
      if(!isTaken){
        takenCourses.push(courseCode.substring(0, courseCode.length - 1));
        addNewClass(courseCode);
      }
      else{
        takenCourses.pop(courseCode.substring(0, courseCode.length - 1));
        removeClass(courseCode);
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

  export function Filter({input, type}){
    return (
      <p id="filters">{type}: {input}</p>
    );
  }

  function useForceUpdate(){
    const [value, setValue] = useState(0);
    return () => setValue(value => value + 1);
  }

export function FilterBar(){

  const [addingFilter, setAddingFilter] = useState(false);

  const[activeFilters, setActiveFilters] = useState("");

  function switchAddingFilter(){
    setAddingFilter(!addingFilter);
  }

  function removeFilter(){
    setActiveFilters(activeFilters.substring(0, activeFilters.lastIndexOf(",")));
    filters.pop();
    filterStrings.pop();
  }

/*
<form id="filterform" onSubmit={handleFilterChange}>
          <label for = "filterinput">Filter: </label>
          <input type="text" id="filterinput" placeholder = "Format: [type] [input]" onSubmit={handleFilterChange}/>
          <input type="submit" value="Submit"/>
        </form>   
*/

  const forceUpdateFilter = useForceUpdate();

  function yayFilters(){

      if(document.getElementById("filters") != null){
      const inString = document.getElementById("filters").textContent + "";
      const type = inString.substring(0, inString.indexOf(' '));
      const input = inString.substring(inString.indexOf(' '));
      filters.push(<Filter type={type} input={input}/>);
      filterStrings.push(type + "& " + input);
      setActiveFilters(filterStrings.join(", ").replaceAll("&", ":"));      
      forceUpdateFilter();
      }

      switchAddingFilter();
  }

  

  return(
    <div className = "filterbar">  
        <Button className="filterbutton" onClick={switchAddingFilter}>{addingFilter ? "Cancel" : "Add New Filter"}</Button>
        {addingFilter ? <FilterPart/> : ''}
        {addingFilter ? <Button className="filterbutton" onClick={yayFilters}>Save Filter</Button> : ""}
        <Button className="filterbutton" onClick={removeFilter}>Remove Latest Filter</Button>
        <p>Current Filters: </p>
        <p className="activeFilters">{activeFilters}</p>
    </div>
  );
}


  export default function Search(){

    const [backClicked, setBackClicked] = useState(false);
    const onBack = () => {
        filterStrings.length = 0;
        filters.length = 0;
        takenCourses.length = 0;
        setSchedule(null);
        setCourses(null);
        setBackClicked(true);
    };

    const [courses, setCourses] = useState('');
    const [schedule, setSchedule] = useState(null);

    useEffect(() => {
      async function fetchData() {
          const courseData = await getClasses(query.q);
          setCourses(courseData);
      }
      fetchData();
  });

    useEffect(() => {
      async function fetchData(){
        const sched = await getSchedule();
        await setSchedule(sched);
        sched.courses.forEach((course) => {
          if(!takenCourses.includes(course.courseCode.substring(0, course.courseCode.length - 1))){
            takenCourses.push(course.courseCode.substring(0, course.courseCode.length - 1));
          }
        });
        
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
                <p>Taken Courses: {takenCourses}</p>
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
