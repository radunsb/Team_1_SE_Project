import React from 'react'
import styles from './styles.module.css';
import { useState, useEffect} from 'react';

//JSON object of courses returned from Java backend


async function getClasses(q){
  const response = await fetch(`http://localhost:7979/search/${q}`);
  const content = await response.json();
  const courses = JSON.parse(JSON.stringify(content))
  let toReturn = [];
  courses.forEach((course) => toReturn.push(<Course courseCode={course.courseCode}
  name={course.name} description={course.description} professor={course.professor}
  meetingDays = {course.meetingDays}/>));
  return toReturn;
}

let data = [];

let filters = [];

function timesFormat(timeList){
    let startTimes = [];
    let endTimes = [];
    for(let i = 0; i < 5; i++){
      let times = timeList[i];
      if(!startTimes.includes(times[0]) || !endTimes.includes(times[1])){
        startTimes.push(times[0]);
        endTimes.push(times[1]);
      }
    }
    return <ul>{startTimes[0]} - {endTimes[0]}</ul>
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
            <th>Description</th>
            <th>Professor</th>
            <th>Meeting Days</th>
        </tr>
        {courses}     
      </table>
      </div>
    </div>
    <FilterBar/>
    </div>
    );
}
